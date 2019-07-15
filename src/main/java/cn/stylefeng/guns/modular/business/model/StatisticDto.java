package cn.stylefeng.guns.modular.business.model;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 统计信息表
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-01
 */
@Data
public class StatisticDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long statisticId;

    /**
     * 地市ID
     */
    private Long provinceId;

    /**
     * 地市名称
     */
    private String provinceName;

    /**
     * 主单数
     */
    private Long mainNum;

    /**
     * 明细数
     */
    private Long detailNum;

    /**
     * 有出院诊断数
     */
    private Long hasDischargeNum;

    /**
     * 就医方式 门诊数量
     */
    private Long outPatientNum;

    /**
     * 就医方式 住院数量
     */
    private Long hospitalNum;

    /**
     * 就医方式
     */
    private String medicalTreatmentName;

    /**
     * 开始日期
     */
    private String beginDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 是否连续(字典) CONTINUATION 连续 不连续
     */
    private Long continuationId;

    /**
     * 是否连续(字典)
     */
    private String continuationName;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人
     */
    private String createUserName;
}
