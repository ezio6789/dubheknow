<template>
  <div class="schema-container">
    <el-container>
      <el-container>
        <el-aside width="250px">
          <div class="aside-header">
            <el-button type="primary" :icon="Plus" @click="addNode">
              &#x65B0;&#x589E;
            </el-button>
            <el-input :placeholder="'\u8BF7\u8F93\u5165'" :suffix-icon="Search"></el-input>
          </div>
          <el-tree
            :data="treeData"
            :props="defaultProps"
            highlight-current="true"
            :expand-on-click-node="false"
            node-key="id"
            :current-node-key="selectedNode?.id"
            @current-change="handleCurrentChange"
            :default-expanded-keys="expandNodes"
          >
            <template #default="{ node, data }">
              <div class="custom-tree-node">
                <span>{{ data.name }}</span>
                <div v-if="selectedNode && selectedNode.id === data.id">
                  <el-button type="primary" link @click.stop="addNode(data)">
                    <el-icon><Plus /></el-icon>
                  </el-button>
                  <el-button
                    style="margin-left: 4px"
                    type="danger"
                    link
                    @click.stop="delNode(node, data)"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </div>
            </template>
          </el-tree>
        </el-aside>
        <el-main>
          <el-tabs
            v-model="activeName"
            class="demo-tabs"
            @tab-click="handleClick"
          >
            <el-tab-pane label="&#x57FA;&#x672C;&#x4FE1;&#x606F;" name="&#x57FA;&#x672C;&#x4FE1;&#x606F;">
              <BasicInfo
                :nodeData="currentData"
                :selectNode="selectedNode"
                :operation="operation"
              />
            </el-tab-pane>
            <el-tab-pane label="&#x5C5E;&#x6027;&#x4FE1;&#x606F;" name="&#x5C5E;&#x6027;&#x4FE1;&#x606F;">
              <AttributeInfo :nodeData="currentData" />
            </el-tab-pane>
            <el-tab-pane label="&#x56FE;&#x8C31;&#x5173;&#x7CFB;" name="&#x56FE;&#x8C31;&#x5173;&#x7CFB;">
              <GraphInfo :nodeData="currentData" />
            </el-tab-pane>
          </el-tabs>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup name="Schema">
import { ref, watch, onMounted, onBeforeUnmount } from "vue";
import { Search, Plus, Delete } from "@element-plus/icons-vue";
import { getSchema, delSchema } from "@/api/graph/ontology/schema.js";
import BasicInfo from "./components/basicInfo.vue";
import AttributeInfo from "./components/attributeInfo.vue";
import GraphInfo from "./components/graphInfo.vue";
import { emptyForm } from "./data";
import { ElMessageBox, ElMessage } from "element-plus";
import { useRoute } from "vue-router";
import { getExtSchemaTree } from "@/api/graph/ontology/schema.js";
import { useKnowledgeContext } from "@/hooks/useKnowledgeContext";
const { knowledgeId } = useKnowledgeContext();

const props = defineProps({
  showData: {
    type: Array,
    default: () => [],
  },
});

const route = useRoute();
// state
const operation = ref("update");
const treeData = ref([]);
const activeName = ref("\u57FA\u672C\u4FE1\u606F");
const currentData = ref({});
const contextMenuVisible = ref(false);
const selectedNode = ref(null);
const selectedTreeNode = ref(null);
const expandNodes = ref([]);
const basicChnnel = new BroadcastChannel("basic");

const hasTreeData = (val) => Array.isArray(val) && val.length > 0;

// tree config
const defaultProps = {
  children: "children",
  label: "name",
  isLeaf: "isLeaf",
};

// watch incoming tree data
watch(
  () => props.showData,
  (newVal) => {
    if (!hasTreeData(newVal)) return;
    treeData.value = JSON.parse(JSON.stringify(newVal));
    if (selectedNode.value) {
      const findNode = (nodes, id) => {
        for (let node of nodes) {
          if (node.id === id) return node;
          if (node.children) {
            const res = findNode(node.children, id);
            if (res) return res;
          }
        }
        return null;
      };
      const node = findNode(treeData.value, selectedNode.value.id);
      if (node) selectedNode.value = node;
    }
  },
  { deep: true, immediate: true }
);

