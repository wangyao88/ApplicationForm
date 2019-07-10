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
import cn.stylefeng.guns.core.common.constant.dictmap.ProvinceDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.node.TreeviewNode;
import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.business.entity.Province;
import cn.stylefeng.guns.modular.business.model.ProvinceDto;
import cn.stylefeng.guns.modular.business.service.ProvinceService;
import cn.stylefeng.guns.modular.business.warpper.ProvinceTreeWrapper;
import cn.stylefeng.guns.modular.business.warpper.ProvinceWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.treebuild.DefaultTreeBuildFactory;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 省市控制器
 *
 * @author fengshuonan
 * @Date 2017年2月17日20:27:22
 */
@Controller
@RequestMapping("/province")
public class ProvinceController extends BaseController {

    private String PREFIX = "/modular/business/province/";

    @Autowired
    private ProvinceService provinceService;

    /**
     * 跳转到省市管理首页
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "province.html";
    }

    /**
     * 跳转到添加省市
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @RequestMapping("/province_add")
    public String provinceAdd() {
        return PREFIX + "province_add.html";
    }

    /**
     * 跳转到修改省市
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @Permission
    @RequestMapping("/province_update")
    public String provinceUpdate(@RequestParam("provinceId") Long provinceId) {

        if (ToolUtil.isEmpty(provinceId)) {
            throw new RequestEmptyException();
        }

        //缓存省市修改前详细信息
        Province province = provinceService.getById(provinceId);
        LogObjectHolder.me().set(province);

        return PREFIX + "province_edit.html";
    }

    /**
     * 获取省市的tree列表，ztree格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = this.provinceService.tree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 获取省市的tree列表，treeview格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/treeview")
    @ResponseBody
    public List<TreeviewNode> treeview() {
        List<TreeviewNode> treeviewNodes = this.provinceService.treeviewNodes();

        //构建树
        DefaultTreeBuildFactory<TreeviewNode> factory = new DefaultTreeBuildFactory<>();
        factory.setRootParentId("0");
        List<TreeviewNode> results = factory.doTreeBuild(treeviewNodes);

        //把子节点为空的设为null
        ProvinceTreeWrapper.clearNull(results);

        return results;
    }

    /**
     * 新增省市
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "添加省市", key = "simpleName", dict = ProvinceDict.class)
    @RequestMapping(value = "/add")
    @Permission
    @ResponseBody
    public ResponseData add(Province province) {
        this.provinceService.addProvince(province);
        return SUCCESS_TIP;
    }

    /**
     * 获取所有省市列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(value = "condition", required = false) String condition,
                       @RequestParam(value = "provinceId", required = false) Long provinceId) {
        Page<Map<String, Object>> list = this.provinceService.list(condition, provinceId);
        Page<Map<String, Object>> wrap = new ProvinceWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 省市详情
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/detail/{provinceId}")
    @Permission
    @ResponseBody
    public Object detail(@PathVariable("provinceId") Long provinceId) {
        Province province = provinceService.getById(provinceId);
        ProvinceDto provinceDto = new ProvinceDto();
        BeanUtil.copyProperties(province, provinceDto);
        provinceDto.setPName(ConstantFactory.me().getProvinceName(provinceDto.getPid()));
        return provinceDto;
    }

    /**
     * 修改省市
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "修改省市", key = "simpleName", dict = ProvinceDict.class)
    @RequestMapping(value = "/update")
    @Permission
    @ResponseBody
    public ResponseData update(Province province) {
        provinceService.editProvince(province);
        return SUCCESS_TIP;
    }

    /**
     * 删除省市
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "删除省市", key = "provinceId", dict = ProvinceDict.class)
    @RequestMapping(value = "/delete")
    @Permission
    @ResponseBody
    public ResponseData delete(@RequestParam Long provinceId) {

        //缓存被删除的省市名称
        LogObjectHolder.me().set(ConstantFactory.me().getProvinceName(provinceId));

        provinceService.deleteProvince(provinceId);

        return SUCCESS_TIP;
    }

}
