<template>
  <div class="justify-between mb15">
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['ontology:schemaAttribute:add']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['ontology:schemaAttribute:export']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-download-line mr5"></i>导出
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </div>
  </div>
  <el-table stripe height="374px" v-loading="loading" :data="schemaAttributeList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column type="selection" width="55" align="center" />
            <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
            <el-table-column v-if="columns[1].visible" label="概念id" align="center" prop="schemaId">
              <template #default="scope">
                {{ scope.row.schemaId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[2].visible" label="概念名称" align="center" prop="schemaName">
              <template #default="scope">
                {{ scope.row.schemaName || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[3].visible" label="属性名称" align="center" prop="name">
              <template #default="scope">
                {{ scope.row.name || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[4].visible" label="属性名称代码" align="center" prop="nameCode">
              <template #default="scope">
                {{ scope.row.nameCode || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[5].visible" label="是否必填" align="center" prop="requireFlag">
              <template #default="scope">
                {{ scope.row.requireFlag || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[6].visible" label="数据类型" align="center" prop="dataType">
              <template #default="scope">
                {{ scope.row.dataType || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[7].visible" label="单/多值" align="center" prop="multipleFlag">
              <template #default="scope">
                {{ scope.row.multipleFlag || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[8].visible" label="校验方式" align="center" prop="validateType">
              <template #default="scope">
                {{ scope.row.validateType || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[9].visible" label="最小值" align="center" prop="minValue">
              <template #default="scope">
                {{ scope.row.minValue || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[10].visible" label="最大值" align="center" prop="maxValue">
              <template #default="scope">
                {{ scope.row.maxValue || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[13].visible" label="创建人" align="center" prop="createBy">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[15].visible" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="columns[19].visible" label="备注" align="center" prop="remark">
              <template #default="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
      <template #default="scope">
        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                   v-hasPermi="['ontology:schemaAttribute:edit']">修改</el-button>
        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                   v-hasPermi="['ontology:schemaAttribute:remove']">删除</el-button>
        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                   v-hasPermi="['ontology:schemaAttribute:edit']">详情</el-button>
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

  <!-- 添加或修改概念属性对话框 -->
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
    <el-form ref="schemaAttributeRef" :model="form" :rules="rules" label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="概念id" prop="schemaId">
                          <el-input v-model="form.schemaId" placeholder="请输入概念id" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="概念名称" prop="schemaName">
                          <el-input v-model="form.schemaName" placeholder="请输入概念名称" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="属性名称" prop="name">
                          <el-input v-model="form.name" placeholder="请输入属性名称" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="属性名称代码" prop="nameCode">
                          <el-input v-model="form.nameCode" placeholder="请输入属性名称代码" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="是否必填" prop="requireFlag">
                          <el-input v-model="form.requireFlag" placeholder="请输入是否必填" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="最小值" prop="minValue">
                          <el-input v-model="form.minValue" placeholder="请输入最小值" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="最大值" prop="maxValue">
                          <el-input v-model="form.maxValue" placeholder="请输入最大值" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
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

  <!-- 概念属性详情对话框 -->
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
    <el-form ref="schemaAttributeRef" :model="form"  label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="概念id" prop="schemaId">
                          <div>
                            {{ form.schemaId }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="概念名称" prop="schemaName">
                          <div>
                            {{ form.schemaName }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="属性名称" prop="name">
                          <div>
                            {{ form.name }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="属性名称代码" prop="nameCode">
                          <div>
                            {{ form.nameCode }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="是否必填" prop="requireFlag">
                          <div>
                            {{ form.requireFlag }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="数据类型" prop="dataType">
                          <div>
                            {{ form.dataType }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="校验方式" prop="validateType">
                          <div>
                            {{ form.validateType }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="最小值" prop="minValue">
                          <div>
                            {{ form.minValue }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="最大值" prop="maxValue">
                          <div>
                            {{ form.maxValue }}
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
        <el-button size="mini" @click="cancel">关 闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ComponentOne">
  import { listSchemaAttribute, getSchemaAttribute, delSchemaAttribute, addSchemaAttribute, updateSchemaAttribute } from "@/api/graph/ontology/schemaAttribute";

  const { proxy } = getCurrentInstance();


  const schemaAttributeList = ref([]);

  // 列显隐信息
  const columns = ref([
        { key: 0, label: "ID", visible: true },
        { key: 1, label: "概念id", visible: true },
        { key: 2, label: "概念名称", visible: true },
        { key: 3, label: "属性名称", visible: true },
        { key: 4, label: "属性名称代码", visible: true },
        { key: 5, label: "是否必填", visible: true },
        { key: 6, label: "数据类型", visible: true },
        { key: 7, label: "单/多值", visible: true },
        { key: 8, label: "校验方式", visible: true },
        { key: 9, label: "最小值（用于区间校验）", visible: true },
        { key: 10, label: "最大值（用于区间校验）", visible: true },
        { key: 11, label: "是否有效", visible: true },
        { key: 12, label: "删除标志", visible: true },
        { key: 13, label: "创建人", visible: true },
        { key: 14, label: "创建人id", visible: true },
        { key: 15, label: "创建时间", visible: true },
        { key: 16, label: "更新人", visible: true },
        { key: 17, label: "更新人id", visible: true },
        { key: 18, label: "更新时间", visible: true },
        { key: 19, label: "备注", visible: true }
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
          schemaAttributeDetail: {
    },
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    schemaId: null,
                    schemaName: null,
                    name: null,
                    nameCode: null,
                    requireFlag: null,
                    dataType: null,
                    multipleFlag: null,
                    validateType: null,
                    minValue: null,
                    maxValue: null,
                    createTime: null,
    },
    rules: {
                    schemaId: [{ required: true, message: "概念id不能为空", trigger: "blur" }],
                    schemaName: [{ required: true, message: "概念名称不能为空", trigger: "blur" }],
                    name: [{ required: true, message: "属性名称不能为空", trigger: "blur" }],
                    nameCode: [{ required: true, message: "属性名称代码不能为空", trigger: "blur" }],
                    requireFlag: [{ required: true, message: "是否必填不能为空", trigger: "blur" }],
                    dataType: [{ required: true, message: "数据类型不能为空", trigger: "change" }],
                    multipleFlag: [{ required: true, message: "单/多值不能为空", trigger: "blur" }],
                    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
                    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
                    createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
                    updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
    }
  });

  const { queryParams, form, schemaAttributeDetail, rules } = toRefs(data);

  /** 查询概念属性列表 */
  function getList() {
    loading.value = true;
    listSchemaAttribute(queryParams.value).then(response => {
            schemaAttributeList.value = response.data.rows;
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
                    schemaId: null,
                    schemaName: null,
                    name: null,
                    nameCode: null,
                    requireFlag: null,
                    dataType: null,
                    multipleFlag: null,
                    validateType: null,
                    minValue: null,
                    maxValue: null,
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
    proxy.resetForm("schemaAttributeRef");
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
    title.value = "添加概念属性";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getSchemaAttribute(_id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改概念属性";
    });
  }


  /** 详情按钮操作 */
  function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getSchemaAttribute(_id).then(response => {
      form.value = response.data;
      openDetail.value = true;
      title.value = "概念属性详情";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["schemaAttributeRef"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateSchemaAttribute(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        } else {
          addSchemaAttribute(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除概念属性编号为"' + _ids + '"的数据项？').then(function() {
      return delSchemaAttribute(_ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('graph/schemaAttribute/export', {
      ...queryParams.value
    }, `schemaAttribute_${new Date().getTime()}.xlsx`)
  }



  getList();

</script>
