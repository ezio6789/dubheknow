<!-- 复杂详情路由模板
    {
        path: '/graph/ontology',
        component: Layout,
        redirect: 'ontology',
        hidden: true,
        children: [
            {
                path: 'schemaMappingDetail',
                component: () => import('@/views/graph/ontology/detail/index.vue'),
                name: 'tree',
                meta: { title: '概念映射详情', activeMenu: '/graph/schemaMapping'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ schemaMappingDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ schemaMappingDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">任务id</div>
                <div class="infotop-row-value">
                  {{ schemaMappingDetail.taskId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">表名</div>
                <div class="infotop-row-value">
                  {{ schemaMappingDetail.tableName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">表显示名称</div>
                <div class="infotop-row-value">
                  {{ schemaMappingDetail.tableComment || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">实体名称列</div>
                <div class="infotop-row-value">
                  {{ schemaMappingDetail.entityNameField || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">概念id</div>
                <div class="infotop-row-value">
                  {{ schemaMappingDetail.schemaId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">概念名称</div>
                <div class="infotop-row-value">
                  {{ schemaMappingDetail.schemaName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ schemaMappingDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(schemaMappingDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ schemaMappingDetail.remark || '-' }}
                </div>
              </div>
            </el-col>
        </el-row>

      </div>
    </div>

    <div  class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="组件一" name="1">
          <component-one ></component-one>
        </el-tab-pane>
        <el-tab-pane label="组件二" name="2">
          <component-two ></component-two>
        </el-tab-pane>
      </el-tabs>
    </div>


  </div>
</template>

<script setup name="SchemaMapping">
  import {getSchemaMapping } from "@/api/graph/ontology/schemaMapping";
  import { useRoute } from 'vue-router';
  import ComponentOne from "@/views/graph/ontology/detail/componentOne.vue";
  import ComponentTwo from "@/views/graph/ontology/detail/componentTwo.vue";

  const { proxy } = getCurrentInstance();

  const activeName = ref('1')

  const handleClick = (tab, event) => {
    console.log(tab, event)
  }

  const showSearch = ref(true);
  const route = useRoute();
  let id = route.query.id || 1;
  // 监听 id 变化
  watch(
          () => route.query.id,
          (newId) => {
            id = newId || 1;  // 如果 id 为空，使用默认值 1
            getSchemaMappingDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      schemaMappingDetail: {
    },
    form: {},
  });

  const {  schemaMappingDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getSchemaMappingDetailById() {
        const _id = id ;
    getSchemaMapping(_id).then(response => {
        schemaMappingDetail.value = response.data;
    });
  }

  getSchemaMappingDetailById();

</script>
