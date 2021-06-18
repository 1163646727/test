package annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;

@MyAnnotation(value = "aa", alias = "bb")
public class AnnotationTest {

    @Test
    public void testAliasForJDK() {
        MyAnnotation ann = getClass().getAnnotation(MyAnnotation.class);
        System.out.println(ann.value());
        System.out.println(ann.alias());
        //反射修改注解值 动态代理处理注解中的值，在调用方法时
    }

    @Test
    public void testAliasForSpring() {
        MyAnnotation ann = AnnotationUtils.findAnnotation(getClass(),
                MyAnnotation.class);
        System.out.println(ann.value());
        System.out.println(ann.alias());
    }

    //无法映射别名
    @Test
    public void testAliasForHutool() {
        Object annotationValue = AnnotationUtil.getAnnotationValue(AnnotationTest.class, MyAnnotation.class);
        System.out.println(annotationValue);
    }

}
