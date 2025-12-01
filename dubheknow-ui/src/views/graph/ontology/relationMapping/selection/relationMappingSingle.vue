<template>
  <el-dialog
      title="关系映射-单选"
      v-model="visible"
      width="1200px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
      @close="cancel"
  >
    <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
    >
      <el-form-item label="任务id" prop="taskId">
        <el-input
            style="width:240px"
            v-model="queryParams.taskId"
            placeholder="请输入任务id"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表名" prop="tableName">
        <el-input
            style="width:240px"
            v-model="queryParams.tableName"
            placeholder="请输入表名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表显示名称" prop="tableComment">
        <el-input
            style="width:240px"
            v-model="queryParams.tableComment"
            placeholder="请输入表显示名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="字段名" prop="fieldName">
        <el-input
            style="width:240px"
            v-model="queryParams.fieldName"
            placeholder="请输入字段名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="字段显示名称" prop="fieldComment">
        <el-input
            style="width:240px"
            v-model="queryParams.fieldComment"
            placeholder="请输入字段显示名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关系" prop="relation">
        <el-input
            style="width:240px"
            v-model="queryParams.relation"
            placeholder="请输入关系"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联表" prop="relationTable">
        <el-input
            style="width:240px"
            v-model="queryParams.relationTable"
            placeholder="请输入关联表"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联表名称" prop="relationTableName">
        <el-input
            style="width:240px"
            v-model="queryParams.relationTableName"
            placeholder="请输入关联表名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联表字段" prop="relationField">
        <el-input
            style="width:240px"
            v-model="queryParams.relationField"
            placeholder="请输入关联表字段"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联表实体字段" prop="relationNameField">
        <el-input
            style="width:240px"
            v-model="queryParams.relationNameField"
            placeholder="请输入关联表实体字段"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker style="width:240px"
                        clearable
                        v-model="queryParams.createTime"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择创建时间">
        </el-date-picker>
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

    <el-table
        ref="tableRef"
        stripe
        height="300px"
        v-loading="loading"
        :data="dataList"
        highlight-current-row
        row-key="id"
        @current-change="handleCurrentChange"
    >
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="任务id" align="center" prop="taskId">
        <template #default="scope">
          {{ scope.row.taskId || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="表名" align="center" prop="tableName">
        <template #default="scope">
          {{ scope.row.tableName || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="表显示名称" align="center" prop="tableComment">
        <template #default="scope">
          {{ scope.row.tableComment || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="字段名" align="center" prop="fieldName">
        <template #default="scope">
          {{ scope.row.fieldName || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="字段显示名称" align="center" prop="fieldComment">
        <template #default="scope">
          {{ scope.row.fieldComment || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="关系" align="center" prop="relation">
        <template #default="scope">
          {{ scope.row.relation || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="关联表" align="center" prop="relationTable">
        <template #default="scope">
          {{ scope.row.relationTable || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="关联表名称" align="center" prop="relationTableName">
        <template #default="scope">
          {{ scope.row.relationTableName || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="关联表字段" align="center" prop="relationField">
        <template #default="scope">
          {{ scope.row.relationField || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="关联表实体字段" align="center" prop="relationNameField">
        <template #default="scope">
          {{ scope.row.relationNameField || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy">
        <template #default="scope">
          {{ scope.row.createBy || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark">
        <template #default="scope">
          {{ scope.row.remark || '-' }}
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

    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="confirm">
          确 定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="RelationMappingSingle">
  import { listRelationMapping } from "@/api/graph/ontology/relationMapping";
  import { ref } from "vue";
  const { proxy } = getCurrentInstance();


  const dataList = ref([]);
  const loading = ref(true);
  const showSearch = ref(true);
  const total = ref(0);
  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      taskId: null,
      tableName: null,
      tableComment: null,
      fieldName: null,
      fieldComment: null,
      relation: null,
      relationTable: null,
      relationTableName: null,
      relationField: null,
      relationNameField: null,
      createTime: null,
    }
  });
  const { queryParams, form} = toRefs(data);

  // -------------------------------------------
  const visible = ref(false);
  // 定义单选数据
  const single = ref();
  // 当前界面table
  const tableRef = ref();

  const emit = defineEmits(["open", "confirm", "cancel"]);

  /** 单选选中事件 */
  function handleCurrentChange(selection) {
    if (selection) {
      single.value = selection;
    }
  }

  /**
   * 设置当前行
   * @param {Object} row 行对象
   * @returns 更改选中对象
   */
  function setCurrentRow(row) {
    if (row) {
      let data = dataList.value.filter((item) => item.id == row.id);
      tableRef.value?.setCurrentRow(data[0]);
    }
  }

  /**
   * 打开选择框
   * @param {Array} val 选中的对象数组
   */
  function open(val) {
    visible.value = true;
    single.value = val;
    resetQuery();
    getList();
  }

  /**
   * 取消按钮
   * @description 取消按钮时，重置所有状态
   */
  function cancel() {
    queryParams.value.pageNum = 1;
    proxy.resetForm("queryRef");
    visible.value = false;
  }

  /**
   * 确定按钮
   * @description 确定按钮时，emit confirm 事件，以便父组件接收到选中的数据
   */
  function confirm() {
    if (!single.value) {
      proxy.$modal.msgWarning("请选择数据！");
      return;
    }
    emit("confirm", single.value);
    visible.value = false;
  }

  /** 查询字典类型列表 */
  function getList() {
    loading.value = true;
    listRelationMapping(proxy.addDateRange(queryParams.value, daterangeCreateTime.value)).then(
        async (response) => {
          dataList.value = response.data.rows;
          total.value = response.data.total;
          loading.value = false;
          // 初始化及分页切换选中逻辑
          await nextTick();
          setCurrentRow(single.value);
        }
    );
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
    proxy.resetForm("queryRef");
    queryParams.value.pageNum = 1;
    handleQuery();
  }

  defineExpose({ open });
</script>
