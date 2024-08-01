package cn.spider.framework.code.agent.areabase.config;
import cn.spider.framework.code.agent.areabase.utils.WrapMapper;
import cn.spider.framework.code.agent.areabase.utils.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //获取堆栈信息
    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    /**
     * 功能描述:未知的运行时异常 统一处理 不给前端返回完整的堆栈信息
     * @param e
     * @date 2020-09-24 15:42:02
     **/
    @ExceptionHandler(RuntimeException.class)
    public Wrapper<String> runtimeException(RuntimeException e) {
        e.printStackTrace();
        log.error("error:",e);
        return WrapMapper.wrap(500,"内部错误",getStackTrace(e));
    }


    /**
     * 统一参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Wrapper methodArgumentError(MethodArgumentNotValidException e){
        e.printStackTrace();
        log.warn("warn",e);
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = String.format("%s",fieldError.getDefaultMessage());
        return WrapMapper.wrap(400,message);
    }

    /**
     * 主要用于huTool 断言的统一异常
     * cn.hutool.core.lang.Assert; 不要导错包了
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Wrapper huToolAssertException(IllegalArgumentException e){
        e.printStackTrace();
        log.error("error:",e);
        return WrapMapper.wrap(500,e.getMessage());
    }

    /**
     * 自定义404
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Wrapper NoHandlerFoundException(NoHandlerFoundException e){
        e.printStackTrace();
        return WrapMapper.wrap(404,e.getMessage());
    }

}
