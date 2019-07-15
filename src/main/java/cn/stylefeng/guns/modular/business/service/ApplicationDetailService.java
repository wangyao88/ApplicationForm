package cn.stylefeng.guns.modular.business.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.business.entity.ApplicationDetail;
import cn.stylefeng.guns.modular.business.mapper.ApplicationDetailMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 统计信息明细表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class ApplicationDetailService extends ServiceImpl<ApplicationDetailMapper, ApplicationDetail> {

    /**
     * 获取统计信息明细列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> list(Long statisticId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, statisticId);
    }
}
