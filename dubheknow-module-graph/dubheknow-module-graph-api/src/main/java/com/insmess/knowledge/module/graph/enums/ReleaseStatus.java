package com.insmess.knowledge.module.graph.enums;

/**
 * 发布状态
 */
public enum ReleaseStatus {
    UNPUBLISHED(0),//未发布
    PUBLISHED(1);//已发布

    private final Integer status;

    ReleaseStatus(Integer status) {
        this.status = status;
    }

    public Integer getValue() {
        return this.status;
    }
}
