package cn.stylefeng.guns.modular.business.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.business.entity.Statistic;
import cn.stylefeng.guns.modular.business.mapper.StatisticMapper;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Cleanup;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 统计信息表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class StatisticService extends ServiceImpl<StatisticMapper, Statistic> {

    /**
     * 获取统计信息列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> list(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition);
    }

    public void importExcel(String fileName, MultipartFile excel) throws IOException {
        @Cleanup
        InputStream inputStream = excel.getInputStream();
        //读取excel
        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 0));
    }
}
