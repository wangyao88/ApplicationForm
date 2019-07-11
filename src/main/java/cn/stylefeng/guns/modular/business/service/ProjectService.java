package cn.stylefeng.guns.modular.business.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.business.entity.Project;
import cn.stylefeng.guns.modular.business.mapper.ProjectMapper;
import cn.stylefeng.guns.modular.business.model.ProjectSimpleDto;
import cn.stylefeng.guns.modular.system.entity.Notice;
import cn.stylefeng.guns.modular.system.mapper.NoticeMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        return this.baseMapper.allProject();

    }
}
