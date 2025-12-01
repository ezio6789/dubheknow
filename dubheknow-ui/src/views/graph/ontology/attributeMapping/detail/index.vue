<!-- 复杂详情路由模板
    {
        path: '/graph/ontology',
        component: Layout,
        redirect: 'ontology',
        hidden: true,
        children: [
            {
                path: 'attributeMappingDetail',
                component: () => import('@/views/graph/ontology/detail/index.vue'),
                name: 'tree',
                meta: { title: '属性映射详情', activeMenu: '/graph/attributeMapping'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ attributeMappingDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ attributeMappingDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">任务id</div>
                <div class="infotop-row-value">
                  {{ attributeMappingDetail.taskId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">表名</div>
                <div class="infotop-row-value">
                  {{ attributeMappingDetail.tableName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">表显示名称</div>
                <div class="infotop-row-value">
                  {{ attributeMappingDetail.tableComment || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">字段名</div>
                <div class="infotop-row-value">
                  {{ attributeMappingDetail.fieldName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">字段显示名称</div>
                <div class="infotop-row-value">
                  {{ attributeMappingDetail.fieldComment || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">属性id</div>
                <div class="infotop-row-value">
                  {{ attributeMappingDetail.attributeId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">属性名称</div>
                <div class="infotop-row-value">
                  {{ attributeMappingDetail.attributeName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ attributeMappingDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(attributeMappingDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ attributeMappingDetail.remark || '-' }}
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

<script setup name="AttributeMapping">
  import {getAttributeMapping } from "@/api/graph/ontology/attributeMapping";
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
            getAttributeMappingDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      attributeMappingDetail: {
    },
    form: {},
  });

  const {  attributeMappingDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getAttributeMappingDetailById() {
        const _id = id ;
    getAttributeMapping(_id).then(response => {
        attributeMappingDetail.value = response.data;
    });
  }

  getAttributeMappingDetailById();

</script>
