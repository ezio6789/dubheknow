<template>
  <div class="justify-between mb15">
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['extract:structTask:add']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['extract:structTask:export']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-download-line mr5"></i>导出
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </div>
  </div>
  <el-table stripe height="374px" v-loading="loading" :data="structTaskList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column type="selection" width="55" align="center" />
            <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
            <el-table-column v-if="columns[1].visible" label="知识库id" align="center" prop="knowledgeId">
              <template #default="scope">
                {{ scope.row.knowledgeId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[2].visible" label="任务名称" align="center" prop="name">
              <template #default="scope">
                {{ scope.row.name || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[3].visible" label="任务状态" align="center" prop="status">
              <template #default="scope">
                {{ scope.row.status || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[4].visible" label="发布状态" align="center" prop="publishStatus">
              <template #default="scope">
                {{ scope.row.publishStatus || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[5].visible" label="发布时间" align="center" prop="publishTime" width="180">
              <template #default="scope">
                <span>{{ parseTime(scope.row.publishTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="columns[6].visible" label="发布人id" align="center" prop="publisherId">
              <template #default="scope">
                {{ scope.row.publisherId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[7].visible" label="发布人" align="center" prop="publishBy">
              <template #default="scope">
                {{ scope.row.publishBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[8].visible" label="数据源id" align="center" prop="datasourceId">
              <template #default="scope">
                {{ scope.row.datasourceId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[9].visible" label="数据源名称" align="center" prop="datasourceName">
              <template #default="scope">
                {{ scope.row.datasourceName || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[12].visible" label="创建人" align="center" prop="createBy">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[14].visible" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="columns[18].visible" label="备注" align="center" prop="remark">
              <template #default="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
      <template #default="scope">
        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                   v-hasPermi="['extract:structTask:edit']">修改</el-button>
        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                   v-hasPermi="['extract:structTask:remove']">删除</el-button>
        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                   v-hasPermi="['extract:structTask:edit']">详情</el-button>
      </template>
    </el-table-column>

    <template #empty>
      <div class="emptyBg">
        <img src="@/assets/images/noData.png" alt="" />
        <p>暂无记录</p>
      </div>
    </template>
  </el-table>

  <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
  />

  <!-- 添加或修改结构化抽取任务对话框 -->
  <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
          <span role="heading" aria-level="2" class="el-dialog__title">
            {{ title }}
            <el-icon size="20" style="color: #909399; font-size: 16px">
              <InfoFilled />
            </el-icon>
          </span>
      <button aria-label="el.dialog.close" class="el-dialog__headerbtn" type="button">
        <i class="el-icon el-dialog__close"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
          <path fill="currentColor"
                d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z">
          </path>
        </svg></i>
      </button>
    </template>
    <el-form ref="structTaskRef" :model="form" :rules="rules" label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="知识库id" prop="knowledgeId">
                          <el-input v-model="form.knowledgeId" placeholder="请输入知识库id" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="任务名称" prop="name">
                          <el-input v-model="form.name" placeholder="请输入任务名称" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="发布时间" prop="publishTime">
                          <el-date-picker clearable
                                          v-model="form.publishTime"
                                          type="date"
                                          value-format="YYYY-MM-DD"
                                          placeholder="请选择发布时间">
                          </el-date-picker>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="发布人id" prop="publisherId">
                          <el-input v-model="form.publisherId" placeholder="请输入发布人id" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="发布人" prop="publishBy">
                          <el-input v-model="form.publishBy" placeholder="请输入发布人" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="数据源id" prop="datasourceId">
                          <el-input v-model="form.datasourceId" placeholder="请输入数据源id" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="数据源名称" prop="datasourceName">
                          <el-input v-model="form.datasourceName" placeholder="请输入数据源名称" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item label="备注" prop="remark">
                          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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

  <!-- 结构化抽取任务详情对话框 -->
  <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
          <el-icon size="20" style="color: #909399; font-size: 16px">
            <InfoFilled />
          </el-icon>
        </span>
      <button aria-label="el.dialog.close" class="el-dialog__headerbtn" type="button">
        <i class="el-icon el-dialog__close"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
          <path fill="currentColor"
                d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z">
          </path>
        </svg></i>
      </button>
    </template>
    <el-form ref="structTaskRef" :model="form"  label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="知识库id" prop="knowledgeId">
                          <div>
                            {{ form.knowledgeId }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="任务名称" prop="name">
                          <div>
                            {{ form.name }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="任务状态" prop="status">
                          <div>
                            {{ form.status }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="发布状态" prop="publishStatus">
                          <div>
                            {{ form.publishStatus }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="发布时间" prop="publishTime">
                          <el-date-picker clearable
                                          v-model="form.publishTime"
                                          type="date"
                                          value-format="YYYY-MM-DD"
                                          placeholder="请选择发布时间">
                          </el-date-picker>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="发布人id" prop="publisherId">
                          <div>
                            {{ form.publisherId }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="发布人" prop="publishBy">
                          <div>
                            {{ form.publishBy }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="数据源id" prop="datasourceId">
                          <div>
                            {{ form.datasourceId }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="数据源名称" prop="datasourceName">
                          <div>
                            {{ form.datasourceName }}
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
        <el-button size="mini" @click="cancel">关 闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ComponentOne">
  import { listStructTask, getStructTask, delStructTask, addStructTask, updateStructTask } from "@/api/graph/extract/structTask";

  const { proxy } = getCurrentInstance();


  const structTaskList = ref([]);

  // 列显隐信息
  const columns = ref([
        { key: 0, label: "ID", visible: true },
        { key: 1, label: "知识库id", visible: true },
        { key: 2, label: "任务名称", visible: true },
        { key: 3, label: "任务状态", visible: true },
        { key: 4, label: "发布状态", visible: true },
        { key: 5, label: "发布时间", visible: true },
        { key: 6, label: "发布人id", visible: true },
        { key: 7, label: "发布人", visible: true },
        { key: 8, label: "数据源id", visible: true },
        { key: 9, label: "数据源名称", visible: true },
        { key: 10, label: "是否有效", visible: true },
        { key: 11, label: "删除标志", visible: true },
        { key: 12, label: "创建人", visible: true },
        { key: 13, label: "创建人id", visible: true },
        { key: 14, label: "创建时间", visible: true },
        { key: 15, label: "更新人", visible: true },
        { key: 16, label: "更新人id", visible: true },
        { key: 17, label: "更新时间", visible: true },
        { key: 18, label: "备注", visible: true }
  ]);

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

  const data = reactive({
          structTaskDetail: {
    },
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    knowledgeId: null,
                    name: null,
                    status: null,
                    publishStatus: null,
                    publishTime: null,
                    publisherId: null,
                    publishBy: null,
                    datasourceId: null,
                    datasourceName: null,
                    createTime: null,
    },
    rules: {
                    name: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
                    status: [{ required: true, message: "任务状态不能为空", trigger: "change" }],
                    publishStatus: [{ required: true, message: "发布状态不能为空", trigger: "change" }],
                    datasourceId: [{ required: true, message: "数据源id不能为空", trigger: "blur" }],
                    datasourceName: [{ required: true, message: "数据源名称不能为空", trigger: "blur" }],
                    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
                    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
                    createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
                    updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
    }
  });

  const { queryParams, form, structTaskDetail, rules } = toRefs(data);

  /** 查询结构化抽取任务列表 */
  function getList() {
    loading.value = true;
    listStructTask(queryParams.value).then(response => {
            structTaskList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
  }

  // 取消按钮
  function cancel() {
    open.value = false;
    openDetail.value = false;
    reset();
  }

  // 表单重置
  function reset() {
    form.value = {
                    id: null,
                    knowledgeId: null,
                    name: null,
                    status: null,
                    publishStatus: null,
                    publishTime: null,
                    publisherId: null,
                    publishBy: null,
                    datasourceId: null,
                    datasourceName: null,
                    validFlag: null,
                    delFlag: null,
                    createBy: null,
                    creatorId: null,
                    createTime: null,
                    updateBy: null,
                    updaterId: null,
                    updateTime: null,
                    remark: null
    };
    proxy.resetForm("structTaskRef");
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
  }

  // 多选框选中数据
  function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }


  /** 排序触发事件 */
  function handleSortChange(column, prop, order) {
    queryParams.value.orderByColumn = column.prop;
    queryParams.value.isAsc = column.order;
    getList();
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加结构化抽取任务";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getStructTask(_id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改结构化抽取任务";
    });
  }


  /** 详情按钮操作 */
  function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getStructTask(_id).then(response => {
      form.value = response.data;
      openDetail.value = true;
      title.value = "结构化抽取任务详情";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["structTaskRef"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateStructTask(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        } else {
          addStructTask(form.value).then(response => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        }
      }
    });
  }

  /** 删除按钮操作 */
  function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal.confirm('是否确认删除结构化抽取任务编号为"' + _ids + '"的数据项？').then(function() {
      return delStructTask(_ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('graph/structTask/export', {
      ...queryParams.value
    }, `structTask_${new Date().getTime()}.xlsx`)
  }



  getList();

</script>
