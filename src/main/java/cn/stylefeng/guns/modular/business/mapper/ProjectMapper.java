package cn.stylefeng.guns.modular.business.mapper;

import cn.stylefeng.guns.modular.business.entity.Project;
import cn.stylefeng.guns.modular.business.model.ProjectSimpleDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 获取项目列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition);

    List<ProjectSimpleDto> allProject();
}
