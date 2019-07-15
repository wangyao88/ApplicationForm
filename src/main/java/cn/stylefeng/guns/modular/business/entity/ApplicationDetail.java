package cn.stylefeng.guns.modular.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 统计信息明细表
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-01
 */
@Data
@TableName("business_application_detail")
public class ApplicationDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "application_detail_id", type = IdType.ID_WORKER)
    private Long applicationDetailId;

    /**
     * 统计信息ID
     */
    @TableField("statistic_id")
    private Long statisticId;

    /**
     * 数量
     */
    @TableField("num")
    private Long num;

    /**
     * 日期
     */
    @TableField("detail_date")
    private String detailDate;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;
}
