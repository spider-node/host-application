package cn.spider.framework.code.agent.util;

public class FieldInfo {
    private final String fieldName;
    private final String fieldType;
    private final String comment;

    public FieldInfo(String fieldName, String fieldType, String comment) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.comment = comment;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Field: " + fieldName + ", Type: " + fieldType + ", Comment: " + comment;
    }
}
