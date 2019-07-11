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
import cn.stylefeng.guns.core.common.constant.dictmap.DeleteDict;
import cn.stylefeng.guns.core.common.constant.dictmap.ProjectMap;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.business.entity.Project;
import cn.stylefeng.guns.modular.business.entity.Province;
import cn.stylefeng.guns.modular.business.model.ProjectDto;
import cn.stylefeng.guns.modular.business.model.ProjectSimpleDto;
import cn.stylefeng.guns.modular.business.model.ProvinceDto;
import cn.stylefeng.guns.modular.business.service.ProjectService;
import cn.stylefeng.guns.modular.business.warpper.ProjectWrapper;
import cn.stylefeng.guns.modular.system.model.UserSimpleDto;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目控制器
 *
 * @author fengshuonan
 * @Date 2017-05-09 23:02:21
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {

    private String PREFIX = "/modular/business/project/";

    @Autowired
    private ProjectService projectService;

    /**
     * 跳转到项目列表首页
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "project.html";
    }

    /**
     * 跳转到添加项目
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/project_add")
    public String projectAdd() {
        return PREFIX + "project_add.html";
    }

    /**
     * 跳转到修改项目
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/project_update")
    public String projectUpdate(@RequestParam("projectId") Long projectId) {

        if (ToolUtil.isEmpty(projectId)) {
            throw new RequestEmptyException();
        }
        //缓存省市修改前详细信息
        Project project = this.projectService.getById(projectId);
        LogObjectHolder.me().set(project);
        return PREFIX + "project_edit.html";
    }

    /**
     * 省市详情
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/detail/{projectId}")
    @Permission
    @ResponseBody
    public Object detail(@PathVariable("projectId") Long projectId) {
        Project project = this.projectService.getById(projectId);
        ProjectDto projectDto = new ProjectDto();
        BeanUtil.copyProperties(project, projectDto);
        projectDto.setProvinceName(ConstantFactory.me().getProvinceName(projectDto.getProvinceId()));
        return projectDto;
    }

    /**
     * 获取项目列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Map<String, Object>> list = this.projectService.list(condition);
        Page<Map<String, Object>> wrap = new ProjectWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 新增项目
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增项目", key = "projectId", dict = ProjectMap.class)
    public Object add(Project project) {
        if (ToolUtil.isOneEmpty(project, project.getTitle(), project.getProvinceId())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        project.setCreateUser(ShiroKit.getUserNotNull().getId());
        project.setCreateTime(new Date());
        this.projectService.save(project);
        return SUCCESS_TIP;
    }

    /**
     * 删除项目
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BussinessLog(value = "删除项目", key = "projectId", dict = DeleteDict.class)
    public Object delete(@RequestParam Long projectId) {
        //缓存项目名称
        LogObjectHolder.me().set(ConstantFactory.me().getProjectTitle(projectId));
        this.projectService.removeById(projectId);
        return SUCCESS_TIP;
    }

    /**
     * 修改项目
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改项目", key = "projectId", dict = ProjectMap.class)
    public Object update(Project project) {
        if (ToolUtil.isOneEmpty(project, project.getProjectId(), project.getTitle(), project.getProvinceId())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Project old = this.projectService.getById(project.getProjectId());
        old.setTitle(project.getTitle());
        old.setProvinceId(project.getProvinceId());
        this.projectService.updateById(old);
        return SUCCESS_TIP;
    }

    @RequestMapping(value="/allProject", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectSimpleDto> allProject(){
        return projectService.allProject();
    }
}
