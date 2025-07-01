package cn.pcs.studentclubmanagement.exception;

import cn.pcs.studentclubmanagement.entity.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception exception){
        exception.printStackTrace();
        return Result.error(StringUtils.hasLength(exception.getMessage()) ? exception.getMessage() : "操作失败");
    }

}