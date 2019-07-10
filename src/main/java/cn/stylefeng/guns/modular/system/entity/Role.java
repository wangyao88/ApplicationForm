package cn.stylefeng.guns.modular.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-01
 */
@Data
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "role_id", type = IdType.ID_WORKER)
    private Long roleId;

    /**
     * 父角色id
     */
    @TableField("pid")
    private Long pid;

    /**
     * 角色名称
     */
    @TableField("name")
    private String name;

    /**
     * 提示
     */
    @TableField("description")
    private String description;

    /**
     * 序号
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 乐观锁
     */
    @TableField("version")
    private Integer version;

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
     * 创建用户
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改用户
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;
}
