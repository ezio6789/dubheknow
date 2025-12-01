<template>
  <div class="justify-between mb15">
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['ontology:attributeMapping:add']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['ontology:attributeMapping:export']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-download-line mr5"></i>导出
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </div>
  </div>
  <el-table stripe height="374px" v-loading="loading" :data="attributeMappingList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column type="selection" width="55" align="center" />
            <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
            <el-table-column v-if="columns[1].visible" label="任务id" align="center" prop="taskId">
              <template #default="scope">
                {{ scope.row.taskId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[2].visible" label="表名" align="center" prop="tableName">
              <template #default="scope">
                {{ scope.row.tableName || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[3].visible" label="表显示名称" align="center" prop="tableComment">
              <template #default="scope">
                {{ scope.row.tableComment || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[4].visible" label="字段名" align="center" prop="fieldName">
              <template #default="scope">
                {{ scope.row.fieldName || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[5].visible" label="字段显示名称" align="center" prop="fieldComment">
              <template #default="scope">
                {{ scope.row.fieldComment || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[6].visible" label="属性id" align="center" prop="attributeId">
              <template #default="scope">
                {{ scope.row.attributeId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[7].visible" label="属性名称" align="center" prop="attributeName">
              <template #default="scope">
                {{ scope.row.attributeName || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[10].visible" label="创建人" align="center" prop="createBy">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[12].visible" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="columns[16].visible" label="备注" align="center" prop="remark">
              <template #default="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
      <template #default="scope">
        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                   v-hasPermi="['ontology:attributeMapping:edit']">修改</el-button>
        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                   v-hasPermi="['ontology:attributeMapping:remove']">删除</el-button>
        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                   v-hasPermi="['ontology:attributeMapping:edit']">详情</el-button>
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

  <!-- 添加或修改属性映射对话框 -->
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
    <el-form ref="attributeMappingRef" :model="form" :rules="rules" label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="任务id" prop="taskId">
                          <el-input v-model="form.taskId" placeholder="请输入任务id" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="表名" prop="tableName">
                          <el-input v-model="form.tableName" placeholder="请输入表名" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="表显示名称" prop="tableComment">
                          <el-input v-model="form.tableComment" placeholder="请输入表显示名称" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="字段名" prop="fieldName">
                          <el-input v-model="form.fieldName" placeholder="请输入字段名" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="字段显示名称" prop="fieldComment">
                          <el-input v-model="form.fieldComment" placeholder="请输入字段显示名称" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="属性id" prop="attributeId">
                          <el-input v-model="form.attributeId" placeholder="请输入属性id" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="属性名称" prop="attributeName">
                          <el-input v-model="form.attributeName" placeholder="请输入属性名称" />
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

  <!-- 属性映射详情对话框 -->
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
    <el-form ref="attributeMappingRef" :model="form"  label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="任务id" prop="taskId">
                          <div>
                            {{ form.taskId }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="表名" prop="tableName">
                          <div>
                            {{ form.tableName }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="表显示名称" prop="tableComment">
                          <div>
                            {{ form.tableComment }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="字段名" prop="fieldName">
                          <div>
                            {{ form.fieldName }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="字段显示名称" prop="fieldComment">
                          <div>
                            {{ form.fieldComment }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="属性id" prop="attributeId">
                          <div>
                            {{ form.attributeId }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="属性名称" prop="attributeName">
                          <div>
                            {{ form.attributeName }}
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
  import { listAttributeMapping, getAttributeMapping, delAttributeMapping, addAttributeMapping, updateAttributeMapping } from "@/api/graph/ontology/attributeMapping";

  const { proxy } = getCurrentInstance();


  const attributeMappingList = ref([]);

  // 列显隐信息
  const columns = ref([
        { key: 0, label: "ID", visible: true },
        { key: 1, label: "任务id", visible: true },
        { key: 2, label: "表名", visible: true },
        { key: 3, label: "表显示名称", visible: true },
        { key: 4, label: "字段名", visible: true },
        { key: 5, label: "字段显示名称", visible: true },
        { key: 6, label: "属性id", visible: true },
        { key: 7, label: "属性名称", visible: true },
        { key: 8, label: "是否有效", visible: true },
        { key: 9, label: "删除标志", visible: true },
        { key: 10, label: "创建人", visible: true },
        { key: 11, label: "创建人id", visible: true },
        { key: 12, label: "创建时间", visible: true },
        { key: 13, label: "更新人", visible: true },
        { key: 14, label: "更新人id", visible: true },
        { key: 15, label: "更新时间", visible: true },
        { key: 16, label: "备注", visible: true }
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
          attributeMappingDetail: {
    },
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    taskId: null,
                    tableName: null,
                    tableComment: null,
                    fieldName: null,
                    fieldComment: null,
                    attributeId: null,
                    attributeName: null,
                    createTime: null,
    },
    rules: {
                    taskId: [{ required: true, message: "任务id不能为空", trigger: "blur" }],
                    tableName: [{ required: true, message: "表名不能为空", trigger: "blur" }],
                    fieldName: [{ required: true, message: "字段名不能为空", trigger: "blur" }],
                    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
                    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
                    createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
                    updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
    }
  });

  const { queryParams, form, attributeMappingDetail, rules } = toRefs(data);

  /** 查询属性映射列表 */
  function getList() {
    loading.value = true;
    listAttributeMapping(queryParams.value).then(response => {
            attributeMappingList.value = response.data.rows;
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
                    taskId: null,
                    tableName: null,
                    tableComment: null,
                    fieldName: null,
                    fieldComment: null,
                    attributeId: null,
                    attributeName: null,
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
    proxy.resetForm("attributeMappingRef");
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
    title.value = "添加属性映射";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getAttributeMapping(_id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改属性映射";
    });
  }


  /** 详情按钮操作 */
  function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getAttributeMapping(_id).then(response => {
      form.value = response.data;
      openDetail.value = true;
      title.value = "属性映射详情";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["attributeMappingRef"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateAttributeMapping(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        } else {
          addAttributeMapping(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除属性映射编号为"' + _ids + '"的数据项？').then(function() {
      return delAttributeMapping(_ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('graph/attributeMapping/export', {
      ...queryParams.value
    }, `attributeMapping_${new Date().getTime()}.xlsx`)
  }



  getList();

</script>
