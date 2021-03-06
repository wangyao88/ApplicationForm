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
public class ProjectDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long projectId;
    /**
     * 名称
     */
    private String title;
    /**
     * 省市主键
     */
    private Long provinceId;
    /**
     * 省市名称
     */
    private String provinceName;
}
