<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
     <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px" v-show="showSearch" @submit.prevent>
      <el-form-item label="起点概念id" prop="startSchemaId">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.startSchemaId"
            placeholder="请输入起点概念id"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关系" prop="relation">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.relation"
            placeholder="请输入关系"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="终点概念id" prop="endSchemaId">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.endSchemaId"
            placeholder="请输入终点概念id"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否可逆" prop="inverseFlag">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.inverseFlag"
            placeholder="请输入是否可逆"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker class="el-form-input-width"
            clearable
            v-model="queryParams.createTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="知识库id" prop="knowledgeId">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.knowledgeId"
            placeholder="请输入知识库id"
            clearable
            @keyup.enter="handleQuery"
        />
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

    <div  class="pagecont-bottom">
     <div class="justify-between mb15">
       <el-row :gutter="15" class="btn-style">
         <el-col :span="1.5">
           <el-button type="primary" plain @click="handleAdd" v-hasPermi="['graph:ontology:schemarelation:add']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-xinzeng mr5"></i>新增
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['graph:ontology:schemarelation:edit']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['graph:ontology:schemarelation:remove']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="info" plain  @click="handleImport" v-hasPermi="['graph:ontology:schemarelation:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="warning" plain @click="handleExport" v-hasPermi="['graph:ontology:schemarelation:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-download-line mr5"></i>导出
           </el-button>
         </el-col>
       </el-row>
       <div class="justify-end top-right-btn">
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
       </div>
     </div>
     <el-table stripe height="58vh" v-loading="loading" :data="schemaRelationList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
       <el-table-column type="selection" width="55" align="center" />
       <el-table-column v-if="getColumnVisibility(0)" label="ID" align="center" prop="id" />
       <el-table-column v-if="getColumnVisibility(1)" label="起点概念id" align="center" prop="startSchemaId">
         <template #default="scope">
           {{ scope.row.startSchemaId || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(2)" label="关系" align="center" prop="relation">
         <template #default="scope">
           {{ scope.row.relation || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(3)" label="终点概念id" align="center" prop="endSchemaId">
         <template #default="scope">
           {{ scope.row.endSchemaId || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(4)" label="是否可逆" align="center" prop="inverseFlag">
         <template #default="scope">
           {{ scope.row.inverseFlag || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(7)" label="创建人" align="center" prop="createBy">
         <template #default="scope">
           {{ scope.row.createBy || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(9)" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
         <template #default="scope">
           <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(13)" label="备注" align="center" prop="remark">
         <template #default="scope">
           {{ scope.row.remark || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(14)" label="知识库id" align="center" prop="knowledgeId">
         <template #default="scope">
           {{ scope.row.knowledgeId || '-' }}
         </template>
       </el-table-column>
       <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
         <template #default="scope">
           <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                      v-hasPermi="['graph:ontology:schemarelation:edit']">修改</el-button>
           <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                      v-hasPermi="['graph:ontology:schemarelation:remove']">删除</el-button>
           <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                      v-hasPermi="['graph:ontology:schemarelation:query']">详情</el-button>
           <el-button link type="primary" icon="view" @click="routeTo('/graph/ontology/schemaRelationDetail',scope.row)"
                      v-hasPermi="['graph:ontology:schemarelation:query']">复杂详情</el-button>
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
    </div>

    <!-- 添加或修改关系配置对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="schemaRelationRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
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
        </span>
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

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px"  :append-to="$refs['app-container']" draggable destroy-on-close>
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的关系配置数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
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

<script setup name="SchemaRelation">
  import { listSchemaRelation, getSchemaRelation, delSchemaRelation, addSchemaRelation, updateSchemaRelation } from "@/api/graph/ontology/schemaRelation";
  import {getToken} from "@/utils/auth.js";

  const { proxy } = getCurrentInstance();

  const schemaRelationList = ref([]);

  // 列显隐信息
  const columns = ref([
            { key: 1, label: "起点概念id", visible: true },
            { key: 2, label: "关系", visible: true },
            { key: 3, label: "终点概念id", visible: true },
            { key: 4, label: "是否可逆", visible: true },
            { key: 7, label: "创建人", visible: true },
            { key: 9, label: "创建时间", visible: true },
            { key: 13, label: "备注", visible: true },
            { key: 14, label: "知识库id", visible: true }
  ]);

  const getColumnVisibility = (key) => {
    const column = columns.value.find(col => col.key === key);
    // 如果没有找到对应列配置，默认显示
    if (!column) return true;
    // 如果找到对应列配置，根据visible属性来控制显示
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
  const router = useRouter();

  /*** 用户导入参数 */
  const upload = reactive({
    // 是否显示弹出层（用户导入）
    open: false,
    // 弹出层标题（用户导入）
    title: "",
    // 是否禁用上传
    isUploading: false,
    // 是否更新已经存在的用户数据
    updateSupport: 0,
    // 设置上传的请求头部
    headers: { Authorization: "Bearer " + getToken() },
    // 上传的地址
    url: import.meta.env.VITE_APP_BASE_API + "/graph/schemaRelation/importData"
  });

  const data = reactive({
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

  const { queryParams, form, rules } = toRefs(data);

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

  /** ---------------- 导入相关操作 -----------------**/
  /** 导入按钮操作 */
  function handleImport() {
    upload.title = "关系配置导入";
    upload.open = true;
  }

  /** 下载模板操作 */
  function importTemplate() {
    proxy.download("system/user/importTemplate", {
    }, `schemaRelation_template_${new Date().getTime()}.xlsx`)
  }

  /** 提交上传文件 */
  function submitFileForm() {
    proxy.$refs["uploadRef"].submit();
  };

  /**文件上传中处理 */
  const handleFileUploadProgress = (event, file, fileList) => {
    upload.isUploading = true;
  };

  /** 文件上传成功处理 */
  const handleFileSuccess = (response, file, fileList) => {
    upload.open = false;
    upload.isUploading = false;
    proxy.$refs["uploadRef"].handleRemove(file);
    proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
    getList();
  };
  /** ---------------------------------**/

  function routeTo(link, row) {
    if (link !== "" && link.indexOf("http") !== -1) {
      window.location.href = link;
      return
    }
    if (link !== "") {
      if(link === router.currentRoute.value.path) {
        window.location.reload();
      } else {
        router.push({
          path: link,
          query: {
            id:row.id
          }
        });
      }
    }
  }

  getList();
</script>
