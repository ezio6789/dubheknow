<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-banner">
      <h1>欢迎使用</h1>
      <h2>请创建或进入知识图谱以进行后续操作</h2>
    </div>
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="100px"
        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item label="知识图谱名称" prop="name">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            placeholder="请输入知识图谱名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            plain
            type="primary"
            @click="handleQuery"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
          </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="handleAdd"
              v-hasPermi="['ext:extKnowledge:knowledge:add']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['ext:extKnowledge:knowledge:edit']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['ext:extKnowledge:knowledge:remove']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="info"
              plain
              @click="handleImport"
              v-hasPermi="['ext:extKnowledge:knowledge:export']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              @click="handleExport"
              v-hasPermi="['ext:extKnowledge:knowledge:export']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-download-line mr5"></i>导出
            </el-button>
          </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar
            v-model:showSearch="showSearch"
            @queryTable="getList"
            :columns="columns"
          ></right-toolbar>
        </div>
      </div>

      <div class="grid-wrapper" v-loading="loading">
        <div v-if="!loading && !extKnowledgeList.length" class="emptyBg">
          <img src="@/assets/images/noData.png" alt="" />
          <p>暂无记录</p>
        </div>
        <el-row v-else :gutter="16">
          <el-col
            v-for="item in extKnowledgeList"
            :key="item.id"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
            :xl="6"
          >
            <el-card class="knowledge-card" shadow="hover">
              <div class="card-checkbox">
                <el-checkbox v-model="item.checked" @change="syncSelection" />
              </div>
              <div class="card-header">
                <div class="card-title">{{ item.name || "-" }}</div>
              </div>
              <div class="card-desc">
                {{ item.description || "暂无描述" }}
              </div>
              <div class="card-meta">
                <span v-if="getColumnVisibility(6)">创建人：{{ item.createBy || "-" }}</span>
                <span v-if="getColumnVisibility(8)">创建时间：{{ parseTime(item.createTime, "{y}-{m}-{d}") }}</span>
              </div>
              <div class="card-actions">
                <el-button
                  link
                  type="primary"
                  icon="view"
                  @click="routeTo('/index', item)"
                  >进入</el-button
                >
                <el-button
                  link
                  type="primary"
                  icon="Edit"
                  @click="handleUpdate(item)"
                  v-hasPermi="['ext:extKnowledge:knowledge:edit']"
                  >修改</el-button
                >
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  @click="handleDelete(item)"
                  v-hasPermi="['ext:extKnowledge:knowledge:remove']"
                  >删除</el-button
                >
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="extKnowledgeRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="知识图谱名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入知识图谱名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="知识图谱描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                placeholder="请输入内容"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入内容"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取 消</el-button>
          <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      :title="title"
      v-model="openDetail"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="extKnowledgeRef" :model="form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="知识图谱名称" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="知识图谱描述" prop="description">
              <div>
                {{ form.description }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <div>
                {{ form.remark }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      :title="upload.title"
      v-model="upload.open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的知识图谱数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
              >下载模板</el-link
            >
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">取 消</el-button>
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ExtKnowledge">
import {
  getCurrentInstance,
  onBeforeUnmount,
  onMounted,
  reactive,
  ref,
  toRefs,
} from "vue";
import { useRouter } from "vue-router";
import {
  listKnowledge,
  getKnowledge,
  delKnowledge,
  addKnowledge,
  updateKnowledge,
} from "@/api/graph/manager/knowledge";
import { getToken } from "@/utils/auth.js";
import useAppStore from "@/store/modules/app";
import useKnowledgeStore from "@/store/modules/knowledge";
import useSettingsStore from "@/store/modules/settings";
import useTagsViewStore from "@/store/modules/tagsView";

const { proxy } = getCurrentInstance();
const router = useRouter();
const appStore = useAppStore();
const knowledgeStore = useKnowledgeStore();
const settingsStore = useSettingsStore();
const tagsViewStore = useTagsViewStore();
const prevTagsView = ref(settingsStore.tagsView);

onMounted(() => {
  appStore.toggleSideBarHide(true);
  prevTagsView.value = settingsStore.tagsView;
  settingsStore.changeSetting({ key: "tagsView", value: false });
});

onBeforeUnmount(() => {
  settingsStore.changeSetting({ key: "tagsView", value: prevTagsView.value });
});

const extKnowledgeList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "vid类型", visible: true },
  { key: 2, label: "知识图谱名称", visible: true },
  { key: 3, label: "知识图谱描述", visible: true },
  { key: 6, label: "创建人", visible: true },
  { key: 8, label: "创建时间", visible: true },
  { key: 12, label: "备注", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  if (!column) return true;
  return column.visible;
};

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

/*** 用户导入参数 */
const upload = reactive({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/ext/extKnowledge/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    vidType: null,
    name: null,
    description: null,
    createTime: null,
  },
  rules: {
    name: [{ required: true, message: "知识图谱名称不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listKnowledge(queryParams.value).then((response) => {
    const rows = response.data.records || [];
    extKnowledgeList.value = rows.map((item) => ({ ...item, checked: false }));
    total.value = response.data.total;
    loading.value = false;
    syncSelection();
  });
}

function syncSelection() {
  const selected = extKnowledgeList.value.filter((item) => item.checked).map((item) => item.id);
  ids.value = selected;
  single.value = selected.length !== 1;
  multiple.value = !selected.length;
}

function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

function reset() {
  form.value = {
    id: null,
    vidType: null,
    name: null,
    description: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("extKnowledgeRef");
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加知识图谱";
}

function handleUpdate(row) {
  reset();
  const _id = row?.id ?? ids.value;
  getKnowledge(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改知识图谱";
  });
}

function handleDetail(row) {
  reset();
  const _id = row?.id ?? ids.value;
  getKnowledge(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "知识图谱详情";
  });
}

function submitForm() {
  proxy.$refs["extKnowledgeRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateKnowledge(form.value)
          .then(() => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch(() => {});
      } else {
        addKnowledge(form.value)
          .then(() => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          })
          .catch(() => {});
      }
    }
  });
}

