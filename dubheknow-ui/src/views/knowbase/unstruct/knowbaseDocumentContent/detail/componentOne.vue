<template>
  <div class="justify-between mb15">
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['unstruct:knowbaseDocumentContent:add']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['unstruct:knowbaseDocumentContent:export']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-download-line mr5"></i>导出
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </div>
  </div>
  <el-table stripe height="374px" v-loading="loading" :data="knowbaseDocumentContentList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column type="selection" width="55" align="center" />
            <el-table-column v-if="columns[0].visible" label="id" align="center" prop="id" />
            <el-table-column v-if="columns[1].visible" label="知识文档id" align="center" prop="documentId">
              <template #default="scope">
                {{ scope.row.documentId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[2].visible" label="知识内容" align="center" prop="content">
              <template #default="scope">
                {{ scope.row.content || '-' }}
              </template>
            </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
      <template #default="scope">
        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                   v-hasPermi="['unstruct:knowbaseDocumentContent:edit']">修改</el-button>
        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                   v-hasPermi="['unstruct:knowbaseDocumentContent:remove']">删除</el-button>
        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                   v-hasPermi="['unstruct:knowbaseDocumentContent:edit']">详情</el-button>
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

  <!-- 添加或修改知识内容对话框 -->
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
    <el-form ref="knowbaseDocumentContentRef" :model="form" :rules="rules" label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="知识文档id" prop="documentId">
                          <el-input v-model="form.documentId" placeholder="请输入知识文档id" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item label="知识内容">
                          <editor v-model="form.content" :min-height="192"/>
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

  <!-- 知识内容详情对话框 -->
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
    <el-form ref="knowbaseDocumentContentRef" :model="form"  label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="知识文档id" prop="documentId">
                          <div>
                            {{ form.documentId }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="知识内容">
                          <div v-html="form.content" ></div>
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
  import { listKnowbaseDocumentContent, getKnowbaseDocumentContent, delKnowbaseDocumentContent, addKnowbaseDocumentContent, updateKnowbaseDocumentContent } from "@/api/knowbase/unstruct/knowbaseDocumentContent";

  const { proxy } = getCurrentInstance();


  const knowbaseDocumentContentList = ref([]);

  // 列显隐信息
  const columns = ref([
        { key: 0, label: "id", visible: true },
        { key: 1, label: "知识文档id", visible: true },
        { key: 2, label: "知识内容（解析结果）", visible: true }
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
          knowbaseDocumentContentDetail: {
    },
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    documentId: null,
                    content: null
    },
    rules: {
    }
  });

  const { queryParams, form, knowbaseDocumentContentDetail, rules } = toRefs(data);

  /** 查询知识内容列表 */
  function getList() {
    loading.value = true;
    listKnowbaseDocumentContent(queryParams.value).then(response => {
            knowbaseDocumentContentList.value = response.data.rows;
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
                    documentId: null,
                    content: null
    };
    proxy.resetForm("knowbaseDocumentContentRef");
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
    title.value = "添加知识内容";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getKnowbaseDocumentContent(_id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改知识内容";
    });
  }


  /** 详情按钮操作 */
  function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getKnowbaseDocumentContent(_id).then(response => {
      form.value = response.data;
      openDetail.value = true;
      title.value = "知识内容详情";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["knowbaseDocumentContentRef"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateKnowbaseDocumentContent(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        } else {
          addKnowbaseDocumentContent(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除知识内容编号为"' + _ids + '"的数据项？').then(function() {
      return delKnowbaseDocumentContent(_ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('knowbase/knowbaseDocumentContent/export', {
      ...queryParams.value
    }, `knowbaseDocumentContent_${new Date().getTime()}.xlsx`)
  }



  getList();

</script>