const loadTreeData = () => {
  const data = { knowledgeId: knowledgeId.value };
  getExtSchemaTree(data)
    .then((res) => {
      treeData.value = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
};

onMounted(() => {
  if (!hasTreeData(props.showData)) {
    loadTreeData();
  } else {
    treeData.value = JSON.parse(JSON.stringify(props.showData));
  }
  basicChnnel.addEventListener("message", (data) => {
    if (data.data) loadTreeData();
  });
});

onBeforeUnmount(() => {
  basicChnnel.close();
});

function handleCurrentChange(currentNode, currentTreeNode) {
  selectedNode.value = currentNode;
  selectedTreeNode.value = currentTreeNode;
  getSchema(currentNode.id)
    .then((res) => {
      currentData.value = res.data;
      operation.value = "update";
    })
    .catch((err) => {
      console.error("\u52A0\u8F7D\u8282\u70B9\u8BE6\u60C5\u5931\u8D25", err);
    });
}

const addNode = () => {
  // create new node
  activeName.value = "\u57FA\u672C\u4FE1\u606F";
  const newNode = JSON.parse(JSON.stringify(emptyForm));
  newNode.name = "test";
  newNode.id = Date.now();
  newNode.children = [];
  newNode.knowledgeId = knowledgeId.value || route.query.id;
  if (selectedNode.value) {
    newNode.pid = selectedNode.value?.id;
    const parentNodeData = selectedNode.value;
    if (!parentNodeData.children) {
      parentNodeData.children = [];
    }
    parentNodeData.children.push(newNode);
    expandNodes.value.push(parentNodeData.id);
    treeData.value = [...treeData.value];
    contextMenuVisible.value = false;
  }
  operation.value = "add";
  currentData.value = newNode;
};

const delData = (id) => {
  delSchema(id)
    .then((res) => {
      ElMessage.success(res.msg);
      currentData.value = {};
      basicChnnel.postMessage(true);
      loadTreeData();
    })
    .catch((err) => {
      console.log(err);
    });
};

const delNode = () => {
  if (!selectedNode.value) {
    console.warn("\u8BF7\u5148\u53F3\u952E\u9009\u4E2D\u4E00\u4E2A\u8282\u70B9");
    contextMenuVisible.value = false;
    return;
  }
  console.log("\u5F85\u5220\u9664\u8282\u70B9: ", selectedNode.value);
  contextMenuVisible.value = false;
  ElMessageBox.confirm("\u786E\u5B9A\u5220\u9664\u9009\u4E2D\u6982\u5FF5?", "\u63D0\u793A", {
    confirmButtonText: "\u786E\u8BA4",
    cancelButtonText: "\u53D6\u6D88",
    type: "warning",
  })
    .then(() => {
      delData(selectedNode.value.id);
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "\u53D6\u6D88\u5220\u9664",
      });
    });
};

const handleClick = (tab) => {
  console.log("\u5207\u6362\u5230\u6807\u7B7E\u9875", tab.label);
};
</script>

<style scoped lang="less">
.schema-container {
  height: 100%;
  margin-top: 10px;
  .el-container {
    & > * {
      margin: 5px 0;
    }
  }
  .aside-header {
    display: flex;
    .el-button {
      width: 50px;
      font-size: 10px;
      margin-right: 10px;
    }
  }
  .custom-tree-node {
    display: flex;
    width: 100%;
    justify-content: space-between;
  }
}
.el-aside {
  background: white;
  height: 80vh;
  padding: 10px;
  position: relative;
}
.el-main {
  background: white;
  height: 80vh;
  margin-left: 5px !important;
}
ul {
  margin: 0;
  padding: 0;
}
li {
  list-style: none;
  padding: 5px 10px;
  border-bottom: 1px solid #eee;
  color: #409eff;
  text-align: center;
  cursor: pointer;
  &:hover {
    background-color: #f5f7fa;
  }
  &:last-child {
    border: none;
  }
}
</style>
<style>
.el-tree {
  max-width: 100% !important;
  font-size: 13px;
  margin-top: 10px;
}
.el-popover.el-popper {
  padding: 0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid #eee;
}
.el-popper__arrow {
  display: none;
}
</style>
