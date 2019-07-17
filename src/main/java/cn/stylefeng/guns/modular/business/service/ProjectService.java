package cn.stylefeng.guns.modular.business.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.business.entity.Project;
import cn.stylefeng.guns.modular.business.mapper.ProjectMapper;
import cn.stylefeng.guns.modular.business.model.ProjectProvinceDto;
import cn.stylefeng.guns.modular.business.model.ProjectSimpleDto;
import cn.stylefeng.guns.modular.system.entity.Notice;
import cn.stylefeng.guns.modular.system.mapper.NoticeMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class ProjectService extends ServiceImpl<ProjectMapper, Project> {

    /**
     * 获取项目列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> list(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition);
    }

    public List<ProjectSimpleDto> allProject() {
        List<ProjectProvinceDto> projectProvinceDtos = this.baseMapper.allProject();
        return projectProvinceDtos.stream().map(projectProvinceDto -> {
            String parentName = projectProvinceDto.getParentName();
            parentName = StringUtils.isBlank(parentName) ? StringUtils.EMPTY : parentName;
            String title = Joiner.on(StringUtils.EMPTY).join(parentName, projectProvinceDto.getFullName(), projectProvinceDto.getTitle());
            return new ProjectSimpleDto(projectProvinceDto.getProjectId(), title);
        }).collect(Collectors.toList());
    }
}
