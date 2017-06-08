package springboot.demo.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CommonConstant {

    public final static Map CacheConstant = new HashMap<>();

    public final static Map CacheReverseConstant = new HashMap<>();

    public final static String ConstantName = "com.shangying.constant.CommonConstant\\$";

    public final static String SplitConstant = "\\$";

    public static final String DEFAULT_PAGE_SIZE = "20";

    public static final String PAGE_SIZE_10 = "10";

    public static final long PAGE_SIZE_10_INT = 10L;

    /**
     * 占位符
     */
    public static final String PLACE_HOLDER = "--";

    /**
     * 返回成功 编码
     */
    static final String RE_SUCCESS_CODE = "0000";

    /**
     * 系统异常
     */
    static final String RE_SYS_ERROR_CODE = "9999";

    /**
     * 金额格式化小数位数
     */
    static final int SCALE = 2;

    static final int ROUND_DOWN = BigDecimal.ROUND_DOWN;

    static final String APP_DATE_FORMAT_PATTEN = "yyyy/MM/dd";

    public static final String APP_DEFAULT_MSG = "--";


    /**
     * 根据 enum名称 获取描述
     *
     * @param enumName
     * @param value
     * @return
     */
    static String ConstantValueOfName(String enumName, String value) throws Exception {
        Map conMap = (Map) CacheConstant.get(enumName);
        Map reverseMap = new HashMap();
        if (conMap == null) {
            conMap = new HashMap();
            Class enumClass = Class.forName(ConstantName + enumName);
            Object[] list = enumClass.getEnumConstants();
            for (int i = 0; i < list.length; i++) {
                Map map = (Map) list[i];
                conMap.putAll(map);
            }
            CacheConstant.put(enumName, conMap);
            CacheReverseConstant.put(enumName, reverseMap);
        }
        return conMap.get(value) != null ? reverseMap.get(value).toString() : "";
    }

    /**
     * 根据 enum名称 和名字 获取值
     *
     * @param enumName
     * @param name
     * @return
     */
    static String ConstantNameOfValue(String enumName, String name) throws Exception {
        Map reverseMap = (Map) CacheReverseConstant.get(enumName);
        Map conMap = new HashMap();
        if (reverseMap == null) {
            reverseMap = new HashMap();
            Class enumClass = Class.forName(ConstantName + enumName);
            Object[] list = enumClass.getEnumConstants();
            for (int i = 0; i < list.length; i++) {
                Map map = (Map) list[i];
                conMap.putAll(map);
                reverseMap.putAll(map);
            }
            CacheConstant.put(enumName, conMap);
            CacheReverseConstant.put(enumName, reverseMap);
        }
        return reverseMap.get(name) != null ? reverseMap.get(name).toString() : "";
    }

    /**
     * 获取 enum 类型的值和描述，返回是map
     *
     * @return
     */
    static Map getConstantMap(String enumName) throws Exception {
        Map res = (Map) CacheConstant.get(enumName);
        Map reverseMap = new HashMap();
        if (res == null) {
            res = new HashMap();
            Class enumClass = Class.forName(ConstantName + enumName);
            Object[] list = enumClass.getEnumConstants();
            for (int i = 0; i < list.length; i++) {
                Map map = (Map) list[i];
                res.putAll(map);
                reverseMap.putAll(map);
            }
            CacheConstant.put(enumName, res);
            CacheReverseConstant.put(enumName, reverseMap);
        }
        return res;
    }

    /**
     * 获取 enum 类型的值和描述，返回是List
     *
     * @param enumName
     * @return
     */
    static List getConstantList(String enumName) throws Exception {
        List newList = new ArrayList<>();
        Class enumClass = Class.forName(ConstantName + enumName);
        Object[] list = enumClass.getEnumConstants();
        for (int i = 0; i < list.length; i++) {
            Map map = (Map) list[i];
            newList.add(map);
        }

        return newList;
    }

    enum YesOrNo {
        no("否", "0"),
        yes("是", "1");
        String name;
        String value;

        YesOrNo(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

}