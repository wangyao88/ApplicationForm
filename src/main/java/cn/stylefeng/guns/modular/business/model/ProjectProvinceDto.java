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
public class ProjectProvinceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目主键
     */
    private Long projectId;

    /**
     * 名称
     */
    private String title;

    /**
     * 父名称
     */
    private String parentName;

    /**
     * 全称
     */
    private String fullName;
}
