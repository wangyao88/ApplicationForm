package cn.stylefeng.guns.modular.business.mapper;

import cn.stylefeng.guns.modular.business.entity.Statistic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 统计信息表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public interface StatisticMapper extends BaseMapper<Statistic> {

    /**
     * 获取项目列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition);
}
