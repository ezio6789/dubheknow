<template>
  <div class="benti-container">
    <el-container>
      <el-aside width="250px">
        <el-tree
          :data="treeDataList"
          :props="defaultProps"
          highlight-current
          :expand-on-click-node="false"
          node-key="id"
          @current-change="handleCurrentChange"
          v-if="treeDataList.length"
        />
      </el-aside>
      <el-main>
        <RightClickMenu :menuList="menuItems" :pos="pos" @select="handleSelect">
          <div class="benti-graph-container" id="benti-graph-container"></div>
        </RightClickMenu>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, watch, nextTick, onMounted } from "vue";
import RightClickMenu from "@/components/RightClickMenu";
import Vis from "vis-network/dist/vis-network.min.js";
import "vis-network/dist/dist/vis-network.min.css";
import { ElMessage, ElMessageBox } from "element-plus";
import { deleteNode } from "@/api/graph/ontology/schema";
import {
  getExtSchemaGraph,
  getExtSchemaTree,
} from "@/api/graph/ontology/schema.js";
import { useKnowledgeContext } from "@/hooks/useKnowledgeContext";
const { knowledgeId } = useKnowledgeContext();

const menuItems = [
  { label: "添加关系", value: "add" },
  { label: "删除节点", value: "delete" },
];
const pos = ref({ x: 0, y: 0 });
const defaultProps = { children: "children", label: "name", isLeaf: "isLeaf" };

const props = defineProps({
  treeData: { type: Array, default: () => [] },
  showData: { type: Object, default: () => null },
});

const treeDataList = ref([]);
let container = null;
let graph = null;
const graphData = reactive({ nodes: [], edges: [] });
const nodes = ref([]);
const edges = ref([]);
const searchData = ref([]);
const currentNodeData = ref();
const showData = ref();

let startNodeId = null;
let linking = false;

const hasGraphPayload = (val) =>
  val && Array.isArray(val.entities) && Array.isArray(val.relationships);

// -------------------- 工具函数 --------------------
const getRandomColor = (soft = true) => {
  if (!soft) {
    return `#${Math.floor(Math.random() * 0xffffff)
      .toString(16)
      .padStart(6, "0")}`;
  }
  const h = Math.floor(Math.random() * 360);
  const s = 40 + Math.random() * 20; // 40% - 60%
  const l = 60 + Math.random() * 20; // 60% - 80%
  const hslToRgb = (h, s, l) => {
    s /= 100;
    l /= 100;
    const k = (n) => (n + h / 30) % 12;
    const a = s * Math.min(l, 1 - l);
    const f = (n) =>
      l - a * Math.max(-1, Math.min(k(n) - 3, Math.min(9 - k(n), 1)));
    return [
      Math.round(f(0) * 255),
      Math.round(f(8) * 255),
      Math.round(f(4) * 255),
    ];
  };
  const [r, g, b] = hslToRgb(h, s, l);
  return `#${r.toString(16).padStart(2, "0")}${g
    .toString(16)
    .padStart(2, "0")}${b.toString(16).padStart(2, "0")}`;
};

const resetNodeColors = () => {
  nodes.value.forEach((item) => {
    graphData.nodes.update({
      id: item.id,
      step: 0,
      size: 10,
      borderWidth: 0,
      color: { background: item.rowBackground, border: item.rowBorder },
    });
  });
};

const zoomToNode = (node) => {
  graph.moveTo({
    position: graph.getPosition(node.id),
    scale: 1.3,
    animation: { duration: 300, easingFunction: "linear" },
  });
};

const drawTempLine = (from, to) => {
  const ctx = graph.canvas.getContext();
  graph.redraw();
  ctx.save();
  ctx.strokeStyle = "#999";
  ctx.setLineDash([5, 5]);
  ctx.lineWidth = 2;
  ctx.beginPath();
  ctx.moveTo(from.x, from.y);
  ctx.lineTo(to.x, to.y);
  ctx.stroke();
  ctx.restore();
};

const resetLinking = () => {
  linking = false;
  startNodeId = null;
  document.body.style.cursor = "default";
  graph.redraw();
};

