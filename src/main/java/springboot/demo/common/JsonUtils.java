package springboot.demo.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    final static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private final static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            if (object != null) {
                return mapper.writeValueAsString(object);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    public static String toJson(Map map) {
        return toJson((Object) map);
    }

    public static Map toMap(String json) throws Exception {
        if (json == null || json.isEmpty() || json == "{}") {
            return new HashMap();
        }
        return mapper.readValue(json, Map.class);
    }

    public static List toList(String json) throws Exception {
        if (json == null || json.isEmpty() || json == "[]") {
            return new ArrayList();
        }
        return mapper.readValue(json, List.class);
    }

    public static Map<String, String> jsonToMap(String json) throws Exception {
        if (json == null || json.isEmpty() || json == "{}") {
            return new HashMap<String, String>();
        }
        JavaType javaType = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, String.class);
        return mapper.readValue(json, javaType);
    }

    public static <T> T toObject(String json, Class<T> clazz) throws Exception {
        if (json == null || json.isEmpty() || json == "{}") {
            return null;
        }
        return mapper.readValue(json, clazz);
    }
}  