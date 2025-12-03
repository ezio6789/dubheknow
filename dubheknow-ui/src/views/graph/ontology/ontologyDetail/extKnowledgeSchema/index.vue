<template>
  <div class="app-container schema-page">
    <el-row :gutter="20">
      <splitpanes class="default-theme">
        <pane size="22">
          <div class="left-panel">
            <div class="pane-header">
              <div class="pane-title">概念</div>
              <div class="pane-actions">
                <el-button link type="primary" size="small" class="action-btn" @click="openAddDialog">
                  <el-icon><Plus /></el-icon>
                  <span>新增</span>
                </el-button>
                <el-button
                  link
                  type="primary"
                  size="small"
                  class="action-btn"
                  :disabled="!selectedNode"
                  @click="openEditDialog"
                >
                  <el-icon><EditPen /></el-icon>
                  <span>修改</span>
                </el-button>
                <el-button
                  link
                  type="danger"
                  size="small"
                  class="action-btn"
                  :disabled="!selectedNode"
                  @click="delNode"
                >
                  <el-icon><DeleteIcon /></el-icon>
                  <span>删除</span>
                </el-button>
              </div>
            </div>
            <el-input
              v-model="treeFilter"
              placeholder="请输入名称"
              clearable
              prefix-icon="Search"
              class="tree-filter"
            />
            <div class="tree-wrap" @click.self="clearSelection">
              <el-tree
                ref="treeRef"
                :data="treeData"
                :props="defaultProps"
                highlight-current
                :expand-on-click-node="false"
                node-key="id"
                :current-node-key="selectedNode?.id"
                @current-change="handleCurrentChange"
                :default-expanded-keys="expandNodes"
                :filter-node-method="filterNode"
                class="schema-tree"
              >
                <template #default="{ data }">
                  <div class="custom-tree-node">
                    <span class="node-label">{{ data.name }}</span>
                  </div>
                </template>
              </el-tree>
            </div>
          </div>
        </pane>

        <pane size="78">
          <div class="content-card">
            <div class="content-header">
              <span class="content-title">{{ selectedNode ? selectedNode.name : "请选择概念" }}</span>
            </div>
            <el-tabs v-model="activeName" class="schema-tabs" @tab-click="handleClick">
              <el-tab-pane label="基本信息" name="基本信息">
                <BasicInfo :nodeData="currentData" />
              </el-tab-pane>
              <el-tab-pane label="属性信息" name="属性信息">
                <AttributeInfo :nodeData="currentData" />
              </el-tab-pane>
              <el-tab-pane label="图谱关系" name="图谱关系">
                <GraphInfo :nodeData="currentData" />
              </el-tab-pane>
            </el-tabs>
          </div>
        </pane>
      </splitpanes>
    </el-row>
    <el-dialog
      title="新增概念"
      v-model="addDialogVisible"
      width="420px"
      append-to-body
      destroy-on-close
    >
      <el-form ref="addFormRef" :model="addForm" :rules="addRules" label-width="90px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="addForm.name" placeholder="请输入概念名称" />
        </el-form-item>
        <el-form-item label="父级概念" prop="parentId">
          <el-tree-select
            v-model="addForm.parentId"
            :data="treeData"
            placeholder="请选择父级概念"
            clearable
            :props="{ value: 'id', label: 'name', children: 'children' }"
            check-strictly
            class="full-width"
          />
        </el-form-item>
        <el-form-item label="概念描述">
          <el-input v-model="addForm.description" type="textarea" placeholder="请输入概念描述" class="full-width-input" />
        </el-form-item>
        <el-form-item label="概念颜色" prop="color">
          <div class="color-picker-row">
            <el-color-picker v-model="addForm.color" />
            <div class="color-presets">
              <span v-for="preset in presetColors" :key="preset" class="color-chip" :style="{ background: preset }" @click="addForm.color = preset"></span>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeAddDialog">取消</el-button>
          <el-button type="primary" @click="submitAdd">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      title="修改概念"
      v-model="editDialogVisible"
      width="420px"
      append-to-body
      destroy-on-close
    >
      <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="90px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="editForm.name" placeholder="请输入概念名称" />
        </el-form-item>
        <el-form-item label="概念描述">
          <el-input v-model="editForm.description" type="textarea" placeholder="请输入概念描述" class="full-width-input" />
        </el-form-item>
        <el-form-item label="概念颜色" prop="color">
          <div class="color-picker-row">
            <el-color-picker v-model="editForm.color" />
            <div class="color-presets">
              <span v-for="preset in presetColors" :key="preset" class="color-chip" :style="{ background: preset }" @click="editForm.color = preset"></span>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeEditDialog">取消</el-button>
          <el-button type="primary" @click="submitEdit">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Schema">
