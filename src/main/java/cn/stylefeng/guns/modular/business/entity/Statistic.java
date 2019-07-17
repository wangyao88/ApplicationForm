package cn.stylefeng.guns.modular.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 统计信息表
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-01
 */
@Data
@TableName("business_statistic")
public class Statistic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "statistic_id", type = IdType.ID_WORKER)
    private Long statisticId;

    /**
     * 申请单ID
     */
    @TableField("applicationForm_Id")
    private Long applicationFormId;

    /**
     * 地市ID
     */
    @TableField("province_Id")
    private Long provinceId;

    /**
     * 主单数
     */
    @TableField("main_num")
    private Long mainNum;

    /**
     * 明细数
     */
    @TableField("detail_num")
    private Long detailNum;

    /**
     * 有出院诊断数
     */
    @TableField("has_discharge_num")
    private Long hasDischargeNum;

    /**
     * 就医方式 数量
     */
    @TableField("medical_treatment")
    private String medicalTreatment;

    /**
     * 开始日期
     */
    @TableField("begin_date")
    private String beginDate;

    /**
     * 结束日期
     */
    @TableField("end_date")
    private String endDate;

    /**
     * 是否连续(字典) CONTINUATION 连续 不连续
     */
    @TableField("continuation")
    private String continuation;

    @TableField(exist = false)
    private List<ApplicationDetail> applicationDetails = Lists.newArrayList();

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

    public void addApplicationDetail(ApplicationDetail applicationDetail) {
        this.getApplicationDetails().add(applicationDetail);
    }
}
