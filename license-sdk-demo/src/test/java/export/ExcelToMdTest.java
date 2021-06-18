package export;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelToMdTest {
    private final String BR = "\r\n";
    private final String SEPERATE = "|";

    @Test
    public void test() throws IOException {
        String filePath = "D:\\bak\\paas-shared-controller.xlsx";
        ExcelReader reader = ExcelUtil.getReader(new File(filePath));
        List<List<Object>> read = reader.read();
        System.out.println(JSON.toJSONString(read.get(0)));

        String newPath = filePath.replace(".xlsx", ".txt");
        FileWriter writer = new FileWriter(newPath);

        //输出标题
        List<Object> header = read.get(0);
        String headerStr = header.stream().map(String::valueOf).collect(Collectors.joining(SEPERATE));
        writer.write(SEPERATE + headerStr + SEPERATE + BR);
        //
        headerStr = header.stream().map(str -> "---").collect(Collectors.joining(SEPERATE));
        writer.write(SEPERATE + headerStr + SEPERATE + BR);
        for (int i = 1; i < read.size(); i++) {
            List<Object> rows = read.get(i);
            writer.write(SEPERATE + rows.stream().map(String::valueOf).collect(Collectors.joining(SEPERATE)) + "|" + BR);
        }
        writer.close();
    }

    @Test
    public void test002() {
        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        System.out.println(data.stream().map(a -> "---").collect(Collectors.joining("|")));
    }
}
