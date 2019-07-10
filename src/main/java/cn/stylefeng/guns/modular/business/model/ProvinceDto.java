package cn.stylefeng.guns.modular.business.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 字典信息
 *
 * @author fengshuonan
 * @Date 2018/12/8 18:16
 */
@Data
public class ProvinceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long provinceId;
    /**
     * 父id
     */
    private Long pid;
    /**
     * 父名称
     */
    private String pName;
    /**
     * 简称
     */
    private String simpleName;
    /**
     * 全称
     */
    private String fullName;
    /**
     * 描述
     */
    private String description;
    /**
     * 排序
     */
    private Integer sort;
}
