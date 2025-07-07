package cn.pcs.studentclubmanagement.interceptor;

import cn.pcs.studentclubmanagement.util.JwtUtil;
import cn.pcs.studentclubmanagement.util.RedisTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT拦截器
 * 用于验证请求中的JWT令牌
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTokenUtil redisTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 设置响应类型
        response.setContentType("application/json;charset=UTF-8");

        // 获取Authorization头
        String authHeader = request.getHeader("Authorization");

        // 如果没有Authorization头，返回未授权错误
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, 401, "未提供有效的认证令牌");
            return false;
        }

        // 提取令牌
        String token = jwtUtil.extractTokenFromHeader(authHeader);

        // 验证JWT令牌格式
        if (token == null || !jwtUtil.validateToken(token)) {
            sendErrorResponse(response, 401, "令牌格式无效或已过期");
            return false;
        }

        // 获取用户ID
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 检查token是否在Redis中存在且有效（5分钟有效期）
        if (!redisTokenUtil.isTokenValid(token, userId)) {
            sendErrorResponse(response, 401, "令牌已失效，请重新登录");
            return false;
        }

        // 将用户信息添加到请求属性中，供后续使用
        request.setAttribute("userId", userId);
        request.setAttribute("username", jwtUtil.getUsernameFromToken(token));
        request.setAttribute("role", jwtUtil.getRoleFromToken(token));

        return true;
    }

    /**
     * 发送错误响应
     * 
     * @param response HTTP响应
     * @param code     错误代码
     * @param message  错误消息
     * @throws IOException IO异常
     */
    private void sendErrorResponse(HttpServletResponse response, int code, String message) throws IOException {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", code);
        errorResponse.put("message", message);
        errorResponse.put("data", null);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
    }
}