package cn.pcs.studentclubmanagement.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis Token管理工具类
 * 用于管理token在Redis中的存储和有效期控制
 */
@Component
public class RedisTokenUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // Token在Redis中的前缀
    private static final String TOKEN_PREFIX = "token:";

    // Token有效期（5分钟）
    private static final long TOKEN_EXPIRATION = 5 * 60;

    /**
     * 将token存储到Redis中，设置5分钟过期时间
     * 
     * @param token  JWT token
     * @param userId 用户ID
     */
    public void storeToken(String token, Long userId) {
        String key = TOKEN_PREFIX + userId;
        stringRedisTemplate.opsForValue().set(key, token, TOKEN_EXPIRATION, TimeUnit.SECONDS);
    }

    /**
     * 检查token是否在Redis中存在且有效
     * 
     * @param token  JWT token
     * @param userId 用户ID
     * @return 是否有效
     */
    public boolean isTokenValid(String token, Long userId) {
        String key = TOKEN_PREFIX + userId;
        String storedToken = stringRedisTemplate.opsForValue().get(key);
        return token.equals(storedToken);
    }

    /**
     * 检查token是否在Redis中存在
     * 
     * @param userId 用户ID
     * @return 是否存在
     */
    public boolean isTokenExists(Long userId) {
        String key = TOKEN_PREFIX + userId;
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * 刷新token的有效期（重新设置5分钟）
     * 
     * @param userId 用户ID
     */
    public void refreshToken(Long userId) {
        String key = TOKEN_PREFIX + userId;
        String token = stringRedisTemplate.opsForValue().get(key);
        if (token != null) {
            stringRedisTemplate.opsForValue().set(key, token, TOKEN_EXPIRATION, TimeUnit.SECONDS);
        }
    }

    /**
     * 使token失效（从Redis中删除）
     * 
     * @param userId 用户ID
     */
    public void invalidateToken(Long userId) {
        String key = TOKEN_PREFIX + userId;
        stringRedisTemplate.delete(key);
    }

    /**
     * 获取token的剩余有效期（秒）
     * 
     * @param userId 用户ID
     * @return 剩余有效期（秒），-1表示不存在
     */
    public long getTokenExpiration(Long userId) {
        String key = TOKEN_PREFIX + userId;
        Long expiration = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expiration != null ? expiration : -1;
    }

    /**
     * 获取存储的token
     * 
     * @param userId 用户ID
     * @return 存储的token，如果不存在返回null
     */
    public String getStoredToken(Long userId) {
        String key = TOKEN_PREFIX + userId;
        return stringRedisTemplate.opsForValue().get(key);
    }
}