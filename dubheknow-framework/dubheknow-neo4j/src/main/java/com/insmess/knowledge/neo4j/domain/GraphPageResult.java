package com.insmess.knowledge.neo4j.domain;

import java.util.List;
import java.util.Map;

public class GraphPageResult {
    private List<Map<String, Object>> nodes;
    private List<Map<String, Object>> relationships;
    private int page;
    private int size;
    private long total;

    public GraphPageResult(List<Map<String, Object>> nodes,
                           List<Map<String, Object>> relationships,
                           int page, int size, long total) {
        this.nodes = nodes;
        this.relationships = relationships;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public List<Map<String, Object>> getNodes() { return nodes; }
    public List<Map<String, Object>> getRelationships() { return relationships; }
    public long getTotal() { return total; }
    public int getPage() { return page; }
    public int getSize() { return size; }
}
