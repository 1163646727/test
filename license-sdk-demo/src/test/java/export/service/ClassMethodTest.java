package export.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Font;
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
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ClassMethodTest {
    @Test
    public void getMethods() throws Exception {
//        String[] packageName = {"com.example.demo.controller"};
        String[] packageName = {
                "com.bocloud.paas.license.server.core.service"
        };
        //写入excel
        List<RowData> rows = CollUtil.newArrayList();
        ExcelWriter writer = ExcelUtil.getWriter("D:/bak/license-server-service.xlsx");
        boolean isCompressName = false;
        //数据收集及单元格合并
        readClassMethod(rows, packageName, writer, isCompressName);
        //样式
        writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        writer.merge(0, 0, 0, 2, null, true);
        Font font = writer.createFont();
        font.setBold(true);
        writer.getStyleSet().getHeadCellStyle().setFont(font);
        //大标题
        writer.writeRow(CollUtil.newArrayList("license-server service列表"), true);
        //列标题
        writer.addHeaderAlias("module", "模块");
        writer.addHeaderAlias("service", "service名称");
        writer.addHeaderAlias("method", "方法名称");
        //批量写出
        writer.write(rows, true);
        writer.close();
    }

    private void readClassMethod(List<RowData> rows, String[] packageNames, ExcelWriter writer, boolean isCompressName) {
        //查询某个包下的所有类
        AtomicInteger indexRow = new AtomicInteger(2);
        for (String packageName : packageNames) {
            List<String> classNames = getClassesByPackageName(packageName);
            AtomicInteger moduleTotal = new AtomicInteger(0); //模块中方法个数
            classNames.stream().forEach(className -> {
                AtomicInteger smTotal = new AtomicInteger(0); //service个数
                Class clazz = null;
                try {
                    clazz = Class.forName(className);
                    //判断类型
                    if (clazz.isInterface()) {
                        return;
                    }
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
                    rows.add(new RowData().setModule(pname).setService(className).setMethod(builder.toString()));
                }
                //合并service,行数=该方法的service个数
                int begin = indexRow.get() - smTotal.get();
//                log.info("fr:{},lr:{},cf:{},cl:{}", begin, begin + smTotal.get() - 1, 1, 1);
                //判断个数 <=1 不合并
                if (smTotal.get() <= 1) {
                    return;
                }
                writer.merge(begin, begin + smTotal.get() - 1, 1, 1, null, true);
            });
            //合并模块，行数=方法个数
            int begin = indexRow.get() - moduleTotal.get();
//            log.info("fr:{},lr:{},cf:{},cl:{}", begin, begin + moduleTotal.get() - 1, 0, 0);
            if (moduleTotal.get() <= 1) {
                continue;
            }
            writer.merge(begin, begin + moduleTotal.get() - 1, 0, 0, null, true);

        }
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

}