import { ref, watch, onMounted, onBeforeUnmount, reactive } from "vue";
import { Search, Plus, EditPen, Delete as DeleteIcon } from "@element-plus/icons-vue";
import { getSchema, delSchema, getExtSchemaTree, addSchema, updateSchema } from "@/api/graph/ontology/schema.js";
import BasicInfo from "./components/basicInfo.vue";
import AttributeInfo from "./components/attributeInfo.vue";
import GraphInfo from "./components/graphInfo.vue";
import { ElMessageBox, ElMessage } from "element-plus";
import { useRoute } from "vue-router";
import { useKnowledgeContext } from "@/hooks/useKnowledgeContext";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";

const { knowledgeId } = useKnowledgeContext();

const props = defineProps({
  showData: {
    type: Array,
    default: () => [],
  },
});

const route = useRoute();
const treeData = ref([]);
const activeName = ref("基本信息");
const currentData = ref({});
const selectedNode = ref(null);
const selectedTreeNode = ref(null);
const expandNodes = ref([]);
const treeFilter = ref("");
const treeRef = ref(null);
const basicChnnel = new BroadcastChannel("basic");
const addDialogVisible = ref(false);
const addFormRef = ref();
const addForm = reactive({ name: "", parentId: null, description: "", color: "" });
const presetColors = ["#ff7849", "#409eff", "#67c23a", "#f56c6c", "#e6a23c", "#909399"];
const addRules = {
  name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
  parentId: [{ required: true, message: "父级概念不能为空", trigger: "change" }],
  color: [{ required: true, message: "概念颜色不能为空", trigger: "change" }],
};
const editDialogVisible = ref(false);
const editFormRef = ref();
const editForm = reactive({ id: null, name: "", description: "", color: "" });
const editRules = {
  name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
  color: [{ required: true, message: "概念颜色不能为空", trigger: "change" }],
};

const hasTreeData = (val) => Array.isArray(val) && val.length > 0;

const defaultProps = {
  children: "children",
  label: "name",
  isLeaf: "isLeaf",
};

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

watch(treeFilter, (val) => {
  treeRef.value?.filter(val);
});

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

function filterNode(value, data) {
  if (!value) return true;
  return (data.name || "").includes(value);
}

function handleCurrentChange(currentNode, currentTreeNode) {
  selectedNode.value = currentNode;
  selectedTreeNode.value = currentTreeNode;
  if (!currentNode?.id) return;
  getSchema(currentNode.id)
    .then((res) => {
      currentData.value = res.data;
    })
    .catch((err) => {
      console.error("加载节点详情失败", err);
    });
}

const openAddDialog = () => {
  addForm.name = "";
  addForm.description = "";
  addForm.parentId = selectedNode.value?.id || null;
  addForm.color = "";
  addDialogVisible.value = true;
};

const closeAddDialog = () => {
  addDialogVisible.value = false;
  addFormRef.value?.resetFields();
};

const submitAdd = () => {
  addFormRef.value?.validate((valid) => {
    if (!valid) return;
    const payload = {
      name: addForm.name,
      pid: addForm.parentId,
      description: addForm.description,
      knowledgeId: knowledgeId.value || route.query.id,
      color: addForm.color,
    };
    addSchema(payload)
      .then((res) => {
        ElMessage.success(res.msg || "新增成功");
        closeAddDialog();
        loadTreeData();
      })
      .catch(() => {
        ElMessage.error("新增失败，请稍后重试");
      });
  });
};

