package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 基础字典
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-01
 */
@Data
public class DictDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    private Long dictId;

    /**
     * 字典名称
     */
    @TableField("name")
    private String name;
}
