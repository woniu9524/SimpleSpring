package com.woniu.spring;

import com.woniu.spring.annotation.Autowired;
import com.woniu.spring.annotation.Component;
import com.woniu.spring.annotation.ComponentScan;
import com.woniu.spring.annotation.Scope;
import com.woniu.spring.inter.BeanNameAware;
import com.woniu.spring.inter.BeanPostProcessor;
import com.woniu.spring.inter.InitializingBean;
import com.woniu.spring.utils.FileUtil;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * @Author: zhangcheng
 * @CreateTime: 2022-09-04  22:33
 * @Description: Spring容器
 */
public class ApplicationContext {

    private Class configClass;

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> singletonMap = new HashMap<>();
    private List<BeanPostProcessor> beanPostProcessorList=new ArrayList<>();

    public ApplicationContext(Class configClass) {
        this.configClass = configClass;
        //包扫描
        componentScan();
        //加载单例bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if ("singleton".equals(beanDefinition.getScope())) {
                createBean(beanName, beanDefinition);
            }
        }

    }

    //创建bean
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        try {
            //通过无参构造方法生成实例
            Object instance = clazz.getConstructor().newInstance();

            //依赖注入
            autoWired(clazz, instance);

            //aware回调
            beanNameAware(instance,beanName);

            //初始化前
            for (BeanPostProcessor beanPostProcessor  : beanPostProcessorList) {
                beanPostProcessor.postProcessBeforeInitialization(beanName,instance);
            }
            //初始化
            beanInitializing(instance);

            //初始化后
            for (BeanPostProcessor beanPostProcessor  : beanPostProcessorList) {
                beanPostProcessor.postProcessAfterInitialization(beanName,instance);
            }
            return instance;
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取bean
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException();
        }
        String scope = beanDefinition.getScope();
        if ("singleton".equals(scope)) {
            Object bean = singletonMap.get(beanName);
            if (bean == null) {
                Object o = createBean(beanName, beanDefinition);
                singletonMap.put(beanName, o);
                return o;
            }
            else {
                return bean;
            }
        }
        else {
            return createBean(beanName, beanDefinition);
        }


    }

    /*
     * 包扫描
     * */
    private void componentScan() {
        //判断包扫描注解是否存在
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            //获取到注解
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);

            //拿到value
            String originPath = componentScanAnnotation.value();//com.woniu.service的形式

            //获取到路径
            String path = originPath.replace(".", "/");
            //.class.getClassLoader().getResource()定位到项目根目录
            //.class.getResource()定位到项目目录
            //这些都是在编译后的out里
            URL resource = ApplicationContext.class.getClassLoader().getResource(path);

            //获取到文件夹
            assert resource != null;
            File scanFile = new File(resource.getFile());

            //解析目录下的class
            if (scanFile.isDirectory()) {
                for (File file : FileUtil.getAllFile(scanFile)) {
                    String fileName = file.getName();//UserService.class
                    //类加载器需要的格式com.woniu.service.UserService
                    // TODO 这里路径应该也是有bug，如果多层，没带上新的路径
                    if (fileName.endsWith(".class")) {
                        fileName = fileName.replace(".class", "");

                        //获取到包扫描下的类
                        Class<?> clazz = null;
                        try {
                            clazz = ApplicationContext.class.getClassLoader().loadClass(originPath + "." + fileName);
                            if (clazz.isAnnotationPresent(Component.class)) {
                                //发现bean对象

                                //Bean后置处理器
                                if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                    //clazz类实现了BeanPostProcessor接口
                                    BeanPostProcessor instance = (BeanPostProcessor) clazz.newInstance();
                                    beanPostProcessorList.add(instance);
                                }

                                //获取beanName
                                Component BeanAnnotation = clazz.getAnnotation(Component.class);
                                String beanName = BeanAnnotation.value();
                                if (beanName.equals("")) {
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());//按照bean的默认规则生产bean名字
                                }

                                //获取Scope
                                String scope = "singleton";
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    scope = clazz.getAnnotation(Scope.class).value();
                                }
                                //生成BeanDefinition
                                BeanDefinition beanDefinition = new BeanDefinition(clazz, scope);
                                beanDefinitionMap.put(beanName, beanDefinition);

                            }
                        }
                        catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /*
     * 依赖注入
     * TODO 依赖注入（简化版，实际上有bug，如果orderService不叫orderService就会报错）
     * */
    private void autoWired(Class clazz, Object instance) throws IllegalAccessException {
        for (Field f : clazz.getDeclaredFields()) {
            //获得某个类的所有声明的字段
            if (f.isAnnotationPresent(Autowired.class)) {
                //如果 accessible 标志被设置为true，那么反射对象在使用的时候，不会去检查Java语言权限控制（private之类的）
                f.setAccessible(true);

                f.set(instance, getBean(f.getName()));
            }

        }
    }

    /*
    * aware回调机制
    * 回调是告诉bean一个东西和后面初始化有点不一样
    * */
    private void beanNameAware(Object instance,String beanName){
        if(instance instanceof BeanNameAware){
            ((BeanNameAware)instance).setBeanName(beanName);
        }
    }

    /*
    * 初始化
    * */
    private void beanInitializing(Object instance){
        if(instance instanceof InitializingBean){
            ((InitializingBean)instance).afterPropertiesSet();
        }
    }
}