function handleDelete(row) {
  const _ids = row?.id ?? ids.value;
  proxy.$modal
    .confirm('是否确认删除知识图谱编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delKnowledge(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

function handleExport() {
  proxy.download(
    "ext/extKnowledge/export",
    {
      ...queryParams.value,
    },
    `extKnowledge_${new Date().getTime()}.xlsx`
  );
}

function handleImport() {
  upload.title = "知识图谱导入";
  upload.open = true;
}

function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `extKnowledge_template_${new Date().getTime()}.xlsx`
  );
}

function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

const handleFileUploadProgress = () => {
  upload.isUploading = true;
};

const handleFileSuccess = (response, file) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    "导入结果",
    { dangerouslyUseHTMLString: true }
  );
  getList();
};

function routeTo(link, row) {
  if (!row) return;
  knowledgeStore.setKnowledge({
    id: row.id,
    name: row.name,
  });
  tagsViewStore.delView({ path: "/know", name: "Know", meta: {} });
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link
      });
    }
  }
}

getList();
</script>

<style scoped>
.pagecont-banner {
  height: 250px;
  background: url("@/assets/system/images/banner.png") no-repeat;
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
}

.pagecont-banner h1 {
  color: #fff;
  font-family: "Microsoft YaHei", "微软雅黑", sans-serif;
  margin-left: 200px;
  font-size: 48px;
}

.pagecont-banner h2 {
  color: #fff;
  font-family: "Microsoft YaHei", "微软雅黑", sans-serif;
  margin: 10px 0 0 200px;
}
.grid-wrapper {
  min-height: 320px;
}

.knowledge-card {
  position: relative;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.card-checkbox {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 1;
}

.card-header {
  padding-right: 28px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  line-height: 1.4;
}

.card-desc {
  color: #606266;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-meta {
  color: #909399;
  font-size: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.card-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: auto;
}

.top-right-btn {
  display: flex;
  align-items: center;
}
</style>

