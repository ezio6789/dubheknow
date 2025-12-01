<!-- 复杂详情路由模板
    {
        path: '/graph/ontology',
        component: Layout,
        redirect: 'ontology',
        hidden: true,
        children: [
            {
                path: 'schemaDetail',
                component: () => import('@/views/graph/ontology/detail/index.vue'),
                name: 'tree',
                meta: { title: '概念配置详情', activeMenu: '/graph/schema'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ schemaDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ schemaDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">概念名称</div>
                <div class="infotop-row-value">
                  {{ schemaDetail.name || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">概念描述</div>
                <div class="infotop-row-value">
                  {{ schemaDetail.description || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">概念颜色</div>
                <div class="infotop-row-value">
                  {{ schemaDetail.color || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ schemaDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(schemaDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ schemaDetail.remark || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">知识库id</div>
                <div class="infotop-row-value">
                  {{ schemaDetail.knowledgeId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">父概念id</div>
                <div class="infotop-row-value">
                  {{ schemaDetail.pid || '-' }}
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

<script setup name="Schema">
  import {getSchema } from "@/api/graph/ontology/schema";
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
            getSchemaDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      schemaDetail: {
    },
    form: {},
  });

  const {  schemaDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getSchemaDetailById() {
        const _id = id ;
    getSchema(_id).then(response => {
        schemaDetail.value = response.data;
    });
  }

  getSchemaDetailById();

</script>
