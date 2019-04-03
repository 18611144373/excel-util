package com.angla.plugins.excel.analysis.anno;

import com.angla.plugins.excel.analysis.anno.interfaces.FieldTranform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: excel-util
 * @description: bean中添加的基本注解，想要解析成bean必须添加此注解
 * @author: angla
 * @create: 2018-08-02 15:11
 * @Version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AnalysisField {
    String  name() default "";                              //中文名字
    boolean must() default false;                           //是否必填
    String regex() default "";                              //自定义正则
    int[] uniNos() default {};                              //是否唯一， 如果唯一的话 则其默认must
    Class transform() default FieldTranform.class;          //定义excel中的转换格式
}