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
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.dictmap.ApplicationFormMap;
import cn.stylefeng.guns.core.common.constant.dictmap.DeleteDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.business.entity.ApplicationForm;
import cn.stylefeng.guns.modular.business.model.ApplicationFormDto;
import cn.stylefeng.guns.modular.business.service.ApplicationFormService;
import cn.stylefeng.guns.modular.business.warpper.ApplicationFormWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * 申请单控制器
 *
 * @author fengshuonan
 * @Date 2017-05-09 23:02:21
 */
@Controller
@RequestMapping("/applicationForm")
public class ApplicationFormController extends BaseController {

    private String PREFIX = "/modular/business/applicationForm/";

    @Autowired
    private ApplicationFormService applicationFormService;

    /**
     * 跳转到申请单列表首页
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "applicationForm.html";
    }

    /**
     * 跳转到添加申请单
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/applicationForm_add")
    public String applicationFormAdd() {
        return PREFIX + "applicationForm_add.html";
    }

    /**
     * 跳转到修改申请单
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/applicationForm_update")
    public String applicationFormUpdate(@RequestParam("applicationFormId") Long applicationFormId) {

        if (ToolUtil.isEmpty(applicationFormId)) {
            throw new RequestEmptyException();
        }
        //缓存省市修改前详细信息
        ApplicationForm applicationForm = this.applicationFormService.getById(applicationFormId);
        LogObjectHolder.me().set(applicationForm);
        return PREFIX + "applicationForm_edit.html";
    }

    /**
     * 省市详情
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/detail/{applicationFormId}")
    @Permission
    @ResponseBody
    public Object detail(@PathVariable("applicationFormId") Long applicationFormId) {
        ApplicationForm applicationForm = this.applicationFormService.getById(applicationFormId);
        ApplicationFormDto applicationFormDto = new ApplicationFormDto();
        BeanUtil.copyProperties(applicationForm, applicationFormDto);
        applicationFormDto.setProvinceName(ConstantFactory.me().getProvinceName(applicationFormDto.getProvinceId()));
        return applicationFormDto;
    }

    /**
     * 获取申请单列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Map<String, Object>> list = this.applicationFormService.list(condition);
        Page<Map<String, Object>> wrap = new ApplicationFormWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 新增申请单
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增申请单", key = "title", dict = ApplicationFormMap.class)
    public Object add(ApplicationForm applicationForm) {
        if (ToolUtil.isOneEmpty(applicationForm, applicationForm.getProjectId(), applicationForm.getApplicationUser())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        applicationForm.setCreateUser(ShiroKit.getUserNotNull().getId());
        applicationForm.setCreateTime(new Date());
        applicationForm.setApplicationTime(new Date());
        this.applicationFormService.save(applicationForm);
        return SUCCESS_TIP;
    }

    /**
     * 删除申请单
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BussinessLog(value = "删除申请单", key = "applicationFormId", dict = DeleteDict.class)
    public Object delete(@RequestParam Long applicationFormId) {
        this.applicationFormService.removeById(applicationFormId);
        return SUCCESS_TIP;
    }

    /**
     * 修改申请单
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改申请单", key = "title", dict = ApplicationFormMap.class)
    public Object update(ApplicationForm applicationForm) {
        if (ToolUtil.isOneEmpty(applicationForm, applicationForm.getApplicationFormId(), applicationForm.getProjectId(), applicationForm.getProvinceId())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        ApplicationForm old = this.applicationFormService.getById(applicationForm.getApplicationFormId());
        old.setTitle(applicationForm.getTitle());
        old.setProvinceId(applicationForm.getProvinceId());
        this.applicationFormService.updateById(old);
        return SUCCESS_TIP;
    }
}
