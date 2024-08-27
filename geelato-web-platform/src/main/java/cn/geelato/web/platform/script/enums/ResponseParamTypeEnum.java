package cn.geelato.web.platform.script.enums;

import org.apache.logging.log4j.util.Strings;

/**
 * @author diabl
 */
public enum ResponseParamTypeEnum {
    STRING("字符串", "string"),
    NUMBER("数值", "number"),
    BOOLEAN("布尔值", "boolean"),
    OBJECT("对象", "object"),
    ARRAY("数组", "array");

    private final String label;// 选项内容
    private final String value;// 选项值

    ResponseParamTypeEnum(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public static String getLabel(String value) {
        if (Strings.isNotBlank(value)) {
            for (ResponseParamTypeEnum enums : ResponseParamTypeEnum.values()) {
                if (enums.getValue().equals(value)) {
                    return enums.getLabel();
                }
            }
        }
        return null;
    }
}