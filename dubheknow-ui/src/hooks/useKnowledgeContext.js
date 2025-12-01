import { computed } from "vue";
import useKnowledgeStore from "@/store/modules/knowledge";

/**
 * Shared helper to access knowledge store and the current knowledge id.
 * Use this in components instead of duplicating the store/computed wiring.
 */
export function useKnowledgeContext() {
  const knowledgeStore = useKnowledgeStore();
  const knowledgeId = computed(() => knowledgeStore.currentKnowledge?.id || null);

  return {
    knowledgeStore,
    knowledgeId,
  };
}
