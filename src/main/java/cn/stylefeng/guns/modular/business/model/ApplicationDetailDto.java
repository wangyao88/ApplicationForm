package cn.stylefeng.guns.modular.business.model;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 统计信息明细表
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-01
 */
@Data
public class ApplicationDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long applicationDetailId;

    /**
     * 统计信息ID
     */
    private Long statisticId;

    /**
     * 数量
     */
    private Long num;

    /**
     * 日期
     */
    private String detailDate;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人
     */
    private String createUserName;
}
