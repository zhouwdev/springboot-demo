package springboot.demo.service;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.commons.collections.functors.FalsePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2016/7/23.
 */
@Component
public class RedisService {
    private static final String LOCK_KEY = "LOCK_KEY:";
    @Autowired
    private RedisTemplate redisTemplate;
    private ValueOperations<Serializable, Object> valueOperations;

    @Autowired
    private void setValueOperations() {
        this.valueOperations = redisTemplate.opsForValue();
    }

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    public Set<Serializable> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return valueOperations.get(key);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value) {
        boolean result = false;
        try {
            valueOperations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            valueOperations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取分布锁
     *
     * @param key
     */
    public void getLocked(String key) throws Exception {
        getLocked(key, 10);
    }

    /**
     * 获取分布锁
     *
     * @param key
     * @param expireTime 失效时间(单位秒)
     */
    public void getLocked(String key, long expireTime) throws Exception {
        long intervalTime = 50;//获取锁间隔时间，单位毫秒
        while (!addLocked(key, expireTime)) {
            try {
                Thread.sleep(intervalTime);//100毫秒
            } catch (Exception e) {
                logger.warn("获取分布锁失败");
                throw e;
            }
        }
    }

    /**
     * 加分布锁
     *
     * @param key
     * @param expireTime 失效时间(单位秒)
     * @return true为加锁成功
     */
    public Boolean addLocked(String key, final long expireTime) {
        final String lock = LOCK_KEY + key;
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) {
                byte[] lockBytes = redisTemplate.getStringSerializer().serialize(lock);
                byte[] value = redisTemplate.getStringSerializer().serialize("1");
                boolean locked = connection.setNX(lockBytes, value);
                if (locked == true) {
                    connection.expire(lockBytes, expireTime);
                    return locked;
                }
                return locked;
            }
        });
    }

    /**
     * 释放分布锁
     *
     * @param key
     */
    public void releaseLock(final String key) {
        String lock = LOCK_KEY + key;
        remove(lock);
    }
}
