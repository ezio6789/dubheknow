<!-- 复杂详情路由模板
    {
        path: '/graph/extract',
        component: Layout,
        redirect: 'extract',
        hidden: true,
        children: [
            {
                path: 'unstructTaskDetail',
                component: () => import('@/views/graph/extract/detail/index.vue'),
                name: 'tree',
                meta: { title: '非结构化抽取任务详情', activeMenu: '/graph/unstructTask'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ unstructTaskDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ unstructTaskDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">知识库id</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDetail.knowledgeId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">任务名称</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDetail.name || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">任务状态</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDetail.status || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">发布状态</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDetail.publishStatus || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">抽取方式。【0规则 1deepke 2大模型】</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDetail.extractMode || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">发布时间</div>
                <div class="infotop-row-value">{{ parseTime(unstructTaskDetail.publishTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">发布人id</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDetail.publisherId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">发布人</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDetail.publishBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(unstructTaskDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ unstructTaskDetail.remark || '-' }}
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

<script setup name="UnstructTask">
  import {getUnstructTask } from "@/api/graph/extract/unstructTask";
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
            getUnstructTaskDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      unstructTaskDetail: {
    },
    form: {},
  });

  const {  unstructTaskDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getUnstructTaskDetailById() {
        const _id = id ;
    getUnstructTask(_id).then(response => {
        unstructTaskDetail.value = response.data;
    });
  }

  getUnstructTaskDetailById();

</script>
