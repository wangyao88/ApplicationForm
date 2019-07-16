package cn.stylefeng.guns.modular.business.service;

import cn.afterturn.easypoi.view.EasypoiMapExcelView;
import cn.hutool.core.collection.CollectionUtil;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.core.util.DecimalUtil;
import cn.stylefeng.guns.modular.business.entity.ApplicationDetail;
import cn.stylefeng.guns.modular.business.entity.ApplicationDetailComparator;
import cn.stylefeng.guns.modular.business.entity.Statistic;
import cn.stylefeng.guns.modular.business.mapper.StatisticMapper;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import net.sf.jsqlparser.statement.select.Join;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private static final Pattern pattern = Pattern.compile("[1-9]{1}[0-9]{3}[-|\\/|年|\\.]{1}(0[1-9]|1[1-2])");
    @Autowired
    private ApplicationDetailService applicationDetailService;

    /**
     * 获取统计信息列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     * @param applicationFormId
     */
    public Page<Map<String, Object>> listAll(Long applicationFormId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.listAll(page, applicationFormId);
    }

    /**
     * 获取统计信息列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> listCondition(Long applicationFormId, String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.listCondition(page, applicationFormId, condition);
    }

    @Transactional(rollbackFor = ServiceException.class)
    public void importExcel(Long applicationFormId, String fileName, MultipartFile excel){
        List<Object> data = Lists.newArrayList();
        try {
            @Cleanup
            InputStream inputStream = excel.getInputStream();
            //读取excel
            data = EasyExcelFactory.read(inputStream, new Sheet(1, 0));
            if(data.size() < 3) {
                return;
            }
        } catch (IOException e) {
            throw new ServiceException(500, "获取上传文件流失败！");
        }
        List<Statistic> statistics = Lists.newArrayList();
        data.stream().skip(2).map(obj -> (List)obj).forEach(eles -> {
            if(!Objects.isNull(eles.get(0))) {
                Statistic currentStatistic = configureStatistic(applicationFormId, eles);
                statistics.add(currentStatistic);
            }
            Statistic currentStatistic = getCurrentStatistic(statistics);
            ApplicationDetail applicationDetail = configureDetail(currentStatistic, eles);
            currentStatistic.addApplicationDetail(applicationDetail);

        });
        statistics.forEach(statistic -> {
            CompletableFuture.runAsync(() -> {
                List<ApplicationDetail> applicationDetails = statistic.getApplicationDetails();
                List<String> details = applicationDetails.stream().filter(this::isDate).sorted(new ApplicationDetailComparator()).map(ApplicationDetail::getDetailDate).collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(details)) {
                    setStatisticValidDetail(statistic, details);
                }else {
                    setStatisticUnValidDetail(statistic, applicationDetails);
                }
            }).whenCompleteAsync((v, e) -> {
                if(!Objects.isNull(e)) {
                    throw new ServiceException(500, "解析excel失败！");
                }
                this.save(statistic);
                statistic.getApplicationDetails().forEach(applicationDetail -> applicationDetailService.save(applicationDetail));
            });
        });
    }

    private void setStatisticUnValidDetail(Statistic statistic, List<ApplicationDetail> applicationDetails) {
        if(CollectionUtil.isNotEmpty(applicationDetails)) {
            ApplicationDetail applicationDetail = applicationDetails.get(0);
            int size = applicationDetails.size();
            String begin = applicationDetail.getDetailDate();
            if(size == 1 && begin.length() > 7) {
                String[] split = begin.split(StringUtils.SPACE);
                String dateRange = split[0];
                String continuation = split[1].trim();
                statistic.setContinuationId(ConstantFactory.me().getDictByName(continuation));
                String[] ranges = dateRange.split("-");
                statistic.setBeginDate(convertDate(ranges[0]));
                statistic.setEndDate(convertDate(ranges[1]));
            }
        }
    }

    private void setStatisticValidDetail(Statistic statistic, List<String> details) {
        int filtedSize = details.size();
        String filtedBegin = details.get(0);
        String filtedEnd = details.get(filtedSize == 1 ? 0 : filtedSize-1);
        statistic.setBeginDate(filtedBegin);
        statistic.setEndDate(filtedEnd);
        statistic.setContinuationId(checkContinuation(filtedBegin, filtedEnd, details));
    }

    private String convertDate(String range) {
        range = range.replaceAll("年", "-").replaceAll("月", StringUtils.EMPTY);
        String[] split = range.split("-");
        String month = split[1];
        month = Integer.parseInt(month) < 10 ? "0"+month : month;
        return Joiner.on("-").join(split[0], month);
    }

    private Long checkContinuation(String begin, String end, List<String> details) {
        List<String> dateRange = getDateRange(begin, end);
        dateRange.removeAll(details);
        if(CollectionUtil.isEmpty(dateRange)) {
            return ConstantFactory.me().getDictByCode("YES_CONTINUATION");
        }
        return ConstantFactory.me().getDictByCode("NO_CONTINUATION");
    }

    private List<String> getDateRange(String begin, String end) {
        List<String> dateRange = Lists.newArrayList();
        int beginYear = getYear(begin);
        int beginMonth = getMonth(begin);
        int endYear = getYear(end);
        int endMonth = getMonth(end);
        while(beginYear+beginMonth < endYear+endMonth) {
            dateRange.add(Joiner.on("-").join(beginYear, beginMonth < 10 ? "0" + beginMonth : beginMonth));
            if(beginMonth < 12) {
                beginMonth++;
            } else {
                beginMonth = 1;
                beginYear++;
            }
        }
        dateRange.add(end);
        return dateRange;
    }

    private int getMonth(String date) {
        return Integer.parseInt(date.substring(5, 7));
    }

    private int getYear(String date) {
        return Integer.parseInt(date.substring(0, 4));
    }

    private boolean isDate(ApplicationDetail applicationDetail) {
        return pattern.matcher(applicationDetail.getDetailDate()).matches();
    }

    public boolean isDate(String date) {
        return pattern.matcher(date).matches();
    }

    private ApplicationDetail configureDetail(Statistic currentStatistic, List eles) {
        ApplicationDetail applicationDetail = new ApplicationDetail();
        try {
            int size = eles.size();
            applicationDetail.setApplicationDetailId(IdWorker.getId());
            if(size > 5) {
                applicationDetail.setDetailDate(String.valueOf(eles.get(5)));
            }
            if(size > 6) {
                Long num = DecimalUtil.getLong(String.valueOf(eles.get(6)));
                applicationDetail.setNum(num);
            }
            applicationDetail.setStatisticId(currentStatistic.getStatisticId());
            applicationDetail.setCreateUser(currentStatistic.getCreateUser());
            applicationDetail.setCreateTime(new Date());
        }catch (Exception e) {
            throw new ServiceException(500, "解析ApplicationDetail对象失败！");
        }

        return applicationDetail;
    }

    private Statistic configureStatistic(Long applicationFormId, List eles) {
        Statistic statistic = new Statistic();
        try {
            statistic.setApplicationFormId(applicationFormId);
            statistic.setStatisticId(IdWorker.getId());
            String provinceName = String.valueOf(eles.get(0)).replaceAll("医保", StringUtils.EMPTY);
            Long provinceId = ConstantFactory.me().getProvinceId(provinceName);
            statistic.setProvinceId(provinceId);
            Long mainNum = DecimalUtil.getLong(String.valueOf(eles.get(1)));
            statistic.setMainNum(mainNum);
            Long detailNum = DecimalUtil.getLong(String.valueOf(eles.get(2)));
            statistic.setDetailNum(detailNum);
            Long hasDischargeNum = DecimalUtil.getLong(String.valueOf(eles.get(3)));
            statistic.setHasDischargeNum(hasDischargeNum);
            String medicalTreatment = String.valueOf(eles.get(4));
            statistic.setMedicalTreatment(medicalTreatment);
            statistic.setCreateUser(ShiroKit.getUserNotNull().getId());
            statistic.setCreateTime(new Date());
        }catch (Exception e) {
            throw new ServiceException(500, "解析Statistic对象失败！");
        }
        return statistic;
    }

    private Statistic getCurrentStatistic(List<Statistic> statistics) {
        int size = statistics.size();
        return statistics.get(size-1);
    }
}