const openEditDialog = () => {
  if (!selectedNode.value || !currentData.value?.id) {
    ElMessage.warning("请先选择需要修改的概念");
    return;
  }
  editForm.id = currentData.value.id;
  editForm.name = currentData.value.name || "";
  editForm.description = currentData.value.description || "";
  editForm.color = currentData.value.color || "";
  editDialogVisible.value = true;
};

const closeEditDialog = () => {
  editDialogVisible.value = false;
  editFormRef.value?.resetFields();
};

const submitEdit = () => {
  editFormRef.value?.validate((valid) => {
    if (!valid) return;
    const payload = {
      id: editForm.id,
      name: editForm.name,
      description: editForm.description,
      color: editForm.color,
      knowledgeId: knowledgeId.value || route.query.id,
    };
    updateSchema(payload)
      .then((res) => {
        ElMessage.success(res.msg || "修改成功");
        closeEditDialog();
        loadTreeData();
        if (payload.id) {
          getSchema(payload.id).then((resp) => {
            currentData.value = resp.data;
          });
        }
      })
      .catch(() => {
        ElMessage.error("修改失败，请稍后重试");
      });
  });
};

const clearSelection = () => {
  selectedNode.value = null;
  selectedTreeNode.value = null;
  treeRef.value?.setCurrentKey(null);
  currentData.value = {};
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
    ElMessage.warning("请先选择一个类目");
    return;
  }
  ElMessageBox.confirm("确定删除选中概念?", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      delData(selectedNode.value.id);
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "已取消删除",
      });
    });
};

const handleClick = (tab) => {
  console.log("切换到标签页", tab.label);
};
</script>

<style scoped lang="less">
.schema-page {
  height: 100%;
}
.left-panel {
  background: #fff;
  padding: 12px;
  border-radius: 6px;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.05);
  height: 78vh;
  display: flex;
  flex-direction: column;
}
.pane-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.pane-title {
  font-weight: 600;
  width: 60px;
  flex-shrink: 0;
}
.pane-actions {
  display: flex;
  gap: 0px;
}
.pane-actions .action-btn {
  padding: 4px 0px;
  font-weight: 500;
}
.pane-actions .action-btn .el-icon {
  margin-right: 2px;
}
.pane-actions .el-button.is-disabled {
  color: #c0c4cc;
}
.tree-filter {
  margin-bottom: 10px;
}
.tree-wrap {
  flex: 1;
  overflow: auto;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  padding: 6px;
}
.schema-tree {
  max-height: 100%;
  overflow: auto;
}
.custom-tree-node {
  display: flex;
  width: 100%;
  justify-content: space-between;
}
.content-card {
  background: #fff;
  padding: 16px 18px;
  min-height: 78vh;
  border-radius: 8px;
  border: 1px solid #eef0f5;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.04);
}
.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 8px;
  margin-bottom: 12px;
  border-bottom: 1px solid #f0f2f6;
}
.content-title {
  font-weight: 600;
  font-size: 16px;
  color: #1f2d3d;
}
.schema-tabs {
  margin-top: 6px;
}
.schema-tabs :deep(.el-tabs__header) {
  margin: 0 0 10px;
}
.schema-tabs :deep(.el-tabs__nav-wrap::after) {
  display: none;
}
.schema-tabs :deep(.el-tabs__item) {
  padding: 0 16px;
}
.schema-tabs :deep(.el-tabs__item.is-active) {
  font-weight: 600;
}
.color-picker-row {
  display: flex;
  align-items: center;
  gap: 10px;
}
.color-presets {
  display: flex;
  gap: 6px;
}
.color-chip {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  cursor: pointer;
  border: 1px solid #e4e7ed;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
}
.full-width-input {
  width: 100%;
}
</style>
