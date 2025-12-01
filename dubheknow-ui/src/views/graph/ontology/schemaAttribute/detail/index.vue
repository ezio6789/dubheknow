<!-- 复杂详情路由模板
    {
        path: '/graph/ontology',
        component: Layout,
        redirect: 'ontology',
        hidden: true,
        children: [
            {
                path: 'schemaAttributeDetail',
                component: () => import('@/views/graph/ontology/detail/index.vue'),
                name: 'tree',
                meta: { title: '概念属性详情', activeMenu: '/graph/schemaAttribute'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ schemaAttributeDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ schemaAttributeDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">概念id</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.schemaId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">概念名称</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.schemaName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">属性名称</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.name || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">属性名称代码</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.nameCode || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">是否必填</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.requireFlag || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据类型</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.dataType || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">单/多值</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.multipleFlag || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">校验方式</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.validateType || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">最小值</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.minValue || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">最大值</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.maxValue || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(schemaAttributeDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ schemaAttributeDetail.remark || '-' }}
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

<script setup name="SchemaAttribute">
  import {getSchemaAttribute } from "@/api/graph/ontology/schemaAttribute";
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
            getSchemaAttributeDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      schemaAttributeDetail: {
    },
    form: {},
  });

  const {  schemaAttributeDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getSchemaAttributeDetailById() {
        const _id = id ;
    getSchemaAttribute(_id).then(response => {
        schemaAttributeDetail.value = response.data;
    });
  }

  getSchemaAttributeDetailById();

</script>
