<template>
  <div class="info-container">
    <div v-if="hasData" class="info-card">
      <el-descriptions :column="1" border label-class-name="desc-label" class="desc-card">
        <el-descriptions-item label="概念名称">
          <span>{{ formData.name || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="父级概念">
          <span>{{ formData.parentName || formData.pid || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="概念描述">
          <span>{{ formData.description || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="概念颜色">
          <div class="color-row">
            <span class="color-chip" :style="{ background: formData.color || '#dcdfe6' }"></span>
            <span>{{ formData.color || '-' }}</span>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="创建人">
          <span>{{ formData.createBy || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          <span>{{ formData.createTime || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="修改人">
          <span>{{ formData.updateBy || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="修改时间">
          <span>{{ formData.updateTime || '-' }}</span>
        </el-descriptions-item>
      </el-descriptions>
    </div>
    <el-empty v-else description="请选择左侧概念查看详情" />
  </div>
</template>

<script setup name="Schema">
import { ref, watch, computed } from "vue";
import { emptyForm } from "../data";

const formData = ref(emptyForm);

const props = defineProps({
  nodeData: {
    type: Object,
    default: () => ({}),
  },
});

const hasData = computed(() => !!(props.nodeData && props.nodeData.id));

watch(
  () => props.nodeData,
  (newVal) => {
    formData.value = newVal ? JSON.parse(JSON.stringify(newVal)) : JSON.parse(JSON.stringify(emptyForm));
  },
  { deep: true, immediate: true }
);
</script>

<style scoped lang="less">
.info-container {
  padding: 10px 0;
}
.info-card {
  background: transparent;
  padding: 0;
}
.desc-card {
  width: 100%;
}
.desc-label {
  width: 120px;
  font-weight: 600;
  background: #f7f9fb;
}
.color-row {
  display: flex;
  align-items: center;
  gap: 8px;
}
.color-chip {
  width: 18px;
  height: 18px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}
</style>
