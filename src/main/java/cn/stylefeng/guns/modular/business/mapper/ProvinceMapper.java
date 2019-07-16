package cn.stylefeng.guns.modular.business.mapper;

import cn.stylefeng.guns.core.common.node.TreeviewNode;
import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.modular.business.entity.Province;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 省市表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public interface ProvinceMapper extends BaseMapper<Province> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有省市列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition, @Param("provinceId") Long provinceId);

    /**
     * 获取所有省市树列表
     */
    List<TreeviewNode> treeviewNodes();

    /**
     * where pids like ''
     */
    List<Province> likePids(@Param("provinceId") Long provinceId);

    List<Province> selectByName(@Param("provinceName") String provinceName);
}
