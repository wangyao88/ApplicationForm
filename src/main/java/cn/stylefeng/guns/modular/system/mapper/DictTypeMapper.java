package cn.stylefeng.guns.modular.system.mapper;

import cn.stylefeng.guns.modular.system.entity.DictType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 字典类型表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-13
 */
public interface DictTypeMapper extends BaseMapper<DictType> {

    Long getIdByCode(@Param("code") String typeCode);
}
