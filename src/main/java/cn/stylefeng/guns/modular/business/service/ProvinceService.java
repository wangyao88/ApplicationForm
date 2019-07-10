package cn.stylefeng.guns.modular.business.service;

import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.node.TreeviewNode;
import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.business.entity.Province;
import cn.stylefeng.guns.modular.business.mapper.ProvinceMapper;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 省市表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class ProvinceService extends ServiceImpl<ProvinceMapper, Province> {

    @Resource
    private ProvinceMapper provinceMapper;

    /**
     * 新增省市
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:00 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void addProvince(Province province) {

        if (ToolUtil.isOneEmpty(province, province.getSimpleName(), province.getFullName(), province.getPid())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //完善pids,根据pid拿到pid的pids
        this.provinceSetPids(province);

        this.save(province);
    }

    /**
     * 修改省市
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:00 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void editProvince(Province province) {

        if (ToolUtil.isOneEmpty(province, province.getProvinceId(), province.getSimpleName(), province.getFullName(), province.getPid())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //完善pids,根据pid拿到pid的pids
        this.provinceSetPids(province);

        this.updateById(province);
    }

    /**
     * 删除省市
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:16 PM
     */
    @Transactional
    public void deleteProvince(Long provinceId) {
        Province province = provinceMapper.selectById(provinceId);

        //根据like查询删除所有级联的省市
        List<Province> subProvinces = provinceMapper.likePids(province.getProvinceId());

        for (Province temp : subProvinces) {
            this.removeById(temp.getProvinceId());
        }

        this.removeById(province.getProvinceId());
    }

    /**
     * 获取ztree的节点列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:16 PM
     */
    public List<ZTreeNode> tree() {
        return this.baseMapper.tree();
    }

    /**
     * 获取ztree的节点列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:16 PM
     */
    public List<TreeviewNode> treeviewNodes() {
        return this.baseMapper.treeviewNodes();
    }

    /**
     * 获取所有省市列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:16 PM
     */
    public Page<Map<String, Object>> list(String condition, Long provinceId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition, provinceId);
    }

    /**
     * 设置省市的父级ids
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:58 PM
     */
    private void provinceSetPids(Province province) {
        if (ToolUtil.isEmpty(province.getPid()) || province.getPid().equals(0L)) {
            province.setPid(0L);
            province.setPids("[0],");
        } else {
            Long pid = province.getPid();
            Province temp = this.getById(pid);
            String pids = temp.getPids();
            province.setPid(pid);
            province.setPids(pids + "[" + pid + "],");
        }
    }
}
