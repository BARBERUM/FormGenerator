package org.example.formgenerator.handler;

import jakarta.validation.constraints.NotBlank;
import org.example.formgenerator.utils.ApiException;
import org.example.formgenerator.utils.R;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice   //捕获全局异常
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public R<?> apiExceptionHandler(ApiException e)
    {
        return R.failure(e.getMessage());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> methodException(MethodArgumentNotValidException e)
    {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(fieldName,errorMsg);
        });
        return R.error(401, "参数校验异常", errors);
    }

    @ExceptionHandler(IOException.class)
    public R<?> ioeException(IOException e)
    {
        return R.failure("文件读取失败：" + e.getMessage());
    }
    @ExceptionHandler(RuntimeException .class)
    public R<?> runTimeException(RuntimeException  e)
    {
        return R.failure("Excel导入失败：" + e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public R<?> exception(Exception e)
    {
        return R.failure("系统异常：" + e.getMessage());
    }
}

