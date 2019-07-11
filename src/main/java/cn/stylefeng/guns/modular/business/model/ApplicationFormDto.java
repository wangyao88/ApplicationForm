package cn.stylefeng.guns.modular.business.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
public class ApplicationFormDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long applicationFormId;

    /**
     * 所属项目主键
     */
    private Long projectId;

    /**
     * 所属项目名称
     */
    private String projectTitle;

    /**
     * 数据内容描述
     */
    private String description;

    /**
     * 用途
     */
    private String use;

    /**
     * 申请单类型(字典) 出库 入库
     */
    private Long applicationFormTypeId;

    /**
     * 申请单类型(字典) 出库 入库
     */
    private String applicationFormTypeName;

    /**
     * 申请人
     */
    private Long applicationUser;

    /**
     * 申请人
     */
    private String applicationUserName;

    /**
     * 申请人部门
     */
    private String applicationUserDeptName;

    /**
     * 申请人邮箱
     */
    private String applicationUserEmail;

    /**
     * 申请人电话
     */
    private String applicationUserPhone;

    /**
     * 申请时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date applicationTime;

    /**
     * 接收人
     */
    private Long receiveUser;

    /**
     * 接收人
     */
    private String receiveUserName;

    /**
     * 接收时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人
     */
    private String createUserName;
}
