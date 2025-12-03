<!-- 复杂详情路由模板
    {
        path: '/knowbase/unstruct',
        component: Layout,
        redirect: 'unstruct',
        hidden: true,
        children: [
            {
                path: 'knowbaseDocumentContentDetail',
                component: () => import('@/views/knowbase/unstruct/detail/index.vue'),
                name: 'tree',
                meta: { title: '知识内容详情', activeMenu: '/knowbase/knowbaseDocumentContent'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ knowbaseDocumentContentDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">id</div>
                <div class="infotop-row-value">{{ knowbaseDocumentContentDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">知识文档id</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentContentDetail.documentId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">知识内容</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentContentDetail.content || '-' }}
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

<script setup name="KnowbaseDocumentContent">
  import {getKnowbaseDocumentContent } from "@/api/knowbase/unstruct/knowbaseDocumentContent";
  import { useRoute } from 'vue-router';
  import ComponentOne from "@/views/knowbase/unstruct/detail/componentOne.vue";
  import ComponentTwo from "@/views/knowbase/unstruct/detail/componentTwo.vue";

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
            getKnowbaseDocumentContentDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      knowbaseDocumentContentDetail: {
    },
    form: {},
  });

  const {  knowbaseDocumentContentDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getKnowbaseDocumentContentDetailById() {
        const _id = id ;
    getKnowbaseDocumentContent(_id).then(response => {
        knowbaseDocumentContentDetail.value = response.data;
    });
  }

  getKnowbaseDocumentContentDetailById();

</script>
