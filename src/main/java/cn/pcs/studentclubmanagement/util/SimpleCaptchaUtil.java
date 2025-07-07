package cn.pcs.studentclubmanagement.util;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;

public class SimpleCaptchaUtil {
    public static class CaptchaResult {
        public String code;
        public String base64;
    }

    public static CaptchaResult createCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 40, 4, 20);
        CaptchaResult result = new CaptchaResult();
        result.code = captcha.getCode();
        result.base64 = captcha.getImageBase64Data();
        return result;
    }
}