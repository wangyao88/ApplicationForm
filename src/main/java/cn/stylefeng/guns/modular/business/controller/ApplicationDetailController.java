package cn.stylefeng.guns.modular.business.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.dictmap.DeleteDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.business.entity.ApplicationDetail;
import cn.stylefeng.guns.modular.business.model.ApplicationDetailDto;
import cn.stylefeng.guns.modular.business.service.ApplicationDetailService;
import cn.stylefeng.guns.modular.business.warpper.ApplicationDetailWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


/**
 * 基础字典控制器
 *
 * @author stylefeng
 * @Date 2019-03-13 13:53:53
 */
@Controller
@RequestMapping("/applicationDetail")
public class ApplicationDetailController extends BaseController {

    private String PREFIX = "/modular/business/applicationDetail";

    @Autowired
    private ApplicationDetailService applicationDetailService;

    /**
     * 跳转到主页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("")
    public String index(@RequestParam("statisticId") Long statisticId, Model model) {
        model.addAttribute("statisticId", statisticId);
        return PREFIX + "/applicationDetail.html";
    }

    /**
     * 获取统计信息明细列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Long statisticId) {
        Page<Map<String, Object>> list = this.applicationDetailService.list(statisticId);
        Page<Map<String, Object>> wrap = new ApplicationDetailWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 新增页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/applicationDetail_add")
    public String add(@RequestParam("statisticId") Long statisticId, Model model) {
        model.addAttribute("statisticId", statisticId);
        return PREFIX + "/applicationDetail_add.html";
    }

    @PostMapping("/add")
    @ResponseBody
    @BussinessLog(value = "新增统计信息明细", key = "applicationDetailId")
    public Object add(ApplicationDetail applicationDetail) {
        if (ToolUtil.isOneEmpty(applicationDetail, applicationDetail.getStatisticId(), applicationDetail.getDetailDate(), applicationDetail.getNum())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        applicationDetail.setCreateUser(ShiroKit.getUserNotNull().getId());
        applicationDetail.setCreateTime(new Date());
        this.applicationDetailService.save(applicationDetail);
        return SUCCESS_TIP;
    }

    /**
     * 新增页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/applicationDetail_batch_add")
    public String batchAdd(@RequestParam("statisticId") Long statisticId, Model model) {
        model.addAttribute("statisticId", statisticId);
        return PREFIX + "/applicationDetail_batch_add.html";
    }

    @PostMapping("/batch_add")
    @ResponseBody
    @BussinessLog(value = "新增统计信息明细", key = "applicationDetailId")
    public Object batchAdd(ApplicationDetail applicationDetail) {
        if (ToolUtil.isOneEmpty(applicationDetail, applicationDetail.getStatisticId(), applicationDetail.getContent())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        applicationDetail.setCreateUser(ShiroKit.getUserNotNull().getId());
        applicationDetail.setCreateTime(new Date());
        this.applicationDetailService.batchSave(applicationDetail);
        return SUCCESS_TIP;
    }

    /**
     * 编辑页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/applicationDetail_update")
    public String edit(@RequestParam("applicationDetailId") Long applicationDetailId, Model model) {
        model.addAttribute("applicationDetailId", applicationDetailId);
        return PREFIX + "/applicationDetail_edit.html";
    }

    /**
     * 省市详情
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/detail/{applicationDetailId}")
    @Permission
    @ResponseBody
    public ApplicationDetailDto detail(@PathVariable("applicationDetailId") Long applicationDetailId) {
        ApplicationDetail applicationDetail = this.applicationDetailService.getById(applicationDetailId);
        ApplicationDetailDto applicationDetailDto = new ApplicationDetailDto();
        BeanUtil.copyProperties(applicationDetail, applicationDetailDto);
        applicationDetailDto.setCreateUserName(ConstantFactory.me().getUserNameById(applicationDetailDto.getCreateUser()));
        return applicationDetailDto;
    }

    /**
     * 删除统计信息明细
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BussinessLog(value = "删除统计信息", key = "applicationDetailId", dict = DeleteDict.class)
    public Object delete(@RequestParam Long applicationDetailId) {
        this.applicationDetailService.removeById(applicationDetailId);
        return SUCCESS_TIP;
    }

    /**
     * 修改统计信息明细
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改统计信息明细", key = "applicationDetailId")
    public Object update(ApplicationDetail applicationDetail) {
        if (ToolUtil.isOneEmpty(applicationDetail, applicationDetail.getStatisticId(), applicationDetail.getDetailDate(), applicationDetail.getNum())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        ApplicationDetail old = this.applicationDetailService.getById(applicationDetail.getApplicationDetailId());
        old.setDetailDate(applicationDetail.getDetailDate());
        old.setNum(applicationDetail.getNum());
        old.setStatisticId(applicationDetail.getStatisticId());
        old.setUpdateUser(ShiroKit.getUserNotNull().getId());
        old.setUpdateTime(new Date());
        this.applicationDetailService.updateById(old);
        return SUCCESS_TIP;
    }
}


