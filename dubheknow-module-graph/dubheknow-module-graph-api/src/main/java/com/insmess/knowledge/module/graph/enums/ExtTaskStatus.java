package com.insmess.knowledge.module.graph.enums;

public enum ExtTaskStatus {
    UNEXECUTED(0),//未执行
    INPROGRESS(1),//执行中
    EXECUTED(2),//已执行
    ERROR(3),//执行错误
    CHONG(4);// 文档状态未更新，需要重新执行

    private final Integer status;

    ExtTaskStatus(Integer status) {
        this.status = status;
    }

    public Integer getValue() {
        return this.status;
    }
}