package springboot.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.demo.common.JsonUtils;
import springboot.demo.common.RestTemplateExt;
import springboot.demo.service.RedisService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouwei on 2017/5/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationRun.class)
public class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisService redisService;

    @Autowired
    private RestTemplateExt restTemplateExt;

    @Test
    public void test() {
        ThreadCase.ConcurrentTask[] tasks = new ThreadCase.ConcurrentTask[5];
        for (int i = 0; i < 5; i++) {
            ThreadCase.ConcurrentTask task = new ThreadCase.ConcurrentTask() {
                public void run() {
                    System.out.println("hello");
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            tasks[i] = task;
            new ThreadCase(tasks);
        }

    }

    @Test
    public void redisTest() {
        redisService.set("123","23435");
        System.out.println(redisService.addLocked("testAddLock",60));
        System.out.println(redisService.get("LOCK_KEY:testAddLock"));
        System.out.println(redisService.get("123"));
    }

    @Test
    public void testRest()  throws  Exception{
        Map<String, String> params = new HashMap<>();
        params.put("userId","1");
        Object result = restTemplateExt.getForObject("http://10.52.2.202:7002/api/acct/queryAcctId", new HttpEntity<>(null, null), Object.class, params);
        System.out.println(JsonUtils.toJson(result));
    }
}
