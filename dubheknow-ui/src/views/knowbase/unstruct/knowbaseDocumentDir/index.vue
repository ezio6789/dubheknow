<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="80px"
      @submit.prevent
    >
      <el-form-item label="目录名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入目录名称"
          clearable
          style="width: 220px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb15">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['knowbase:unstruct:documentdir:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="knowbaseDocumentDirList"
      row-key="id"
      :default-expand-all="isExpandAll"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="目录名称" min-width="240">
        <template #default="scope">
          <span>{{ scope.row.name || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" width="120">
        <template #default="scope">
          <span>{{ scope.row.orderNum ?? '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" align="center">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="200">
        <template #default="scope">
          <span>{{ scope.row.remark || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['knowbase:unstruct:documentdir:edit']"
          >修改</el-button>
          <el-button
            link
            type="primary"
            icon="Plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['knowbase:unstruct:documentdir:add']"
          >新增</el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['knowbase:unstruct:documentdir:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="knowbaseDocumentDirRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="上级目录" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="dirOptions"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择上级目录"
            check-strictly
            filterable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="目录名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入目录名称" />
        </el-form-item>
        <el-form-item label="显示排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="KnowbaseDocumentDir">
import { getCurrentInstance, nextTick, reactive, ref, toRefs } from "vue";
import { listKnowbaseDocumentDir, getKnowbaseDocumentDir, delKnowbaseDocumentDir, addKnowbaseDocumentDir, updateKnowbaseDocumentDir } from "@/api/knowbase/unstruct/knowbaseDocumentDir";

const { proxy } = getCurrentInstance();

const knowbaseDocumentDirList = ref([]);
const dirOptions = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const isExpandAll = ref(false);
const refreshTable = ref(true);

const data = reactive({
  form: {},
  queryParams: {
    name: undefined,
    pageSize: 1000
  },
  rules: {
    parentId: [{ required: true, message: "上级目录不能为空", trigger: "change" }],
    name: [{ required: true, message: "目录名称不能为空", trigger: "blur" }],
    orderNum: [{ required: true, message: "显示排序不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

function formatTree(list) {
  return proxy.handleTree(list, "id");
}

function getList() {
  loading.value = true;
  listKnowbaseDocumentDir(queryParams.value).then((response) => {
    const list = response.data?.records ?? response.data ?? [];
    knowbaseDocumentDirList.value = formatTree(list);
    loading.value = false;
  });
}

function reset() {
  form.value = {
    id: undefined,
    parentId: 0,
    name: undefined,
    orderNum: 0,
    remark: undefined,
  };
  proxy.resetForm("knowbaseDocumentDirRef");
}

function handleQuery() {
  getList();
}

function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

function loadDirOptions() {
  return listKnowbaseDocumentDir().then((response) => {
    const list = response.data?.records ?? response.data ?? [];
    dirOptions.value = formatTree(list);
  });
}

function handleAdd(row) {
  reset();
  loadDirOptions().then(() => {
    if (row) {
      form.value.parentId = row.id;
    }
  });
  open.value = true;
  title.value = "新增知识目录";
}

function handleUpdate(row) {
  reset();
  loadDirOptions();
  getKnowbaseDocumentDir(row.id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改知识目录";
  });
}

function submitForm() {
  proxy.$refs["knowbaseDocumentDirRef"].validate((valid) => {
    if (valid) {
      const request = form.value.id ? updateKnowbaseDocumentDir(form.value) : addKnowbaseDocumentDir(form.value);
      request.then(() => {
        proxy.$modal.msgSuccess(form.value.id ? "修改成功" : "新增成功");
        open.value = false;
        getList();
      });
    }
  });
}

function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除名称为"' + (row.name || row.id) + '"的数据项?')
    .then(() => delKnowbaseDocumentDir(row.id))
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

function cancel() {
  open.value = false;
  reset();
}

getList();
</script>
