<template>
  <div class="form-container">
    <el-form :mode="formData">
      <div class="form-item1">
        <div class="form-item1-1">
          <el-form-item label="概念中文名称:" required>
            <el-input
              placeholder="请输入实体名称"
              v-model="formData.name"
            ></el-input>
          </el-form-item>
          <!-- <el-form-item label="概念英文名称:" required>
            <el-input
              placeholder="请输入实体名称"
              v-model="formData.enName"
            ></el-input>
          </el-form-item> -->
        </div>
        <el-form-item label="概念描述:">
          <el-input
            type="textarea"
            resize="none"
            maxlength="400"
            show-word-limit
            placeholder="请输入概念描述信息..."
            v-model="formData.description"
          ></el-input>
        </el-form-item>
      </div>
      <div class="form-item2">
        <!-- <div class="form-item2-1">
          <h3>图标选择</h3>
          <el-form-item label="时空属性" required>
            <el-input
              placeholder="请输入"
              v-model="formData.spatAttr"
            ></el-input>
          </el-form-item>
        </div> -->
        <div class="form-item2-2">
          <h3>
            图标选择
            <span style="font-size: 12px">无时空属性关联</span>
          </h3>
          <div class="form-item2-2-color">
            <el-color-picker
              show-alpha
              v-model="formData.color"
              :predefine="predefineColors"
            />
            <span
              v-for="item in predefineColors"
              :style="{ background: item }"
              :key="item"
              :class="hexToRgba(item) === formData.color ? 'active' : ''"
              @click="() => (formData.color = hexToRgba(item))"
            ></span>
          </div>
        </div>
        <!-- <div class="form-item2-3">
          <h3>ICON图</h3>
          <el-upload
            v-model:file-list="fileList"
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-change="handleChange"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <el-dialog v-model="dialogVisible">
            <img w-full :src="dialogImageUrl" alt="Preview Image" />
          </el-dialog>
        </div> -->
      </div>
      <el-form-item>
        <el-button type="primary" @click="submit">保存</el-button>
        <el-button type="info" v-if="operButton === 'add'" @click="cancel"
          >取消</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup name="Schema">
import { ref } from "vue";
import { Plus } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { updateSchema, addSchema } from "@/api/graph/ontology/schema.js";
import { emptyForm } from "../data";
import { useKnowledgeContext } from "@/hooks/useKnowledgeContext";
const { knowledgeId } = useKnowledgeContext();

const formData = ref(emptyForm);
const operButton = ref("update");
const predefineColors = ref([
  "#ff4500",
  "#ff8c00",
  "#ffd700",
  "#90ee90",
  "#00ced1",
  "#1e90ff",
  "#c71585",
  "rgba(255, 69, 0, 0.68)",
  "#c7158577",
]);
const props = defineProps({
  nodeData: {
    type: Object,
    default: () => ({}),
  },
  operation: {
    type: String,
    default: "",
  },
  selectNode: {
    type: Object,
    default: () => ({}),
  },
});
watch(
  () => props.nodeData,
  (newVal) => {
    if (JSON.stringify(newVal) !== JSON.stringify(formData.value)) {
      formData.value = newVal
        ? JSON.parse(JSON.stringify(newVal))
        : JSON.parse(JSON.stringify(emptyForm));
      operButton.value = props.operation;
    }
  },
  { deep: true, immediate: true }
);

const basicChnnel = new BroadcastChannel("basic");
// const dialogImageUrl = ref("");
// const dialogVisible = ref(false);
// // 删除图片
// const handleRemove = (uploadFile, uploadFiles) => {
//   formData.iconFile = null;
// };
// // 展示图片
// const handlePictureCardPreview = (uploadFile) => {
//   dialogImageUrl.value = uploadFile.url;
//   dialogVisible.value = true;
// };
// const handleChange = (file) => {
//   formData.iconFile = file.raw;
// };
function updateData(data) {
  updateSchema(data)
    .then((res) => {
      basicChnnel.postMessage(true);
      ElMessage.success(res.msg);
    })
    .catch((err) => {
      console.log(err);
    });
}
function addData(data) {
  data.id = undefined;
  console.log(data);
  addSchema(data)
    .then((res) => {
      ElMessage.success(res.msg);
      basicChnnel.postMessage(true);
    })
    .catch((err) => {
      console.log(err);
    });
}
function submit() {
  formData.value.knowledgeId = knowledgeId.value;
  switch (props.operation) {
    case "update":
      updateData(formData.value);
      break;
    case "add":
      addData(formData.value);
      break;
  }
}
function cancel() {
  basicChnnel.postMessage(true);
  formData.value = emptyForm;
  operButton.value = "update";
}
function hexToRgba(hex) {
  if (typeof hex !== "string" || !hex.startsWith("#")) {
    return hex;
  }
  let color = hex.replace("#", "");
  if (color.length === 3) {
    color = color
      .split("")
      .map((c) => c + c)
      .join("");
  }
  if (![6, 8].includes(color.length)) {
    throw new Error("无效的十六进制颜色格式");
  }
  const r = parseInt(color.slice(0, 2), 16);
  const g = parseInt(color.slice(2, 4), 16);
  const b = parseInt(color.slice(4, 6), 16);
  let a;
  if (color.length === 8) {
    a = Number((parseInt(color.slice(6, 8), 16) / 255).toFixed(1));
  } else {
    a = 1;
  }
  const rgba = `rgba(${r}, ${g}, ${b}, ${a})`;
  return rgba;
}
</script>

<style scoped lang="less">
.form-container {
  padding: 30px 50px;
  .form-item1 {
    &-1 {
      display: flex;
      & > * {
        margin-right: 10px;
      }
    }
    .el-input {
      width: 300px;
    }
    & > * {
      margin-right: 10px;
      margin-bottom: 30px;
    }
  }
  .form-item2 {
    h3 {
      color: rgb(12, 129, 245);
    }
    span {
      color: black;
    }
    .el-input {
      width: 200px;
    }
    &-2 {
      &-color {
        display: flex;
        align-items: center;
        span {
          margin-left: 10px;
          display: inline-block;
          position: relative;
          border-radius: 50%;
          box-sizing: border-box;
          width: 18px;
          height: 18px;
          z-index: 1;
          transition: border-color 0.25s cubic-bezier(0.71, -0.46, 0.29, 1.46),
            background-color 0.25s cubic-bezier(0.71, -0.46, 0.29, 1.46),
            outline 0.25s cubic-bezier(0.71, -0.46, 0.29, 1.46);
        }
        span.active::after {
          box-sizing: content-box;
          content: "";
          border: 2px solid white;
          border-left: 0;
          border-top: 0;
          height: 7px;
          left: 50%;
          position: absolute;
          top: 50%;
          transform: translate(-50%, -50%) rotate(45deg);
          width: 3px;
          transition: transform 0.15s ease-in 0.05s;
          transform-origin: center;
        }
      }
    }
  }
}
.el-form {
  & > * {
    margin-top: 30px;
  }
}
</style>
<style>
.el-textarea {
  width: 50%;
}
.el-tabs {
  .el-tabs__header {
    margin: 0;
  }
}
</style>
