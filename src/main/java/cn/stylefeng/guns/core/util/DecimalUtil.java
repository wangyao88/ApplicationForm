package cn.stylefeng.guns.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * decimal变量获取
 *
 * @author fengshuonan
 * @date 2019-04-04-17:06
 */
public class DecimalUtil {

    /**
     * 获取object的值
     *
     * @author fengshuonan
     * @Date 2019-04-04 17:07
     */
    public static Long getLong(Object object) {
        if (object == null) {
            return 0L;
        }
        try {
            if (object instanceof BigDecimal) {
                return ((BigDecimal) object).longValue();
            }
            if (object instanceof BigInteger) {
                return ((BigInteger) object).longValue();
            }
            if (object instanceof Long) {
                return ((Long) object);
            }
            if (object instanceof String) {
                return Long.valueOf(String.valueOf(object));
            }
        } catch (NumberFormatException e) {
            return 0L;
        }
        return 0L;
    }

}
