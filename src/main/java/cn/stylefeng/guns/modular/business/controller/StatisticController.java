/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.stylefeng.guns.modular.business.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.config.properties.GunsProperties;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.constant.dictmap.DeleteDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.business.entity.ApplicationForm;
import cn.stylefeng.guns.modular.business.entity.Statistic;
import cn.stylefeng.guns.modular.business.model.StatisticDto;
import cn.stylefeng.guns.modular.business.service.ApplicationFormService;
import cn.stylefeng.guns.modular.business.service.ProjectService;
import cn.stylefeng.guns.modular.business.service.StatisticService;
import cn.stylefeng.guns.modular.business.warpper.StatisticWrapper;
import cn.stylefeng.guns.modular.system.model.DictDto;
import cn.stylefeng.guns.modular.system.service.DictService;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 统计信息控制器
 *
 * @author fengshuonan
 * @Date 2017-05-09 23:02:21
 */
@Controller
@RequestMapping("/statistic")
public class StatisticController extends BaseController {

    private String PREFIX = "/modular/business/statistic/";

    @Autowired
    private StatisticService statisticService;
    @Autowired
    private GunsProperties gunsProperties;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DictService dictService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationFormService applicationFormService;

    /**
     * 跳转到统计信息列表首页
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @GetMapping("")
    public String index() {
        return PREFIX + "statistic.html";
    }

    /**
     * 跳转到添加统计信息
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/statistic_add")
    public String statisticAdd() {
        return PREFIX + "statistic_add.html";
    }

    /**
     * 获取统计信息列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(name = "applicationFormId", required = false) Long applicationFormId, @RequestParam(name = "provinceName", required = false) String provinceName) {
        Page<Map<String, Object>> wrap;
        if(StringUtils.isBlank(provinceName)) {
            Page<Map<String, Object>> list = this.statisticService.listAll(applicationFormId);
            wrap = new StatisticWrapper(list).wrap();
        }else {
            provinceName = Joiner.on(StringUtils.EMPTY).join("%", provinceName, "%");
            Page<Map<String, Object>> list = this.statisticService.listCondition(applicationFormId, provinceName);
            wrap = new StatisticWrapper(list).wrap();
        }
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 新增统计信息
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @PostMapping("/add")
    @ResponseBody
    @BussinessLog(value = "新增统计信息", key = "statisticId")
    public Object add(Statistic statistic) {
        Long applicationFormId = statistic.getApplicationFormId();
        if (ToolUtil.isOneEmpty(statistic, statistic.getProvinceId(), applicationFormId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        ApplicationForm applicationForm = applicationFormService.getById(applicationFormId);
        if(Objects.isNull(applicationForm)) {
            throw new ServiceException(403, "无此申请单编号");
        }
        statistic.setCreateUser(ShiroKit.getUserNotNull().getId());
        statistic.setCreateTime(new Date());
        this.statisticService.save(statistic);
        return SUCCESS_TIP;
    }

    /**
     * 删除统计信息
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BussinessLog(value = "删除统计信息", key = "statisticId", dict = DeleteDict.class)
    public Object delete(@RequestParam Long statisticId) {
        this.statisticService.removeById(statisticId);
        return SUCCESS_TIP;
    }

    /**
     * 跳转到修改统计信息
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/statistic_update")
    public String statisticUpdate(@RequestParam("statisticId") Long statisticId) {
        if (ToolUtil.isEmpty(statisticId)) {
            throw new RequestEmptyException();
        }
        Statistic statistic = this.statisticService.getById(statisticId);
        LogObjectHolder.me().set(statistic);
        return PREFIX + "statistic_edit.html";
    }

    /**
     * 统计信息详情
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/detail/{statisticId}")
    @Permission
    @ResponseBody
    public Map<String, Object> detail(@PathVariable("statisticId") Long statisticId) {
        Statistic statistic = this.statisticService.getById(statisticId);
        StatisticDto statisticDto = new StatisticDto();
        BeanUtil.copyProperties(statistic, statisticDto);
        statisticDto.setProvinceName(ConstantFactory.me().getProvinceName(statisticDto.getProvinceId()));
        Map<String, Object> maps = initComboxs();
        maps.put("statistic", statisticDto);
        return maps;
    }

    /**
     * 修改统计信息
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改统计信息", key = "statisticId")
    public Object update(Statistic statistic) {
        Long applicationFormId = statistic.getApplicationFormId();
        if (ToolUtil.isOneEmpty(statistic, statistic.getStatisticId(), statistic.getProvinceId(), applicationFormId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        ApplicationForm applicationForm = applicationFormService.getById(applicationFormId);
        if(Objects.isNull(applicationForm)) {
            throw new ServiceException(403, "无此申请单编号");
        }
        Statistic old = this.statisticService.getById(statistic.getStatisticId());
        old.setProvinceId(statistic.getProvinceId());
        old.setMainNum(statistic.getMainNum());
        old.setDetailNum(statistic.getDetailNum());
        old.setHasDischargeNum(statistic.getHasDischargeNum());
        old.setMedicalTreatment(statistic.getMedicalTreatment());
        old.setBeginDate(statistic.getBeginDate());
        old.setEndDate(statistic.getEndDate());
        old.setContinuation(statistic.getContinuation());
        old.setUpdateUser(ShiroKit.getUserNotNull().getId());
        old.setUpdateTime(new Date());
        this.statisticService.updateById(old);
        return SUCCESS_TIP;
    }

    @PostMapping("/comboxs")
    @ResponseBody
    public Map<String, Object> comboxs(){
        return initComboxs();
    }

    private Map<String, Object> initComboxs() {
        List<DictDto> continuationDicts = dictService.allDict("CONTINUATION");
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("continuationDicts", continuationDicts);
        return maps;
    }

    /**
     * 跳转到导入统计信息
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/statistic_import")
    public String goImportExcel(@RequestParam("applicationFormId") Long applicationFormId, HttpServletRequest request) {
        request.getSession().setAttribute(Const.APPLICATIONfORM_ID_FOR_IMPORT, applicationFormId);
        return PREFIX + "statistic_import.html";
    }

    @PostMapping("/importExcel")
    @ResponseBody
    public Object importExcel(@RequestParam("file") MultipartFile excel, HttpServletRequest request) {
        String fileName = excel.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new ServiceException(403, "上传文件格式不正确");
        }
        try {
            Object attribute = request.getSession().getAttribute(Const.APPLICATIONfORM_ID_FOR_IMPORT);
            Long applicationFormId = Long.valueOf(String.valueOf(attribute));
            this.statisticService.importExcel(applicationFormId, fileName, excel);
        } catch (ServiceException e) {
            throw new ServiceException(500, "上传文件失败！");
        }
        return SUCCESS_TIP;
    }
}
