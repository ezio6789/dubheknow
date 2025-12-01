<!-- 复杂详情路由模板
    {
        path: '/graph/extract',
        component: Layout,
        redirect: 'extract',
        hidden: true,
        children: [
            {
                path: 'unstructTaskDocRelDetail',
                component: () => import('@/views/graph/extract/detail/index.vue'),
                name: 'tree',
                meta: { title: '任务文件关联详情', activeMenu: '/graph/unstructTaskDocRel'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ unstructTaskDocRelDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ unstructTaskDocRelDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">任务id</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDocRelDetail.taskId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">文件id</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDocRelDetail.docId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">文件名</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDocRelDetail.docName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDocRelDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(unstructTaskDocRelDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDocRelDetail.remark || '-' }}
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

<script setup name="UnstructTaskDocRel">
  import {getUnstructTaskDocRel } from "@/api/graph/extract/unstructTaskDocRel";
  import { useRoute } from 'vue-router';
  import ComponentOne from "@/views/graph/extract/detail/componentOne.vue";
  import ComponentTwo from "@/views/graph/extract/detail/componentTwo.vue";

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
            getUnstructTaskDocRelDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      unstructTaskDocRelDetail: {
    },
    form: {},
  });

  const {  unstructTaskDocRelDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getUnstructTaskDocRelDetailById() {
        const _id = id ;
    getUnstructTaskDocRel(_id).then(response => {
        unstructTaskDocRelDetail.value = response.data;
    });
  }

  getUnstructTaskDocRelDetailById();

</script>
