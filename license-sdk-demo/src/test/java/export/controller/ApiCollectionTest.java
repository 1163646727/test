/*
package export.controller;//package export.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ApiCollectionTest {
    @Test
    public void getMethods() throws Exception {
        String[] packageName = {
                "com.bocloud.paas.shared.alert.provider.controller",
                "com.bocloud.paas.shared.audit.controller",
                "com.bocloud.paas.shared.message.provider.controller",
                "com.bocloud.paas.shared.other.provider.controller",
        };
        //写入excel
        List<Column> rows = CollUtil.newArrayList();
        ExcelWriter writer = ExcelUtil.getWriter("D:/bak/paas-shared-controller.xlsx");
        boolean isCompressName = false;
        //数据收集及单元格合并
        readClassMethod(rows, packageName, writer, isCompressName);
        //样式
        writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        writer.merge(0, 0, 0, 5, null, true);
        Font font = writer.createFont();
        font.setBold(true);
        writer.getStyleSet().getHeadCellStyle().setFont(font);
        //大标题
        writer.writeRow(CollUtil.newArrayList("license-server controller API列表"), true);
        //列标题
        writer.addHeaderAlias("id", "序号");
        writer.addHeaderAlias("desc", "名称");
        writer.addHeaderAlias("requestMethod", "请求方式");
        writer.addHeaderAlias("url", "请求地址");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("module", "模块");
        writer.addHeaderAlias("className", "类路径");
        writer.addHeaderAlias("method", "方法名称");
        //批量写出
        writer.write(rows, true);
        writer.close();
    }

    private void readClassMethod(List<Column> rows, String[] packageNames, ExcelWriter writer, boolean isCompressName) {
        //查询某个包下的所有类
        AtomicInteger indexRow = new AtomicInteger(2);
        AtomicInteger count = new AtomicInteger(0);
        for (String packageName : packageNames) {
            List<String> classNames = getClassesByPackageName(packageName);
            AtomicReference<String> requestMapping = new AtomicReference<>("");
            AtomicInteger moduleTotal = new AtomicInteger(0); //模块中方法个数
            classNames.stream().forEach(className -> {
                AtomicInteger smTotal = new AtomicInteger(0); //service个数
                Class clazz = null;
                try {
                    clazz = Class.forName(className);
                    RequestMapping annotation = AnnotationUtils.getAnnotation(clazz, RequestMapping.class);
                    if (null != annotation) {
                        requestMapping.set(StringUtils.join(annotation.value(), ","));
                    }
                    //判断类型
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method method : declaredMethods) {
                    StringBuilder builder = new StringBuilder();
                    int modifiers = method.getModifiers();
                    String modifierName = getModify(modifiers);
                    if (modifierName.equals("private")) {
                        continue;
                    }
                    moduleTotal.getAndIncrement(); //模块方法个数
                    smTotal.getAndIncrement(); //service方法个数
                    indexRow.getAndIncrement(); //行号
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
                    String pname = packageName;
                    if (isCompressName) {
                        pname = getCompressName(packageName);
                        className = getCompressName(className);
                    }
                    String url = "";
                    String requestMethod = "";
                    //方法请求地址
                    String[] requestMethod1 = getRequestMethod(method);
                    requestMethod = requestMethod1[0];
                    url = requestMethod1[1];
                    //描述
                    ApiOperation descAnno = method.getAnnotation(ApiOperation.class);
                    String desc = "";
                    if (null != descAnno) {
                        desc = descAnno.value();
                    }
                    rows.add(new Column()
                            .setId(count.incrementAndGet())
                            .setModule(pname)
                            .setClassName(className)
                            .setMethod(builder.toString())
                            .setRequestMethod(requestMethod)
                            .setUrl(requestMapping.get() + url)
                            .setDesc(desc)
                    );
                }
                //合并service,行数=该方法的service个数
                int begin = indexRow.get() - smTotal.get();
//                log.info("fr:{},lr:{},cf:{},cl:{}", begin, begin + smTotal.get() - 1, 1, 1);
                //判断个数 <=1 不合并
                if (smTotal.get() <= 1) {
                    return;
                }
                writer.merge(begin, begin + smTotal.get() - 1, 6, 6, null, true);
            });
            //合并模块，行数=方法个数
            int begin = indexRow.get() - moduleTotal.get();
//            log.info("fr:{},lr:{},cf:{},cl:{}", begin, begin + moduleTotal.get() - 1, 0, 0);
            if (moduleTotal.get() <= 1) {
                continue;
            }
            writer.merge(begin, begin + moduleTotal.get() - 1, 5, 5, null, true);

        }
    }

    private String[] getRequestMethod(Method method) {
        if (null != method.getAnnotation(GetMapping.class)) {
            return new String[]{"GET", StringUtils.join(method.getAnnotation(GetMapping.class).value(), ",")};
        }
        if (null != method.getAnnotation(PostMapping.class)) {
            return new String[]{"POST", StringUtils.join(method.getAnnotation(PostMapping.class).value(), ",")};
        }
        if (null != method.getAnnotation(PutMapping.class)) {
            return new String[]{"PUT", StringUtils.join(method.getAnnotation(PutMapping.class).value(), ",")};
        }
        if (null != method.getAnnotation(DeleteMapping.class)) {
            return new String[]{"DELETE", StringUtils.join(method.getAnnotation(DeleteMapping.class).value(), ",")};
        }
        if (null != method.getAnnotation(RequestMapping.class)) {
            return new String[]{"ALL", StringUtils.join(method.getAnnotation(RequestMapping.class).value(), ",")};
        }
        return new String[]{"", ""};
    }

    private String getCompressName(String packageName) {
        //提取包名首字母
        String[] split = packageName.split("\\.");
        StringBuilder builder = new StringBuilder();
        int len = split.length;
        for (int i = 0; i < len; i++) {
            String str = split[i];
            if (i < len - 1) {
                str = str.substring(0, 1);
            }
            builder.append(str).append(".");
        }
        return builder.substring(0, builder.length() - 1);
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
            list.add(packageName.replace("\\", "."));
            return;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            getFiles(f, list);
        }
    }

    @Test
    public void test002() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.bocloud.paas.license.mgr.controller.KeystoreHistoryController");
        RequestMapping annotation = aClass.getAnnotation(RequestMapping.class);
        if (null != annotation && annotation instanceof RequestMapping) {
            System.out.println("ALL");
        }
    }

}
*/
