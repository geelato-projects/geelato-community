package cn.geelato.core.meta.model.entity;

import cn.geelato.core.meta.annotation.Col;
import cn.geelato.core.meta.annotation.Title;
import lombok.Setter;

/**
 * 基础业务实体，增加了工作流信息
 */
@Setter
public class BizEntity extends BaseEntity {

    // 工作流实例id
    private String procId;
    // 工作流状态编码
    private String procStatus;


    @Col(name = "proc_id", nullable = true)
    @Title(title = "流程实例ID")
    public String getProcId() {
        return procId;
    }

    @Col(name = "proc_status", nullable = true)
    @Title(title = "流程状态")
    public String getProcStatus() {
        return procStatus;
    }

    /***
     * 其它属性设置之后，调用。可用于通用的增删改查功能中，特别字段的生成
     */
    @Override
    public void afterSet() {

    }
}
