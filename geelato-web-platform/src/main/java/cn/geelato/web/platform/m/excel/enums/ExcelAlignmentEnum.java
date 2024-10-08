package cn.geelato.web.platform.m.excel.enums;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * @author diabl
 */
@Getter
public enum ExcelAlignmentEnum {
    LEFT(HorizontalAlignment.LEFT, "left"),
    CENTER(HorizontalAlignment.CENTER, "center"),
    RIGHT(HorizontalAlignment.RIGHT, "right");

    private final HorizontalAlignment label;// 选项内容
    private final String value;// 选项值

    ExcelAlignmentEnum(HorizontalAlignment label, String value) {
        this.label = label;
        this.value = value;
    }

    public static HorizontalAlignment getLabel(String value) {
        if (Strings.isNotBlank(value)) {
            for (ExcelAlignmentEnum enums : ExcelAlignmentEnum.values()) {
                if (enums.getValue().equalsIgnoreCase(value)) {
                    return enums.getLabel();
                }
            }
        }
        return ExcelAlignmentEnum.CENTER.getLabel();
    }
}