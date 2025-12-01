<template>
  <div class="justify-between mb15">
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['ontology:schemaRelation:add']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['ontology:schemaRelation:export']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-download-line mr5"></i>导出
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </div>
  </div>
  <el-table stripe height="374px" v-loading="loading" :data="schemaRelationList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column type="selection" width="55" align="center" />
            <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
            <el-table-column v-if="columns[1].visible" label="起点概念id" align="center" prop="startSchemaId">
              <template #default="scope">
                {{ scope.row.startSchemaId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[2].visible" label="关系" align="center" prop="relation">
              <template #default="scope">
                {{ scope.row.relation || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[3].visible" label="终点概念id" align="center" prop="endSchemaId">
              <template #default="scope">
                {{ scope.row.endSchemaId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[4].visible" label="是否可逆" align="center" prop="inverseFlag">
              <template #default="scope">
                {{ scope.row.inverseFlag || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[7].visible" label="创建人" align="center" prop="createBy">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[9].visible" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="columns[13].visible" label="备注" align="center" prop="remark">
              <template #default="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[14].visible" label="知识库id" align="center" prop="knowledgeId">
              <template #default="scope">
                {{ scope.row.knowledgeId || '-' }}
              </template>
            </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
      <template #default="scope">
        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                   v-hasPermi="['ontology:schemaRelation:edit']">修改</el-button>
        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                   v-hasPermi="['ontology:schemaRelation:remove']">删除</el-button>
        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                   v-hasPermi="['ontology:schemaRelation:edit']">详情</el-button>
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

  <!-- 添加或修改关系配置对话框 -->
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
    <el-form ref="schemaRelationRef" :model="form" :rules="rules" label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="起点概念id" prop="startSchemaId">
                          <el-input v-model="form.startSchemaId" placeholder="请输入起点概念id" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="关系" prop="relation">
                          <el-input v-model="form.relation" placeholder="请输入关系" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="终点概念id" prop="endSchemaId">
                          <el-input v-model="form.endSchemaId" placeholder="请输入终点概念id" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="是否可逆" prop="inverseFlag">
                          <el-input v-model="form.inverseFlag" placeholder="请输入是否可逆" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="24">
                        <el-form-item label="备注" prop="remark">
                          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="知识库id" prop="knowledgeId">
                          <el-input v-model="form.knowledgeId" placeholder="请输入知识库id" />
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

  <!-- 关系配置详情对话框 -->
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
    <el-form ref="schemaRelationRef" :model="form"  label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="起点概念id" prop="startSchemaId">
                          <div>
                            {{ form.startSchemaId }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="关系" prop="relation">
                          <div>
                            {{ form.relation }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="终点概念id" prop="endSchemaId">
                          <div>
                            {{ form.endSchemaId }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="是否可逆" prop="inverseFlag">
                          <div>
                            {{ form.inverseFlag }}
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
                      <el-col :span="12">
                        <el-form-item label="知识库id" prop="knowledgeId">
                          <div>
                            {{ form.knowledgeId }}
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
  import { listSchemaRelation, getSchemaRelation, delSchemaRelation, addSchemaRelation, updateSchemaRelation } from "@/api/graph/ontology/schemaRelation";

  const { proxy } = getCurrentInstance();


  const schemaRelationList = ref([]);

  // 列显隐信息
  const columns = ref([
        { key: 0, label: "ID", visible: true },
        { key: 1, label: "起点概念id", visible: true },
        { key: 2, label: "关系", visible: true },
        { key: 3, label: "终点概念id", visible: true },
        { key: 4, label: "是否可逆", visible: true },
        { key: 5, label: "是否有效", visible: true },
        { key: 6, label: "删除标志", visible: true },
        { key: 7, label: "创建人", visible: true },
        { key: 8, label: "创建人id", visible: true },
        { key: 9, label: "创建时间", visible: true },
        { key: 10, label: "更新人", visible: true },
        { key: 11, label: "更新人id", visible: true },
        { key: 12, label: "更新时间", visible: true },
        { key: 13, label: "备注", visible: true },
        { key: 14, label: "知识库id", visible: true }
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
          schemaRelationDetail: {
    },
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    startSchemaId: null,
                    relation: null,
                    endSchemaId: null,
                    inverseFlag: null,
                    createTime: null,
                    knowledgeId: null
    },
    rules: {
                    startSchemaId: [{ required: true, message: "起点概念id不能为空", trigger: "blur" }],
                    relation: [{ required: true, message: "关系不能为空", trigger: "blur" }],
                    endSchemaId: [{ required: true, message: "终点概念id不能为空", trigger: "blur" }],
                    inverseFlag: [{ required: true, message: "是否可逆不能为空", trigger: "blur" }],
                    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
                    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
                    createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
                    updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
    }
  });

  const { queryParams, form, schemaRelationDetail, rules } = toRefs(data);

  /** 查询关系配置列表 */
  function getList() {
    loading.value = true;
    listSchemaRelation(queryParams.value).then(response => {
            schemaRelationList.value = response.data.rows;
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
                    startSchemaId: null,
                    relation: null,
                    endSchemaId: null,
                    inverseFlag: null,
                    validFlag: null,
                    delFlag: null,
                    createBy: null,
                    creatorId: null,
                    createTime: null,
                    updateBy: null,
                    updaterId: null,
                    updateTime: null,
                    remark: null,
                    knowledgeId: null
    };
    proxy.resetForm("schemaRelationRef");
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
    title.value = "添加关系配置";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getSchemaRelation(_id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改关系配置";
    });
  }


  /** 详情按钮操作 */
  function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getSchemaRelation(_id).then(response => {
      form.value = response.data;
      openDetail.value = true;
      title.value = "关系配置详情";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["schemaRelationRef"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateSchemaRelation(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        } else {
          addSchemaRelation(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除关系配置编号为"' + _ids + '"的数据项？').then(function() {
      return delSchemaRelation(_ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('graph/schemaRelation/export', {
      ...queryParams.value
    }, `schemaRelation_${new Date().getTime()}.xlsx`)
  }



  getList();

</script>
