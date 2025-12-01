<!-- 复杂详情路由模板
    {
        path: '/graph/extract',
        component: Layout,
        redirect: 'extract',
        hidden: true,
        children: [
            {
                path: 'unstructTaskTextDetail',
                component: () => import('@/views/graph/extract/detail/index.vue'),
                name: 'tree',
                meta: { title: '任务文件段落关联详情', activeMenu: '/graph/unstructTaskText'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ unstructTaskTextDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ unstructTaskTextDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">任务id</div>
                <div class="infotop-row-value">
                  {{ unstructTaskTextDetail.taskId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">文件id</div>
                <div class="infotop-row-value">
                  {{ unstructTaskTextDetail.docId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">段落标识</div>
                <div class="infotop-row-value">
                  {{ unstructTaskTextDetail.paragraphIndex || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">文字内容</div>
                <div class="infotop-row-value">
                  {{ unstructTaskTextDetail.text || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ unstructTaskTextDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(unstructTaskTextDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ unstructTaskTextDetail.remark || '-' }}
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

<script setup name="UnstructTaskText">
  import {getUnstructTaskText } from "@/api/graph/extract/unstructTaskText";
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
            getUnstructTaskTextDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      unstructTaskTextDetail: {
    },
    form: {},
  });

  const {  unstructTaskTextDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getUnstructTaskTextDetailById() {
        const _id = id ;
    getUnstructTaskText(_id).then(response => {
        unstructTaskTextDetail.value = response.data;
    });
  }

  getUnstructTaskTextDetailById();

</script>
