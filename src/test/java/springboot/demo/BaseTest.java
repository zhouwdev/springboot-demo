package springboot.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.demo.service.RedisService;

/**
 * Created by zhouwei on 2017/5/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationRun.class)
public class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisService redisService;

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
}
