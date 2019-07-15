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
import cn.stylefeng.guns.core.common.constant.dictmap.ApplicationFormMap;
import cn.stylefeng.guns.core.common.constant.dictmap.DeleteDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.business.entity.ApplicationForm;
import cn.stylefeng.guns.modular.business.model.ApplicationFormDto;
import cn.stylefeng.guns.modular.business.model.ProjectSimpleDto;
import cn.stylefeng.guns.modular.business.service.ApplicationFormService;
import cn.stylefeng.guns.modular.business.service.ProjectService;
import cn.stylefeng.guns.modular.business.warpper.ApplicationFormWrapper;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.model.DictDto;
import cn.stylefeng.guns.modular.system.model.UserSimpleDto;
import cn.stylefeng.guns.modular.system.service.DictService;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    private GunsProperties gunsProperties;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DictService dictService;
    @Autowired
    private UserService userService;

    /**
     * 跳转到申请单列表首页
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @GetMapping("")
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

    @RequestMapping(method = RequestMethod.POST, path = "/img/upload")
    @ResponseBody
    public JSONObject upload(@RequestPart("image") MultipartFile image, HttpServletRequest request) {
        try {
            String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(image.getOriginalFilename());

            String fileSavePath = applicationFormService.mkdirForUpload();
            fileSavePath = fileSavePath + pictureName;
            image.transferTo(new File(fileSavePath));
            return getUploadImg(pictureName, request);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
    }

    private JSONObject getUploadImg(String pictureName, HttpServletRequest request) {
        String fileSavePath = request.getRequestURL() + "/getImageByName?name=" + pictureName;
        JSONObject json = new JSONObject();
        json.put("url", fileSavePath);
        return json;
    }

    @RequestMapping("/img/upload/getImageByName")
    public void getImageByName(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        @Cleanup
        ServletOutputStream out = response.getOutputStream();
        String name = request.getParameter("name");
        byte[] image = applicationFormService.getImageByName(name);
        if(image == null){
            return;
        }
        @Cleanup
        InputStream imageStream = new ByteArrayInputStream(image);
        response.setContentType("image/*");
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = imageStream.read(buf, 0, 1024)) != -1) {
            out.write(buf, 0, len);
        }
        out.flush();
    }

    /**
     * 新增申请单
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @PostMapping("/add")
    @ResponseBody
    @BussinessLog(value = "新增申请单", key = "applicationFormId", dict = ApplicationFormMap.class)
    public Object add(ApplicationForm applicationForm) {
        if (ToolUtil.isOneEmpty(applicationForm, applicationForm.getProjectId(), applicationForm.getDescription(), applicationForm.getUseText())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        applicationForm.setCreateUser(ShiroKit.getUserNotNull().getId());
        applicationForm.setCreateTime(new Date());
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
    public Map<String, Object> detail(@PathVariable("applicationFormId") Long applicationFormId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ApplicationForm applicationForm = this.applicationFormService.getById(applicationFormId);
        ApplicationFormDto applicationFormDto = new ApplicationFormDto();
        BeanUtil.copyProperties(applicationForm, applicationFormDto);
        applicationFormDto.setApplicationTimeStr(simpleDateFormat.format(applicationFormDto.getApplicationTime()));
        applicationFormDto.setReceiveTimeStr(simpleDateFormat.format(applicationFormDto.getReceiveTime()));
        applicationFormDto.setApplicationUserName(ConstantFactory.me().getUserNameById(applicationFormDto.getApplicationUser()));
        applicationFormDto.setReceiveUserName(ConstantFactory.me().getUserNameById(applicationFormDto.getReceiveUser()));
        User applicationUser = ConstantFactory.me().getUser(applicationFormDto.getApplicationUser());
        Long deptId = applicationUser.getDeptId();
        applicationFormDto.setApplicationUserDeptName(ConstantFactory.me().getDeptName(deptId));
        applicationFormDto.setApplicationUserEmail(applicationUser.getEmail());
        applicationFormDto.setApplicationUserPhone(applicationUser.getPhone());
        applicationFormDto.setCreateUserName(ConstantFactory.me().getUserNameById(applicationFormDto.getApplicationUser()));
        applicationFormDto.setProjectTitle(ConstantFactory.me().getProjectTitle(applicationFormDto.getApplicationUser()));
        applicationFormDto.setApplicationFormTypeName(ConstantFactory.me().getDictName(applicationFormDto.getApplicationFormTypeId()));
        Map<String, Object> maps = initComboxs();
        maps.put("applicationForm", applicationFormDto);
        return maps;
    }

    /**
     * 修改申请单
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改申请单", key = "applicationFormId", dict = ApplicationFormMap.class)
    public Object update(ApplicationForm applicationForm) {
        if (ToolUtil.isOneEmpty(applicationForm, applicationForm.getApplicationFormId(), applicationForm.getProjectId(), applicationForm.getDescription(), applicationForm.getUseText())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        ApplicationForm old = this.applicationFormService.getById(applicationForm.getApplicationFormId());
        old.setApplicationFormTypeId(applicationForm.getApplicationFormTypeId());
        old.setProjectId(applicationForm.getProjectId());
        old.setApplicationUser(applicationForm.getApplicationUser());
        old.setApplicationTime(applicationForm.getApplicationTime());
        old.setDescription(applicationForm.getDescription());
        old.setUseText(applicationForm.getUseText());
        old.setReceiveUser(applicationForm.getReceiveUser());
        old.setReceiveTime(applicationForm.getReceiveTime());
        old.setUpdateUser(ShiroKit.getUserNotNull().getId());
        old.setUpdateTime(new Date());
        this.applicationFormService.updateById(old);
        return SUCCESS_TIP;
    }

    @RequestMapping(value="/comboxs", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> comboxs(){
        return initComboxs();
    }

    private Map<String, Object> initComboxs() {
        List<ProjectSimpleDto> projectSimpleDtos = projectService.allProject();
        List<UserSimpleDto> userSimpleDtos = userService.allUser();
        List<DictDto> dictDtos = dictService.allDict("APPLICATION_FORM_TYPE");
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("projects", projectSimpleDtos);
        maps.put("users", userSimpleDtos);
        maps.put("dicts", dictDtos);
        return maps;
    }
}
