package com.woniu.service;

import com.woniu.spring.annotation.Component;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: zhangcheng
 * @CreateTime: 2022-09-05  17:22
 * @Description: 后置处理器
 */
@Component
public class BeanPostProcessor implements com.woniu.spring.inter.BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if ("orderService".equals(beanName)){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    System.out.println("这里是对目标类进行增强！！！");
                    return null;
                }
            });
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        System.out.println("====初始化后=====   "+beanName);
        return bean;
    }
}
