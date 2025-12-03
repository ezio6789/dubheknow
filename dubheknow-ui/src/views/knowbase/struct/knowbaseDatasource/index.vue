<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="100px"
        @submit.prevent
      >
        <el-form-item label="数据连接名称" prop="datasourceName">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.datasourceName"
            placeholder="请输入数据连接名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="数据源类型" prop="datasourceType">
          <el-select
            class="el-form-input-width"
            v-model="queryParams.datasourceType"
            placeholder="请选择数据源类型"
            clearable
          >
            <el-option
              v-for="dict in datasource_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
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
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['knowbase:struct:datasource:add']" @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table
        stripe
        height="590px"
        v-loading="loading"
        :data="knowbaseDatasourceList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
      >
        <el-table-column label="序号" align="center" width="80">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(1)" label="数据连接名称" align="center" prop="datasourceName" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.datasourceName || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(9)" label="数据连接描述" align="center" prop="description" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.description || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(4)" label="数据库主机地址" align="center" prop="ip" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.ip || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" label="数据源类型" align="center" prop="datasourceType">
          <template #default="scope">
            <dict-tag :options="datasource_type" :value="scope.row.datasourceType" />
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(13)" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(14)" label="更新时间" align="center" prop="updateTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="350">
          <template #default="scope">
            <el-button link type="primary" icon="View" :loading="testingId === scope.row.id" @click="handleTestConnection(scope.row)" v-hasPermi="['knowbase:struct:datasource:query']">测试</el-button>
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['knowbase:struct:datasource:edit']">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['knowbase:struct:datasource:remove']">删除</el-button>
            <el-button link type="primary" icon="View" @click="handleDetail(scope.row)" v-hasPermi="['knowbase:struct:datasource:query']">详情</el-button>
          </template>
        </el-table-column>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/images/noData.png" alt="" />
            <p>暂无记录</p>
          </div>
        </template>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </div>

    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="knowbaseDatasourceRef" :model="form" :rules="rules" label-width="110px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据连接名称" prop="datasourceName">
              <el-input v-model="form.datasourceName" placeholder="请输入数据连接名称" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="数据源类型" prop="datasourceType">
              <el-select v-model="form.datasourceType" placeholder="请选择数据源类型">
                <el-option v-for="dict in datasource_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入账号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input :type="title === '新增数据源' ? 'text' : 'password'" v-model="form.password" placeholder="请输入密码" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据库IP" prop="ip">
              <el-input v-model="form.ip" placeholder="请输入数据库IP" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="端口号" prop="port">
              <el-input v-model="form.port" placeholder="请输入端口号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24" v-if="form.datasourceType !== null && (form.datasourceType === 'DM8' || form.datasourceType === 'Oracle' || form.datasourceType === 'Oracle11')">
            <el-form-item label="模式" prop="sid">
              <el-input v-model="form.sid" placeholder="请输入模式" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-else>
            <el-form-item label="数据库名称" prop="dbname">
              <el-input v-model="form.dbname" placeholder="请输入数据库名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="数据连接描述" prop="description">
              <el-input type="textarea" :min-height="192" v-model="form.description" placeholder="请输入数据连接描述" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取消</el-button>
          <el-button type="primary" size="mini" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="knowbaseDatasourceRef" :model="form" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据连接名称" prop="datasourceName">
              <div>
                {{ form.datasourceName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="datasourceType">
              <dict-tag :options="datasource_type" :value="form.datasourceType" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账号" prop="username">
              <div>
                {{ form.username }}
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <div>***********</div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据库名称" prop="dbname">
              <div>
                {{ form.dbname }}
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="form.datasourceType !== 'DM8' && form.datasourceType !== null">
            <el-form-item label="模式" prop="sid">
              <div>
                {{ form.sid }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据库IP" prop="ip">
              <div>
                {{ form.ip }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="端口号" prop="port">
              <div>
                {{ form.port }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="数据连接描述" prop="description">
              <div>
                {{ form.description }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
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
          <el-button size="mini" @click="cancel">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据源数据
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

<script setup name="KnowbaseDatasource">
import { getCurrentInstance, reactive, ref, toRefs } from "vue";
import { listKnowbaseDatasource, getKnowbaseDatasource, delKnowbaseDatasource, addKnowbaseDatasource, updateKnowbaseDatasource, testKnowbaseDatasource } from "@/api/knowbase/struct/knowbaseDatasource";
import { getToken } from "@/utils/auth.js";

const { proxy } = getCurrentInstance();
const { datasource_type } = proxy.useDict("datasource_type");
const knowbaseDatasourceList = ref([]);

const columns = ref([
  { key: 1, label: "数据连接名称", visible: true },
  { key: 2, label: "数据源类型", visible: true },
  { key: 4, label: "数据库IP", visible: true },
  { key: 9, label: "数据连接描述", visible: true },
  { key: 13, label: "创建时间", visible: true },
  { key: 14, label: "更新时间", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find(col => col.key === key);
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
const defaultSort = ref({ prop: "createTime", order: "desc" });
const testingId = ref(null);

const upload = reactive({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/knowbase/knowbaseDatasource/importData"
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    datasourceName: null,
    datasourceType: null,
    datasourceConfig: null,
    ip: null,
    port: null,
    description: null,
    createTime: null,
    orderByColumn: defaultSort.value.prop,
    isAsc: defaultSort.value.order
  },
  rules: {
    datasourceName: [{ required: true, message: "数据连接名称不能为空", trigger: "blur" }],
    datasourceType: [{ required: true, message: "数据源类型不能为空", trigger: "change" }],
    ip: [{ required: true, message: "数据库IP不能为空", trigger: "blur" }],
    port: [{ required: true, message: "端口号不能为空", trigger: "blur" }],
    username: [{ required: true, message: "账号不能为空", trigger: "blur" }],
    password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
    dbname: [{ required: true, message: "数据库名称不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listKnowbaseDatasource(queryParams.value).then(response => {
    knowbaseDatasourceList.value = response.data?.records ?? response.data?.rows ?? response.data ?? [];
    total.value = response.data?.total ?? response.total ?? 0;
    loading.value = false;
  });
}

function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

function reset() {
  form.value = {
    id: null,
    datasourceName: null,
    datasourceType: null,
    datasourceConfig: null,
    ip: null,
    port: null,
    description: null,
    username: null,
    password: null,
    dbname: null,
    sid: null,
    remark: null
  };
  proxy.resetForm("knowbaseDatasourceRef");
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

function resetQuery() {
  proxy.resetForm("queryRef");
  queryParams.value.orderByColumn = defaultSort.value.prop;
  queryParams.value.isAsc = defaultSort.value.order;
  handleQuery();
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

function handleSortChange(column) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增数据源";
}

function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getKnowbaseDatasource(_id).then(response => {
    form.value = response.data;
    if (form.value.datasourceConfig) {
      try {
        const config = JSON.parse(form.value.datasourceConfig);
        form.value.username = config.username;
        form.value.password = config.password;
        form.value.dbname = config.dbname;
        form.value.sid = config.sid;
      } catch (e) {
        // ignore parse error, keep raw config
      }
    }
    open.value = true;
    title.value = "修改数据源";
  });
}

function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getKnowbaseDatasource(_id).then(response => {
    form.value = response.data;
    if (form.value.datasourceConfig) {
      try {
        const config = JSON.parse(form.value.datasourceConfig);
        form.value.username = config.username;
        form.value.password = config.password;
        form.value.dbname = config.dbname;
        form.value.sid = config.sid;
      } catch (e) {}
    }
    openDetail.value = true;
    title.value = "数据源详情";
  });
}

function handleTestConnection(row) {
  const _id = row.id || ids.value;
  testingId.value = _id;
  testKnowbaseDatasource({ id: _id })
    .then((response) => {
      proxy.$modal.msgSuccess(response?.msg || "连接成功");
    })
    .catch(() => {
      proxy.$modal.msgError("连接失败，请检查配置");
    })
    .finally(() => {
      testingId.value = null;
    });
}

function submitForm() {
  proxy.$refs["knowbaseDatasourceRef"].validate(valid => {
    if (valid) {
      form.value.datasourceConfig = JSON.stringify({
        username: form.value.username,
        password: form.value.password,
        dbname: form.value.dbname,
        sid: form.value.sid
      });
      const request = form.value.id != null ? updateKnowbaseDatasource(form.value) : addKnowbaseDatasource(form.value);
      request.then(() => {
        proxy.$modal.msgSuccess(form.value.id ? "修改成功" : "新增成功");
        open.value = false;
        getList();
      }).catch(() => {});
    }
  });
}

function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除数据连接名称为"' + (row.datasourceName || _ids) + '"的数据项？').then(function() {
    return delKnowbaseDatasource(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

function handleExport() {
  proxy.download('knowbase/knowbaseDatasource/export', {
    ...queryParams.value
  }, `knowbaseDatasource_${new Date().getTime()}.xlsx`);
}

function handleImport() {
  upload.title = "数据源导入";
  upload.open = true;
}

function importTemplate() {
  proxy.download("system/user/importTemplate", {
  }, `knowbaseDatasource_template_${new Date().getTime()}.xlsx`);
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
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
  getList();
};

queryParams.value.orderByColumn = defaultSort.value.prop;
queryParams.value.isAsc = defaultSort.value.order;
getList();
</script>
