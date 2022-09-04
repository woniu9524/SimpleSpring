package com.woniu.spring;

/**
 * @Author: zhangcheng
 * @CreateTime: 2022-09-04  23:59
 * @Description: TODO
 */
public class BeanDefinition {
    private Class type;//bean的类型
    private String scope;//单例还是多例

    public BeanDefinition() {
    }

    public BeanDefinition(Class type, String scope) {
        this.type = type;
        this.scope = scope;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
