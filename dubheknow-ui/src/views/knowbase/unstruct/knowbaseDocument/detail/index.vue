<!-- 复杂详情路由模板
    {
        path: '/knowbase/unstruct',
        component: Layout,
        redirect: 'unstruct',
        hidden: true,
        children: [
            {
                path: 'knowbaseDocumentDetail',
                component: () => import('@/views/knowbase/unstruct/detail/index.vue'),
                name: 'tree',
                meta: { title: '知识文件详情', activeMenu: '/knowbase/knowbaseDocument'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ knowbaseDocumentDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ knowbaseDocumentDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">知识分类id</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentDetail.categoryId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">知识分类名称</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentDetail.categoryName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">文件名称</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentDetail.name || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">文件类型</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentDetail.type || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">解析状态</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentDetail.parseStatus || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">文件url</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentDetail.url || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">文件描述</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentDetail.description || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(knowbaseDocumentDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ knowbaseDocumentDetail.remark || '-' }}
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

<script setup name="KnowbaseDocument">
  import {getKnowbaseDocument } from "@/api/knowbase/unstruct/knowbaseDocument";
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
            getKnowbaseDocumentDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      knowbaseDocumentDetail: {
    },
    form: {},
  });

  const {  knowbaseDocumentDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getKnowbaseDocumentDetailById() {
        const _id = id ;
    getKnowbaseDocument(_id).then(response => {
        knowbaseDocumentDetail.value = response.data;
    });
  }

  getKnowbaseDocumentDetailById();

</script>
