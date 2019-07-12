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
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 申请单列表的包装
 *
 * @author fengshuonan
 * @date 2017年4月25日 18:10:31
 */
public class ApplicationFormWrapper extends BaseControllerWrapper {

    public ApplicationFormWrapper(Map<String, Object> single) {
        super(single);
    }

    public ApplicationFormWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ApplicationFormWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ApplicationFormWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Long applicationUserId = DecimalUtil.getLong(map.get("applicationUser"));
        User applicationUser = ConstantFactory.me().getUser(applicationUserId);
        map.put("applicationUserName", applicationUser.getName());
        map.put("applicationUserDeptName", ConstantFactory.me().getDeptName(applicationUser.getDeptId()));
        map.put("applicationUserEmail", applicationUser.getEmail());
        map.put("applicationUserPhone", applicationUser.getPhone());
        Long applicationFormTypeId = DecimalUtil.getLong(map.get("applicationFormTypeId"));
        map.put("applicationFormTypeName", ConstantFactory.me().getDictName(applicationFormTypeId));
        Long receiveUser = DecimalUtil.getLong(map.get("receiveUser"));
        map.put("receiveUserName", ConstantFactory.me().getUserNameById(receiveUser));
        Long projectId = DecimalUtil.getLong(map.get("projectId"));
        map.put("projectTitle", ConstantFactory.me().getProjectTitle(projectId));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp applicationTime = (Timestamp) map.get("applicationTime");
        simpleDateFormat.format(applicationTime);
        map.put("applicationTime", simpleDateFormat.format(applicationTime));

        Timestamp receiveTime = (Timestamp) map.get("receiveTime");
        simpleDateFormat.format(receiveTime);
        map.put("receiveTime", simpleDateFormat.format(receiveTime));
    }
}
