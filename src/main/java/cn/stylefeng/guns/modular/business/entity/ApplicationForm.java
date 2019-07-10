package cn.stylefeng.guns.modular.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 申请单表
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-01
 */
@Data
@TableName("business_application_form")
public class ApplicationForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "application_form_id", type = IdType.ID_WORKER)
    private Long applicationFormId;

    /**
     * 所属项目主键
     */
    @TableId(value = "project_id")
    private Long projectId;

    /**
     * 数据内容描述
     */
    @TableField("description")
    private String description;

    /**
     * 用途
     */
    @TableField("use")
    private String use;

    /**
     * 申请单类型(字典) 出库 入库
     */
    @TableField("application_form_type_id")
    private Long applicationFormTypeId;

    /**
     * 申请人
     */
    @TableField(value = "application_user", fill = FieldFill.INSERT)
    private Long applicationUser;

    /**
     * 申请时间
     */
    @TableField(value = "application_time", fill = FieldFill.INSERT)
    private Date applicationTime;

    /**
     * 接收人
     */
    @TableField(value = "receive_user", fill = FieldFill.INSERT)
    private Long receiveUser;

    /**
     * 接收时间
     */
    @TableField(value = "receive_time", fill = FieldFill.INSERT)
    private Date receiveTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;
}
