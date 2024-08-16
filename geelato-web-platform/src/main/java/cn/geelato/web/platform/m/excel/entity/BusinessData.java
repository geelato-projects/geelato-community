package cn.geelato.web.platform.m.excel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author diabl
 * @description: 业务数据
 * @date 2023/10/15 10:53
 */

@Getter
@Setter
public class BusinessData {
    private int XIndex;
    private int YIndex;
    private Object value;
    private Object primevalValue;
    private Set<Object> transitionValue = new LinkedHashSet<>();
    private String[] multiValue;
    private BusinessTypeData businessTypeData;
    private Set<String> errorMsg = new LinkedHashSet<>();

    public Set<String> getTransitionValueString() {
        Set<String> stringSet = new LinkedHashSet<>();
        if (this.transitionValue != null && !this.transitionValue.isEmpty()) {
            for (Object obj : this.transitionValue) {
                stringSet.add(String.valueOf(obj));
            }
        }

        return stringSet;
    }

    public void setTransitionValue(Object transitionValue) {
        if (this.transitionValue == null) {
            this.transitionValue = new LinkedHashSet<>();
        }
        this.transitionValue.add(transitionValue);
    }

    public void setTransitionValues(Set<Object> transitionValues) {
        if (this.transitionValue == null) {
            this.transitionValue = new LinkedHashSet<>();
        }
        this.transitionValue.addAll(transitionValues);
    }

    public void addErrorMsg(String errorMsg) {
        if (this.errorMsg == null) {
            this.errorMsg = new LinkedHashSet<>();
        }
        this.errorMsg.add(errorMsg);
    }

    public void addAllErrorMsgs(Set<String> errorMsg) {
        if (this.errorMsg == null) {
            this.errorMsg = new LinkedHashSet<>();
        }
        this.errorMsg.addAll(errorMsg);
    }

    public boolean isValidate() {
        return !(this.errorMsg != null && this.errorMsg.size() > 0);
    }
}
