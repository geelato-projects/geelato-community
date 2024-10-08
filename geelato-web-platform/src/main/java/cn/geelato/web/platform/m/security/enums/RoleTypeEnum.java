package cn.geelato.web.platform.m.security.enums;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

/**
 * @author diabl
 */
@Getter
public enum RoleTypeEnum {
    PLATFORM("平台级角色", "platform"), APP("应用级角色", "app");

    private final String label;//选项内容
    private final String value;//选项值

    RoleTypeEnum(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static String getLabel(String value) {
        if (Strings.isNotBlank(value)) {
            for (RoleTypeEnum enums : RoleTypeEnum.values()) {
                if (enums.getValue().equals(value)) {
                    return enums.getLabel();
                }
            }
        }
        return null;
    }
}