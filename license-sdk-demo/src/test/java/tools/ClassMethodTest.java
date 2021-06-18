package tools;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassMethodTest {
    @Test
    public void getMethods() throws Exception {
//        String[] packageName = {"com.example.demo.controller"};
        String[] packageName = {"com.example.demo.controller"};
        //写入excel
        List<List<String>> rows = CollUtil.newArrayList();
        readClassMethod(rows, packageName);
        ExcelWriter writer = ExcelUtil.getWriter("D:/bak/service.xlsx");
        writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        writer.writeRow(CollUtil.newArrayList("service方法列表"), true);
        writer.write(rows, true);
        writer.close();
    }

    private void readClassMethod(List<List<String>> rows, String[] packageNames) {
        //查询某个包下的所有类
        for (String packageName : packageNames) {
            //模块、service接口、方法
            String module = packageName;
            List<String> classNames = getClassesByPackageName(packageName);
            classNames.stream().forEach(className -> {
                if (className.contains("interface")) {
                    return; //continue
                }
                //service
                rows.add(CollUtil.newArrayList("---接口---" + className + "-------"));
                Class clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Method[] declaredMethods = clazz.getDeclaredMethods();
                //方法
                for (Method method : declaredMethods) {
                    StringBuilder builder = new StringBuilder();
                    int modifiers = method.getModifiers();
                    String modifierName = getModify(modifiers);
                    String name = method.getName();
                    Class<?> returnType = method.getReturnType();
                    String returnName = returnType.getSimpleName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    builder.append(modifierName).append(" ");
                    builder.append(returnName).append(" ");
                    builder.append(name).append("(");
                    for (Class cl : parameterTypes) {
                        builder.append(cl.getName()).append(",");
                    }
                    if (builder.lastIndexOf(",") > 0) {
                        builder = builder.replace(builder.lastIndexOf(","), builder.length(), "");
                    }
                    builder.append(")");
                    rows.add(CollUtil.newArrayList(builder.toString()));
                }
            });
        }
    }

    public String getModify(int m2) {
        if (Modifier.isPublic(m2)) {
            return "public";
        }
        if (Modifier.isProtected(m2)) {
            return "protect";
        }
        if (Modifier.isPrivate(m2)) {
            return "private";
        }
        return "";
    }

    public List<String> getClassesByPackageName(String packageName) {
        List<String> list = new ArrayList<>();
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代下去
            while (dirs.hasMoreElements()) {
                //获取下一个元素
                URL url = dirs.nextElement();
                //得到协议的名称
                String protocol = url.getProtocol();
                //如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //以文件的方式扫描整个包下的文件 并添加到集合中
                    File dir = new File(filePath);
                    //
                    getFiles(dir, list);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void getFiles(File file, List<String> list) {
        if (!file.isDirectory()) {
            String className = file.getName().substring(0, file.getName().length() - 6);
            String packageName = file.getPath();
            packageName = packageName.substring(packageName.indexOf("\\classes\\") + 9);
            packageName = packageName.replace(".class", "");
            if (packageName.contains("interface")) {
                return; //continue
            }
            list.add(packageName.replace("\\", "."));
            return;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            getFiles(f, list);
        }
    }

    @Test
    public void test001() {
        List<List<String>> rows = CollUtil.newArrayList();
        rows.add(CollUtil.newArrayList("aa1","aa2","aa3","aa4","aa5"));
        rows.add(CollUtil.newArrayList("aa1","aa2","aa3","aa4","aa5"));
        rows.add(CollUtil.newArrayList("aa1","aa3","aa3","aa4","aa5"));
        rows.add(CollUtil.newArrayList("aa6","aa2","aa3","aa4","aa5"));
        rows.add(CollUtil.newArrayList("aa6","aa2","aa3","aa4","aa5"));
        rows.add(CollUtil.newArrayList("aa7","aa2","aa3","aa4","aa5"));
//        rows.add(CollUtil.newArrayList("aa7","aa2","aa3","aa4","aa5"));
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("d:/bak/writeTest.xlsx");
//通过构造方法创建writer
//ExcelWriter writer = new ExcelWriter("d:/writeTest.xls");
        writer.merge(0,2,0,0,null,true);
        writer.merge(0,1,1,1,null,true);
        writer.merge(3,4,0,0,null,true);
        writer.merge(3,4,1,1,null,true);

//        writer.merge(5,5,0,0,null,true);

        //跳过当前行，既第一行，非必须，在此演示用
//        writer.passCurrentRow();
//合并单元格后的标题行，使用默认标题样式
//一次性写出内容，强制输出标题
        writer.write(rows, true);
//关闭writer，释放内存
        writer.close();
    }

}
