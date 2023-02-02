package com.geoffrey.laoye.common;

import com.geoffrey.laoye.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice//这个注解包装了以下这两个注解
//@ControllerAdvice(annotations = {RestController.class, Controller.class})
//@ResponseBody
public class GlobalExceptionHandler  {
    //以后开发业务都是搞一个Excetion叫未知异常，以后看见报错慢慢加上对应的自定义异常
    //一个异常有很多种情况造成，遇到了异常则在异常中进行自定义的异常处理，没遇到的统一定义为未知异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    //R<T>的T为String是因为异常处理返回的都是msg字符串
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知异常");
    }

    /**
     *  异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
