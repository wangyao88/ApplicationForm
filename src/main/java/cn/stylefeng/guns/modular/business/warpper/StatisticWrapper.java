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
package cn.stylefeng.guns.modular.business.warpper;

import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.util.DecimalUtil;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 统计信息列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class StatisticWrapper extends BaseControllerWrapper {

    public StatisticWrapper(Map<String, Object> single) {
        super(single);
    }

    public StatisticWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public StatisticWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public StatisticWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Long createUserId = DecimalUtil.getLong(map.get("createUser"));
        map.put("createUserName", ConstantFactory.me().getUserNameById(createUserId));

        Long provinceId = DecimalUtil.getLong(map.get("provinceId"));
        map.put("provinceName", ConstantFactory.me().getProvinceName(provinceId));

        Long continuationId = DecimalUtil.getLong(map.get("continuationId"));
        map.put("continuationName", ConstantFactory.me().getDictName(continuationId));

        if("null".equalsIgnoreCase(String.valueOf(map.get("medicalTreatment")))) {
            map.put("medicalTreatment", StringUtils.EMPTY);
        }
    }
}
