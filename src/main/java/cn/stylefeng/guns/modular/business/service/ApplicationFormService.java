package cn.stylefeng.guns.modular.business.service;

import cn.stylefeng.guns.config.properties.GunsProperties;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.business.entity.ApplicationForm;
import cn.stylefeng.guns.modular.business.mapper.ApplicationFormMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class ApplicationFormService extends ServiceImpl<ApplicationFormMapper, ApplicationForm> {

    @Autowired
    private GunsProperties gunsProperties;

    /**
     * 获取申请单列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> list(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition);
    }

    public byte[] getImageByName(String name) {
        byte[] buffer = "".getBytes();
        try {
            String fileSavePath = mkdirForUpload();
            String filePath = fileSavePath + name;
            File file = new File(filePath);
            @Cleanup
            FileInputStream fis = new FileInputStream(file);
            @Cleanup
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public String mkdirForUpload() {
        String fileSavePath = gunsProperties.getFileUploadPath();
        File dir = new File(fileSavePath);
        if(!dir.exists() && dir.isDirectory()){
            dir.mkdirs();
        }
        return fileSavePath;
    }
}
