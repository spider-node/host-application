package cn.spider.framework.code.agent.areabase.modules.domaininfo.entity;

public class DomainFieldInfo {
    /**
     * 字段名称
     */
    private String field;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段描述
     */
    private String fieldDesc;

    /**
     * 表结构中的字段名称
     */
    private String tableField;

    public DomainFieldInfo(String field, String type, String fieldDesc, String tableField) {
        this.field = field;
        this.type = type;
        this.fieldDesc = fieldDesc;
        this.tableField = tableField;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getTableField() {
        return tableField;
    }

    public void setTableField(String tableField) {
        this.tableField = tableField;
    }
}
