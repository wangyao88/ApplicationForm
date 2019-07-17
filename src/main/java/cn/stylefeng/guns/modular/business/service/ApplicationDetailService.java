package cn.stylefeng.guns.modular.business.service;

import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.business.entity.ApplicationDetail;
import cn.stylefeng.guns.modular.business.mapper.ApplicationDetailMapper;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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

    @Autowired
    private StatisticService statisticService;

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

    @Transactional(rollbackFor = ServiceException.class)
    public void batchSave(ApplicationDetail applicationDetail) {
        String content = applicationDetail.getContent();
        String[] dateNumArr = content.split("\n");
        for (String dateNum : dateNumArr) {
            String[] split = dateNum.split(":");
            String date = split[0].trim();
            if(!statisticService.isDate(date)) {
                throw new ServiceException(403, date+"为非法日期格式（正确格式为xxxx-xx）");
            }
            applicationDetail.setDetailDate(date);
            String num = split[1].trim();
            try {
                applicationDetail.setNum(Long.valueOf(num));
            } catch (NumberFormatException e) {
                throw new ServiceException(403, num+"为非法数字格式");
            }
            applicationDetail.setApplicationDetailId(IdWorker.getId());
            this.save(applicationDetail);
        }
    }
}
