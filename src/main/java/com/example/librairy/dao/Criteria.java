package com.example.librairy.dao;

public class Criteria {
    private String field;
    private Object value;
    private String orderBy;
    private boolean isAscending;

    public Criteria(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public Criteria(String field, Object value, String orderBy, boolean isAscending) {
        this.field = field;
        this.value = value;
        this.orderBy = orderBy;
        this.isAscending = isAscending;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isAscending() {
        return isAscending;
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "field='" + field + '\'' +
                ", value=" + value +
                ", orderBy=" + orderBy +
                ", isAscending=" + isAscending +
                '}';
    }
}
