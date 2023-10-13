package com.xyongfeng.aop;


import com.alibaba.fastjson.JSONObject;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Param.UsersAddParam;
import com.xyongfeng.pojo.Param.UsersSetImgParam;
import com.xyongfeng.service.AdminLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * 日志记录切面
 *
 * @author xyongfeng
 * @since 2023-9-5
 */

@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    /**
     * 日志业务类
     */
    @Autowired
    private AdminLogService adminLogService;

    /**
     * 设置操作日志切入点，在注解的位置切入代码
     */
    @Pointcut("@annotation(com.xyongfeng.aop.OperationLogAnnotation)")
    public void operLogPoinCut() {
    }

    @AfterReturning(returning  /**
     * 记录操作日志
     * @param joinPoint 方法的执行点
     * @param result  方法返回值
     * @throws Throwable
     */ = "result", value = "operLogPoinCut()")
    public void saveOperLog(JoinPoint joinPoint, JsonResult result) {
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取注解信息
        OperationLogAnnotation annotation = method.getAnnotation(OperationLogAnnotation.class);
        if (annotation != null) {
            //获取参数值
            Object[] parameterArgs = joinPoint.getArgs();
            //获取参数值类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            //获取参数名
            String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(method);


            //建立上下文字典，储存参数信息
            StandardEvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < parameterArgs.length; i++) {
                if (parameterNames[i] != null) {
                    // JSONObject.toJSONString  可以将对象转换为json字符串
                    Object parameterArg = parameterArgs[i];
                    // 判断该类是否为UsersAddParam，则清空密码
                    if(UsersAddParam.class == parameterTypes[i]){
                        ((UsersAddParam)parameterArg).setPassword(null);
                    }
                        try {
                            context.setVariable(parameterNames[i], JSONObject.toJSONString(parameterArg));
                        } catch (Exception e) {
                            log.warn(String.format("%s(): %s 参数转换json失败", method.getName(), parameterArg));
                        }
                }
            }
            // 使用SpEL表达式替换文本
            SpelExpressionParser parser = new SpelExpressionParser();
            // 替换url
            Expression urlExpression = parser.parseExpression(annotation.actionUrl(), ParserContext.TEMPLATE_EXPRESSION);
            // 替换content
            Expression contentExpression = parser.parseExpression(annotation.actionContent(), ParserContext.TEMPLATE_EXPRESSION);


            // 读取注解信息，执行替换，再进行日志插入
            adminLogService.insert(
                    annotation.actionModule(),
                    annotation.actionType(),
                    (String) urlExpression.getValue(context),
                    (String) contentExpression.getValue(context),
                    result.getCode().equals(200));

        }

    }


}
