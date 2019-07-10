package cn.stylefeng.guns.modular.business.mapper;

import cn.stylefeng.guns.modular.business.entity.ApplicationForm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 申请单表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public interface ApplicationFormMapper extends BaseMapper<ApplicationForm> {

    /**
     * 获取项目列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition);

}
