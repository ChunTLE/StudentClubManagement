package cn.pcs.studentclubmanagement.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类
 * 用于生成BCrypt加密的密码
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 生成BCrypt加密的密码
     * 
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码
     * 
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 生成测试用的BCrypt密码
     * 可以在数据库中直接使用
     */
    public static void main(String[] args) {
        String password = "123456";
        String encoded = encode(password);
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt加密后: " + encoded);
        System.out.println("验证结果: " + matches(password, encoded));

        // 生成多个不同的加密结果（BCrypt每次加密结果都不同）
        System.out.println("\n生成多个加密结果:");
        for (int i = 0; i < 3; i++) {
            System.out.println("第" + (i + 1) + "次: " + encode(password));
        }
    }
}