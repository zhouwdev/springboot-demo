package springboot.demo.cache;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResCodeMessageCache implements InitializingBean {

    private static final String Success = "0000";

    private Resource[] codeXml;

    public ResCodeMessageCache(Resource[] codeXml) {
        this.codeXml = codeXml;
    }

    public void setCodeXml(Resource[] codeXml) {
        this.codeXml = codeXml;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Map<String, String> ResMessage = new HashMap<>();


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("加载ResCode消息字典开始");
        for (Resource xl : codeXml) {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(xl.getInputStream());
            Element root = document.getRootElement();
            List<Element> elements = root.elements("Message");
            for (Element el : elements) {
                ResMessage.put(el.attributeValue("code"), el.attributeValue("desc"));
            }
        }
        logger.info("加载ResCode消息字典结束");
    }

    /**
     * 根据code获取描述信息
     *
     * @param code
     * @return
     */
    public static String getMessageDesc(String code) {
        return ResMessage.get(code);
    }

    /**
     * 根据Exception 获取 异常code
     *
     * @param e
     * @return
     */
    public static String getMessageCode(Exception e) {
        String res;
        if (e.getMessage() != null && e.getMessage() != "") {
            res = ResMessage.get(e.getMessage());
        } else {
            res = "9999";
        }
        return res;

    }

    /**
     * 根据Exception 获取 异常code
     *
     * @param e
     * @param defaultCode
     * @return
     */
    public static String getMessageCode(Exception e, String defaultCode) {

        String errCode = getMessageCode(e);
        if (errCode == "9999") errCode = defaultCode;

        return errCode;
    }

}
