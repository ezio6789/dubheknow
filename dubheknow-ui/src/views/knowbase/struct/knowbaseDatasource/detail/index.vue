<!-- 复杂详情路由模板
    {
        path: '/knowbase/struct',
        component: Layout,
        redirect: 'struct',
        hidden: true,
        children: [
            {
                path: 'knowbaseDatasourceDetail',
                component: () => import('@/views/knowbase/struct/detail/index.vue'),
                name: 'tree',
                meta: { title: '数据源详情', activeMenu: '/knowbase/knowbaseDatasource'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ knowbaseDatasourceDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ knowbaseDatasourceDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据源名称</div>
                <div class="infotop-row-value">
                  {{ knowbaseDatasourceDetail.datasourceName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据源类型</div>
                <div class="infotop-row-value">
                  {{ knowbaseDatasourceDetail.datasourceType || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据源配置(json字符串)</div>
                <div class="infotop-row-value">
                  {{ knowbaseDatasourceDetail.datasourceConfig || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">IP地址</div>
                <div class="infotop-row-value">
                  {{ knowbaseDatasourceDetail.ip || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">端口号</div>
                <div class="infotop-row-value">
                  {{ knowbaseDatasourceDetail.port || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">描述</div>
                <div class="infotop-row-value">
                  {{ knowbaseDatasourceDetail.description || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ knowbaseDatasourceDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(knowbaseDatasourceDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ knowbaseDatasourceDetail.remark || '-' }}
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

<script setup name="KnowbaseDatasource">
  import {getKnowbaseDatasource } from "@/api/knowbase/struct/knowbaseDatasource";
  import { useRoute } from 'vue-router';
  import ComponentOne from "@/views/knowbase/struct/detail/componentOne.vue";
  import ComponentTwo from "@/views/knowbase/struct/detail/componentTwo.vue";

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
            getKnowbaseDatasourceDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      knowbaseDatasourceDetail: {
    },
    form: {},
  });

  const {  knowbaseDatasourceDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getKnowbaseDatasourceDetailById() {
        const _id = id ;
    getKnowbaseDatasource(_id).then(response => {
        knowbaseDatasourceDetail.value = response.data;
    });
  }

  getKnowbaseDatasourceDetailById();

</script>
