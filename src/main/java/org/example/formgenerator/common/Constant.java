package org.example.formgenerator.common;

/**
 * 系统常量
 */
public class Constant {
    /**
     * 布局类型：1-纵向 2-横向
     */
    public static final Byte LAYOUT_TYPE_VERTICAL = 1;
    public static final Byte LAYOUT_TYPE_HORIZONTAL = 2;

    /**
     * 字段类型
     */
    public static final String FIELD_TYPE_TEXT = "text";
    public static final String FIELD_TYPE_NUMBER = "number";
    public static final String FIELD_TYPE_DATE = "date";

    /**
     * 逻辑删除：0-未删 1-已删
     */
    public static final Byte IS_DELETED_NO = 0;
    public static final Byte IS_DELETED_YES = 1;
}