package com.woniu.service;

import com.woniu.spring.annotation.Component;

/**
 * @Author: zhangcheng
 * @CreateTime: 2022-09-05  00:52
 * @Description: OrderService
 */
@Component
public class OrderService {
    public void order(String name) {
        System.out.println("order->" + name);
    }
}
