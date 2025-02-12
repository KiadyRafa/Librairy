package com.example.librairy.dao;

public class Criteria {
    private String field;
    private Object value;

    public Criteria(String field, Object value) {
        this.field = field;
        this.value = value;
    }


    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "field='" + field + '\'' +
                ", value=" + value +
                '}';
    }
}