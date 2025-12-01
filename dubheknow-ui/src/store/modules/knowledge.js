import { defineStore } from "pinia";

const STORAGE_KEY = "current_knowledge";

export default defineStore("knowledge", {
  state: () => ({
    currentKnowledge: (() => {
      return {
        id: 100,
      }
    })(),
  }),
  // state: () => ({
  //   currentKnowledge: (() => {
  //     const saved = sessionStorage.getItem(STORAGE_KEY);
  //     if (!saved) return null;
  //     try {
  //       return JSON.parse(saved);
  //     } catch (e) {
  //       sessionStorage.removeItem(STORAGE_KEY);
  //       return null;
  //     }
  //   })(),
  // }),
  getters: {
    hasKnowledge: (state) =>
      !!(state.currentKnowledge && state.currentKnowledge.id),
  },
  actions: {
    setKnowledge(knowledge) {
      this.currentKnowledge = knowledge;
      sessionStorage.setItem(STORAGE_KEY, JSON.stringify(knowledge || null));
    },
    clearKnowledge() {
      this.setKnowledge(null);
    },
  },
});