// -------------------- 初始化图数据 --------------------
const initGraphData = (data) => {
  nodes.value = data.entities.map((item) => ({
    ...item,
    id: item.id + "",
    label: item.name.length > 6 ? item.name.slice(0, 6) + "..." : item.name,
    rowBackground: getRandomColor(),
    rowBorder: "rgba(59, 130, 246, 0.3)",
    color: { background: getRandomColor(), border: getRandomColor() },
    borderWidth: 0,
    step: 0,
  }));

  edges.value = data.relationships
    .filter(
      (r) =>
        nodes.value.some((n) => n.id == r.startId) &&
        nodes.value.some((n) => n.id == r.endId)
    )
    .map((r) => ({
      ...r,
      id: r.id + "",
      label: r.relationType,
      from: r.startId + "",
      to: r.endId + "",
    }));

  graphData.nodes = new Vis.DataSet(nodes.value);
  graphData.edges = new Vis.DataSet(edges.value);

  searchData.value = nodes.value.map((item) => ({
    ...item,
    value: item.name,
    name: item.name,
  }));
  setGraph(graphData);
};

// -------------------- 初始化图 --------------------
const setGraph = (data) => {
  container = document.getElementById("benti-graph-container");

  container.addEventListener("mousemove", (e) => {
    if (!linking || !startNodeId) return;
    const fromCanvas = graph.canvasToDOM(
      graph.getPositions([startNodeId])[startNodeId]
    );
    drawTempLine(fromCanvas, { x: e.offsetX, y: e.offsetY });
  });

  const options = {
    autoResize: true,
    interaction: { hover: true },
    clickToUse: false,
    layout: { improvedLayout: true },
    nodes: {
      shape: "dot",
      size: 10,
      borderWidth: 0,
      color: { hover: { background: "#89b8ff", border: "#89b8ff4d" } },
      font: { size: 14, color: "#d7e7ff" },
      shapeProperties: { useBorderWithImage: true },
      label: "",
    },
    edges: {
      length: 150,
      arrows: { to: { enabled: true, scaleFactor: 1 } },
      arrowStrikethrough: false,
      color: { color: "#89b8ff", highlight: "#89b8ff", hover: "#89b8ff" },
      hoverWidth: 2,
      smooth: { enabled: true, type: "dynamic" },
      selectionWidth: 4,
      font: { size: 14, color: "#89b8ff", align: "top" },
    },
    physics: {
      enabled: true,
      stabilization: edges.value.length > 100,
      wind: { x: 0, y: 0 },
      barnesHut: {
        gravitationalConstant: -2000,
        springConstant: 0.1,
        damping: 0.08,
      },
    },
  };

  graph = new Vis.Network(container, data, options);
  graph.moveTo({ scale: 1.1 });

  // -------------------- 事件 --------------------
  graph.on("hoverNode", (e) => {
    const node = graphData.nodes.get(e.node);
    node.borderWidth = 15;
    node.size = 20;
    node.color.hover = {
      background: node.step !== -1 ? node.rowBackground : "#89b8ff",
      border: node.step !== -1 ? node.rowBorder : "#89b8ff4d",
    };
    graphData.nodes.update(node);
  });

  graph.on("blurNode", (e) => {
    const node = graphData.nodes.get(e.node);
    node.borderWidth = node.step === 0 ? 0 : 5;
    node.size = node.step < 2 ? 10 : 20;
    graphData.nodes.update(node);
  });

  graph.on("click", async (d) => {
    if (!linking) {
      resetNodeColors();
      if (!d.nodes.length) {
        currentNodeData.value = null;
        setTimeout(() => graph.moveTo({ scale: 1.1 }), 300);
        return;
      }
      currentNodeData.value = nodes.value.find((n) => n.id === d.nodes[0]);
      graphData.nodes.update({
        id: d.nodes[0],
        size: 20,
        borderWidth: 5,
        step: 2,
        color: {
          background: currentNodeData.value.color.background,
          border: currentNodeData.value.color.border,
        },
      });
      zoomToNode(currentNodeData.value);
    } else if (d.nodes.length && startNodeId && d.nodes[0] !== startNodeId) {
      try {
        const endNodeId = d.nodes[0];
        const { value } = await ElMessageBox.prompt("请输入关系类型", "提示", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
        });
        graphData.edges.add({
          from: startNodeId,
          to: endNodeId,
          label: value,
        });
        const startNodeData = nodes.value.find(
          (item) => +item.id === +startNodeId
        );
        const endNodeData = nodes.value.find((item) => +item.id === +endNodeId);
        const newEdges = {
          endId: +endNodeId,
          endName: endNodeData.name,
          from: startNodeId,
          label: value,
          relationType: value,
          startId: +startNodeId,
          startName: startNodeData.name,
          to: endNodeId,
        };
        console.log(newEdges);
        ElMessage.success("添加成功");
        resetLinking();
      } catch {
        ElMessage.info("取消添加");
        resetLinking();
      }
    } else resetLinking();
  });

  graph.on("oncontext", (e) => {
    e.event.preventDefault();
    startNodeId = graph.getNodeAt(e.pointer.DOM);
    if (!startNodeId) return;

    const domPos = graph.canvasToDOM(
      graph.getPositions([startNodeId])[startNodeId]
    );
    const containerRect = graph.body.container.getBoundingClientRect();
    pos.value = {
      x: containerRect.left + domPos.x,
      y: containerRect.top + domPos.y,
    };
  });
  graph.on("dragging", (params) => {
    if (!startNodeId) return;
    const domPos = graph.canvasToDOM(
      graph.getPositions([startNodeId])[startNodeId]
    );
    const containerRect = graph.body.container.getBoundingClientRect();
    pos.value = {
      x: containerRect.left + domPos.x,
      y: containerRect.top + domPos.y,
    };
  });
};

