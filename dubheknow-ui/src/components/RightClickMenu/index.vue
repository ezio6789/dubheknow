<template>
  <div class="container" ref="containerRef">
    <slot></slot>
    <teleport to="body">
      <transition name="fade">
        <ul
          v-if="visible"
          class="menu-container"
          :style="{ top: `${position.y}px`, left: `${position.x}px` }"
          ref="menuRef"
        >
          <li
            v-for="(item, index) in menuList"
            :key="index"
            class="menu-item"
            @click="handleClick(item)"
          >
            {{ item.label }}
          </li>
        </ul>
      </transition>
    </teleport>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount, nextTick } from "vue";

const props = defineProps({
  /** 菜单列表 */
  menuList: {
    type: Array,
    default: () => [],
  },
  /** 菜单应出现的位置（节点中心点） */
  pos: {
    type: Object,
    default: () => ({ x: 0, y: 0 }),
  },
});

const emits = defineEmits(["select"]);

const visible = ref(false);
const position = ref({ x: 0, y: 0 });
const menuRef = ref(null);
const containerRef = ref(null);

/**
 * 监听位置变化，显示菜单并自动调整位置
 */
watch(
  () => props.pos,
  async (newVal) => {
    if (!newVal) return;
    visible.value = true;
    position.value = { ...newVal };
    await nextTick();
    adjustMenuPosition();
  },
  { deep: true }
);

/**
 * 点击菜单项
 */
const handleClick = (item) => {
  emits("select", item);
  visible.value = false;
};

/**
 * 点击菜单外区域关闭菜单
 */
const handleClickOutside = (e) => {
  if (menuRef.value && !menuRef.value.contains(e.target)) {
    visible.value = false;
  }
};

/**
 * 自动调整菜单位置，确保不会超出屏幕边界
 * 且让右上角与节点中心对齐
 */
const adjustMenuPosition = () => {
  const menu = menuRef.value;
  if (!menu) return;

  const { innerWidth, innerHeight } = window;
  const rect = menu.getBoundingClientRect();

  // 默认让菜单的右上角与点击点对齐
  let x = position.value.x - rect.width;
  let y = position.value.y - rect.height / 2;

  // 边界防溢出处理
  if (x < 0) x = 5;
  if (y < 0) y = 5;
  if (x + rect.width > innerWidth) x = innerWidth - rect.width - 5;
  if (y + rect.height > innerHeight) y = innerHeight - rect.height - 5;

  position.value = { x, y };
};

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<style scoped lang="scss">
.container {
  width: 100%;
  height: 100%;
}

.menu-container {
  position: fixed;
  z-index: 9999;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 0 6px 6px 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  padding: 6px 0;
  min-width: 140px;
  font-size: 14px;
  user-select: none;
  overflow: hidden;
  transform: translate(100%, 40%);
}

.menu-item {
  padding: 8px 14px;
  cursor: pointer;
  transition: background 0.2s;
  list-style: none;
  &:hover {
    background: #f5f5f5;
  }
}

/* 简单淡入淡出动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
