package com.itheima.reggie.common;

import com.itheima.reggie.controller.R;
import com.itheima.reggie.exception.BusinessException;
import com.itheima.reggie.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author 陈万三
 * @create 2023-03-08 14:25
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            log.info("已存在");
            return R.error("已存在");
        }
        return R.error("未知错误");
    }

    /**
     * 业务异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public R<String> businessExceptionHandler(BusinessException ex){
        log.info(ex.getMessage());
        return R.error(ex.getMessage());
    }
    /**
     * 业务异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException ex){
        log.info(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
