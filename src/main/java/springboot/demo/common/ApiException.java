package springboot.demo.common;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import springboot.demo.cache.ResCodeMessageCache;

import java.util.HashMap;
import java.util.Map;

public class ApiException extends Exception {

    String resCode;
    String resDesc;

    public ApiException(String resCode) {
        super(ResCodeMessageCache.getMessageDesc(resCode));
        this.resCode = resCode;
        resDesc = ResCodeMessageCache.getMessageDesc(resCode);
    }

    ApiException(String code, String msg) {
        super(msg);
        this.resCode = code;
        this.resDesc = msg;
    }

    static Map getResponse(Exception e) {
        if (e instanceof UndeclaredThrowableException) {
            if (((UndeclaredThrowableException)e).getUndeclaredThrowable() instanceof ApiException) {
                ApiException sys = (ApiException)((UndeclaredThrowableException)e).getUndeclaredThrowable();
                Map map = new HashMap();
                map.put(sys.resCode,sys.resDesc);
                return map;
            }
        } else if (e instanceof ApiException) {
            ApiException sys = (ApiException)e;
            Map map = new HashMap();
            map.put(sys.resCode,sys.resDesc);
            return map;
        }
        Map map = new HashMap();
        map.put(CommonConstant.RE_SYS_ERROR_CODE,e.getMessage());
        return map;
    }

}
