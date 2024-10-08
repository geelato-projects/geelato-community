package cn.geelato.core.enums;

import lombok.Getter;

/**
 * @author diabl
 */
@Getter
public enum DeleteStatusEnum {
    IS(1, "已删除"),
    NO(0, "未删除");

    private final int code;
    private final String name;

    DeleteStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
