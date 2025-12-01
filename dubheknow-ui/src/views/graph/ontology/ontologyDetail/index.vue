<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-row>
        <el-col :span="21">
          <div>{{ route.query.name }}</div>
        </el-col>
        <el-col :span="3">
          <div class="button-group">
            <el-button
              :type="currentPageType === 'benti' ? 'success' : 'info'"
              size="small"
              plain
              @click="switchPageType('benti')"
            >
              本体图谱
            </el-button>
            <!-- <el-button
              :type="currentPageType === 'entity' ? 'success' : 'info'"
              plain
              size="small"
              @click="switchPageType('entity')"
            >
              实体数据
            </el-button> -->
            <el-button
              :type="currentPageType === 'concept' ? 'success' : 'info'"
              size="small"
              plain
              @click="switchPageType('concept')"
            >
              概念模型
            </el-button>
            <!-- <el-button
              :type="currentPageType === 'relation' ? 'success' : 'info'"
              size="small"
              plain
              @click="switchPageType('relation')"
            >
              关系模型
            </el-button> -->
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 页面内容区域 -->
    <div class="page-content">
      <div v-if="currentPageType === 'benti'">
        <!-- 本体数据内容 -->
        <ext-benti :showData="showData" :treeData="treeData" />
      </div>

      <div v-else-if="currentPageType === 'concept'">
        <!-- 概念模型数据内容 -->
        <ext-knowledge-schema :showData="treeData" />
      </div>

      <div v-else-if="currentPageType === 'relation'">
        <!-- 关系模型内容 -->
        <ext-schema-relation />
      </div>
    </div>
  </div>
</template>

<script setup name="Schema">
import { onMounted, ref } from "vue";
import ExtKnowledgeSchema from "@/views/graph/ontology/ontologyDetail/extKnowledgeSchema/index.vue";
import ExtSchemaRelation from "@/views/graph/ontology/ontologyDetail/extSchemaRelation/index.vue";
import ExtBenti from "@/views/graph/ontology/ontologyDetail/extBenti/index.vue";
import useKnowledgeStore from "@/store/modules/knowledge.js";
import { useRoute } from "vue-router";
import {
  getExtSchemaTree,
  getExtSchemaGraph,
} from "@/api/graph/ontology/schema.js";
import { onMounted, ref } from "vue";
const route = useRoute();
// 当前选中的页面类型
const currentPageType = ref("benti"); // 默认为实体数据
const showData = ref([]);
const treeData = ref([]);
const basicChnnel = new BroadcastChannel("basic");
const knowledgeStore = useKnowledgeStore();
const knowledgeId = computed(() => knowledgeStore.currentKnowledge?.id || null);

// 页面类型映射
const pageTypes = {
  entity: "实体数据",
  concept: "概念模型",
  relation: "关系模型",
};



// 切换页面类型
function switchPageType(type) {
  currentPageType.value = type;
  console.log(currentPageType.value);
  // 这里可以添加加载对应数据的逻辑
  loadPageData(type);
}

function getConceptData(id) {
  const data = { knowledgeId: id };
  getExtSchemaTree(data)
    .then((res) => {
      treeData.value = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
}
function getBenTiData(id) {
  const data = { knowledgeId: id };
  getExtSchemaGraph(data)
    .then((res) => {
      showData.value = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
}
basicChnnel.addEventListener("message", (data) => {
  if (data.data) {
    getConceptData(knowledgeId.value);
  }
});

function loadPageData(type) {
  // 根据不同的页面类型加载相应的数据
  switch (type) {
    case "benti":
      if (!showData.value.length) {
        getBenTiData(knowledgeId.value);
      }
      if (!treeData.value.length) {
        getConceptData(knowledgeId.value);
      }
      break;
    case "entity":
      // 加载实体数据
      break;
    case "concept":
      if (!treeData.value.length) {
        getConceptData(knowledgeId.value);
      }
      break;
    case "relation":
      // 加载关系模型数据
      break;
  }
}

onMounted(() => {
  loadPageData(currentPageType.value);
});
</script>
<style scoped>
.app-container {
  height: 100%;
}
.color-box {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 25px;
  height: 25px;
  margin: 0 auto; /* Center horizontally */
  border-radius: 50%; /* 使其成为圆形 */
}
::v-deep .el-color-picker .el-color-picker__trigger {
  width: 30px; /* 设置宽度 */
  height: 30px; /* 设置高度 */
  border-radius: 50%; /* 设置为圆形 */
  overflow: hidden; /* 确保内容不会超出圆形 */
}
::v-deep .el-color-picker__color {
  border-radius: 50%; /* 设置为圆形 */
  overflow: hidden; /* 确保内容不会超出圆形 */
}
.pagecont-top {
  display: block !important;
}
.page-content {
  height: 100%;
}
</style>
