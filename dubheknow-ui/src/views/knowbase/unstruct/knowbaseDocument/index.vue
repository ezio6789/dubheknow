<template>
  <div class="app-container">
    <el-row :gutter="20">
      <splitpanes :horizontal="appStore.device === 'mobile'" class="default-theme">
        <pane size="18">
          <el-col>
            <div class="head-container">
              <el-input
                v-model="dirFilter"
                placeholder="请输入目录名称"
                clearable
                prefix-icon="Search"
                style="margin-bottom: 12px"
              />
            </div>
            <div class="head-container">
              <el-tree
                :data="dirOptions"
                :props="{ label: 'name', children: 'children' }"
                node-key="id"
                highlight-current
                default-expand-all
                :expand-on-click-node="false"
                :filter-node-method="filterDirNode"
                ref="dirTreeRef"
                @node-click="handleDirClick"
              />
            </div>
          </el-col>
        </pane>

        <pane size="82">
          <el-col>
            <el-form
              :model="queryParams"
              ref="queryRef"
              :inline="true"
              v-show="showSearch"
              label-width="90px"
              @submit.prevent
            >
              <el-form-item label="文件名称" prop="name">
                <el-input
                  v-model="queryParams.name"
                  placeholder="请输入文件名称"
                  clearable
                  style="width: 240px"
                  @keyup.enter="handleQuery"
                />
              </el-form-item>
              <el-form-item label="解析状态" prop="parseStatus">
                <el-input
                  v-model="queryParams.parseStatus"
                  placeholder="请输入解析状态"
                  clearable
                  style="width: 240px"
                  @keyup.enter="handleQuery"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-form>

            <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  icon="Plus"
                  @click="handleAdd"
                  v-hasPermi="['knowbase:unstruct:document:add']"
                >新增</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="success"
                  plain
                  icon="Edit"
                  :disabled="single"
                  @click="handleUpdate"
                  v-hasPermi="['knowbase:unstruct:document:edit']"
                >修改</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="danger"
                  plain
                  icon="Delete"
                  :disabled="multiple"
                  @click="handleDelete"
                  v-hasPermi="['knowbase:unstruct:document:remove']"
                >删除</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="info"
                  plain
                  icon="Upload"
                  @click="handleImport"
                  v-hasPermi="['knowbase:unstruct:document:export']"
                >导入</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="warning"
                  plain
                  icon="Download"
                  @click="handleExport"
                  v-hasPermi="['knowbase:unstruct:document:export']"
                >导出</el-button>
              </el-col>
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </el-row>

            <el-table
              v-loading="loading"
              :data="knowbaseDocumentList"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="50" align="center" />
              <el-table-column label="ID" align="center" key="id" prop="id" v-if="columns.id.visible" />
              <el-table-column label="目录名称" align="center" key="categoryName" prop="categoryName" v-if="columns.categoryName.visible" :show-overflow-tooltip="true" />
              <el-table-column label="文件名称" align="center" key="name" prop="name" v-if="columns.name.visible" :show-overflow-tooltip="true" />
              <el-table-column label="文件类型" align="center" key="type" prop="type" v-if="columns.type.visible" :show-overflow-tooltip="true" />
              <el-table-column label="解析状态" align="center" key="parseStatus" prop="parseStatus" v-if="columns.parseStatus.visible" :show-overflow-tooltip="true" />
              <el-table-column label="创建时间" align="center" key="createTime" prop="createTime" v-if="columns.createTime.visible" width="160">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
                <template #default="scope">
                  <el-tooltip content="修改" placement="top">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['knowbase:unstruct:document:edit']"></el-button>
                  </el-tooltip>
                  <el-tooltip content="删除" placement="top">
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['knowbase:unstruct:document:remove']"></el-button>
                  </el-tooltip>
                  <el-tooltip content="详情" placement="top">
                    <el-button link type="primary" icon="View" @click="handleDetail(scope.row)" v-hasPermi="['knowbase:unstruct:document:query']"></el-button>
                  </el-tooltip>
                </template>
              </el-table-column>
            </el-table>

            <pagination
              v-show="total > 0"
              :total="total"
              v-model:page="queryParams.pageNum"
              v-model:limit="queryParams.pageSize"
              @pagination="getList"
            />
          </el-col>
        </pane>
      </splitpanes>
    </el-row>

    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="knowbaseDocumentRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属目录" prop="categoryId">
              <el-tree-select
                v-model="form.categoryId"
                :data="dirOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择所属目录"
                check-strictly
                filterable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文件名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入文件名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="文件类型" prop="type">
              <el-input v-model="form.type" placeholder="请输入文件类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="解析状态" prop="parseStatus">
              <el-input v-model="form.parseStatus" placeholder="请输入解析状态" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="文件地址" prop="url">
              <el-input v-model="form.url" type="textarea" placeholder="请输入文件地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="文件描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入文件描述" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="title" v-model="openDetail" width="700px" append-to-body>
      <el-form :model="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属目录">
              <span>{{ form.categoryName || '-' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文件名称">
              <span>{{ form.name || '-' }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="文件类型">
              <span>{{ form.type || '-' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="解析状态">
              <span>{{ form.parseStatus || '-' }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="文件地址">
              <span>{{ form.url || '-' }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="文件描述">
              <span>{{ form.description || '-' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <span>{{ form.remark || '-' }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body destroy-on-close>
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已存在的知识文件数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">取消</el-button>
          <el-button type="primary" @click="submitFileForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="KnowbaseDocument">
import { getCurrentInstance, onMounted, reactive, ref, toRefs, watch } from "vue";
import useAppStore from "@/store/modules/app";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import { listKnowbaseDocument, getKnowbaseDocument, delKnowbaseDocument, addKnowbaseDocument, updateKnowbaseDocument } from "@/api/knowbase/unstruct/knowbaseDocument";
import { listKnowbaseDocumentDir } from "@/api/knowbase/unstruct/knowbaseDocumentDir";
import { getToken } from "@/utils/auth.js";

const appStore = useAppStore();
const { proxy } = getCurrentInstance();

const knowbaseDocumentList = ref([]);
const dirOptions = ref([]);
const dirFilter = ref("");
const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);

const columns = ref({
  id: { label: "ID", visible: true },
  categoryName: { label: "目录名称", visible: true },
  name: { label: "文件名称", visible: true },
  type: { label: "文件类型", visible: true },
  parseStatus: { label: "解析状态", visible: true },
  createTime: { label: "创建时间", visible: true },
});

const upload = reactive({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/knowbase/knowbaseDocument/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    categoryId: undefined,
    name: undefined,
    parseStatus: undefined,
  },
  rules: {
    categoryId: [{ required: true, message: "所属目录不能为空", trigger: "change" }],
    name: [{ required: true, message: "文件名称不能为空", trigger: "blur" }],
    url: [{ required: true, message: "文件地址不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

watch(dirFilter, (val) => {
  proxy.$refs["dirTreeRef"]?.filter(val);
});

function filterDirNode(value, data) {
  if (!value) return true;
  return data.name?.indexOf(value) !== -1;
}

function formatDirTree(list) {
  return proxy.handleTree(list, "id");
}

function getDirTree() {
  listKnowbaseDocumentDir().then((response) => {
    const list = response.data?.records ?? response.data ?? [];
    dirOptions.value = formatDirTree(list);
  });
}

function getList() {
  loading.value = true;
  listKnowbaseDocument(proxy.addDateRange(queryParams.value, dateRange.value)).then((response) => {
    knowbaseDocumentList.value = response.data?.records ?? response.rows ?? [];
    total.value = response.data?.total ?? response.total ?? 0;
    loading.value = false;
  });
}

function handleDirClick(data) {
  queryParams.value.categoryId = data.id;
  queryParams.value.pageNum = 1;
  handleQuery();
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  queryParams.value.categoryId = undefined;
  proxy.$refs.dirTreeRef?.setCurrentKey(null);
  handleQuery();
}

function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

function handleAdd() {
  reset();
  form.value.categoryId = queryParams.value.categoryId;
  open.value = true;
  title.value = "新增知识文件";
}

function handleUpdate(row) {
  reset();
  const _id = row?.id || ids.value;
  getKnowbaseDocument(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改知识文件";
  });
}

function handleDetail(row) {
  reset();
  const _id = row?.id || ids.value;
  getKnowbaseDocument(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "知识文件详情";
  });
}

function submitForm() {
  proxy.$refs["knowbaseDocumentRef"].validate((valid) => {
    if (valid) {
      const request = form.value.id ? updateKnowbaseDocument(form.value) : addKnowbaseDocument(form.value);
      request
        .then(() => {
          proxy.$modal.msgSuccess(form.value.id ? "修改成功" : "新增成功");
          open.value = false;
          getList();
        })
        .catch(() => {});
    }
  });
}

function handleDelete(row) {
  const _ids = row?.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除知识文件编号为"' + _ids + '"的数据项？')
    .then(() => delKnowbaseDocument(_ids))
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

function handleExport() {
  proxy.download("knowbase/knowbaseDocument/export", { ...queryParams.value }, `knowbaseDocument_${new Date().getTime()}.xlsx`);
}

function handleImport() {
  upload.title = "知识文件导入";
  upload.open = true;
}

function importTemplate() {
  proxy.download("system/user/importTemplate", {}, `knowbaseDocument_template_${new Date().getTime()}.xlsx`);
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
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {
    dangerouslyUseHTMLString: true,
  });
  getList();
};

function reset() {
  form.value = {
    id: null,
    categoryId: null,
    categoryName: null,
    name: null,
    type: null,
    parseStatus: null,
    url: null,
    description: null,
    remark: null,
  };
  proxy.resetForm("knowbaseDocumentRef");
}

function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

onMounted(() => {
  getDirTree();
  getList();
});
</script>