// -------------------- 树节点高亮 --------------------
const handleCurrentChange = (node) => {
  const newNodes = nodes.value.filter((n) => n.id === node.id + "");
  if (!newNodes.length) return graph.moveTo({ scale: 1.1 });

  const nodeIds = newNodes.map((n) => n.id);
  const relatedEdges = edges.value.filter(
    (e) => nodeIds.includes(e.from) || nodeIds.includes(e.to)
  );
  const connectedNodeIds = relatedEdges.map((e) => e.to);
  const connectedNodes = nodes.value.filter((n) =>
    connectedNodeIds.includes(n.id)
  );

  const finalNodes = [...newNodes, ...connectedNodes];
  const nodesHL = finalNodes.map((n) => n.id);
  const edgesHL = relatedEdges.map((e) => e.id);

  resetNodeColors();
  finalNodes.forEach((n) =>
    graphData.nodes.update({ id: n.id, size: 20, borderWidth: 6 })
  );

  graph.setSelection(
    { nodes: nodesHL, edges: edgesHL },
    { highlight: true, unselectAll: true }
  );
  setTimeout(
    () =>
      graph.focus(node.id + "", {
        scale: 1.3,
        animation: { duration: 400, easingFunction: "easeInOutQuad" },
      }),
    300
  );
};

const deleteSelectNode = () => {
  ElMessageBox.confirm("是否删除节点?", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      deleteNode(startNodeId)
        .then((res) => {
          ElMessage.success(res.msg);
          loadGraphData();
        })
        .then((err) => {
          console.log(err);
        });
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "Delete canceled",
      });
    });
};

// -------------------- 右键菜单操作 --------------------
const handleSelect = (data) => {
  const event = data.value;
  switch (event) {
    case "add":
      linking = true;
      document.body.style.cursor = "crosshair";
      break;
    case "delete":
      deleteSelectNode();
      break;
  }
};

// -------------------- 数据监听 --------------------
watch(
  () => props.showData,
  (newVal) => {
    if (!hasGraphPayload(newVal)) return;
    showData.value = newVal;
    nextTick(() => initGraphData(newVal));
  },
  { deep: true, immediate: true }
);

watch(
  () => props.treeData,
  (newVal) => {
    if (newVal && newVal.length) treeDataList.value = newVal;
  },
  { deep: true, immediate: true }
);

const loadGraphData = () => {
  const data = { knowledgeId: knowledgeId.value };
  getExtSchemaGraph(data)
    .then((res) => {
      showData.value = res.data;
      initGraphData(res.data);
    })
    .catch((err) => {
      console.log(err);
    });
};

const loadTreeData = () => {
  const data = { knowledgeId: knowledgeId.value };
  getExtSchemaTree(data)
    .then((res) => {
      treeDataList.value = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
};

onMounted(() => {
  if (!hasGraphPayload(props.showData)) {
    loadGraphData();
  }
  if (!props.treeData?.length) {
    loadTreeData();
  } else {
    treeDataList.value = props.treeData;
  }
});
</script>

<style scoped lang="scss">
.benti-container {
  height: 100%;
  margin-top: 10px;

  .el-container {
    display: flex;
    & > * {
      margin: 5px 0;
    }
  }
}

.el-aside {
  background: #fff;
  height: 80vh;
  padding: 10px;
  overflow: auto;
}

.el-main {
  background: #fff;
  height: 80vh;
  margin-left: 5px;
  padding: 0;
  overflow: hidden;
}

.benti-graph-container {
  width: 100%;
  height: 100%;
  background: #fff url("@/assets/ke/images/bg1.png") no-repeat;
  background-size: 100% 100%;
  transition: width 0.3s;
  &.detailShow {
    width: calc(100% - 400px);
  }
  :deep(.g6-toolbar .g6-toolbar-item) {
    fill: #7dbffa;
    &:hover {
      background-color: rgba(255, 255, 255, 0.2);
    }
  }
}
</style>
