package tools;


import java.util.Scanner;

public class MarkDownTest {
    public static void main(String[] args) {
        int rows = 4;
        int cols = 3;
        String head = tableHead(cols);
        String style = tableStyle(cols);
        //打印表格内容：
        String body = tableBody(cols, rows);
//      System.out.println("----------------------------------------");
        System.out.println(head + style + body);
        //把生成的markdown代码写入到系统剪贴板中
        SysClipboardUtil.setSysClipboardText(head + style + body);
    }

    /**
     * @param cols
     * @return
     */
    public static String tableHead(int cols) {
        StringBuilder buf = new StringBuilder();
        //打印表格头
        for (int j = 1; j <= cols; j++) {

//          System.out.print("|标题"+j);
            buf.append("|标题" + j);
        }
//      System.out.println("|");//标题行结束
        buf.append("|\n");
        return buf.toString();
    }

    /**
     * 生成默认格式的markdown表格对齐样式。
     *
     * @param cols 表格的列数
     * @return markdown表格对齐样式语句字符串。
     */
    public static String tableStyle(int cols) {
        StringBuilder buf = new StringBuilder();
        //打印表格样式使用默认
        for (int j = 1; j <= cols; j++) {
//          System.out.print("|-");
            buf.append("|-");
        }
//      System.out.println("|");//标题行结束
        buf.append("|\n");
        return buf.toString();
    }

    /**
     * 生成表格体markdown语句。
     *
     * @param cols 表格的列数
     * @param rows 表格的行数
     * @return 带编号的表格体markdown语句，表格体第一行编号1,第二行编号2.
     */
    public static String tableBody(int cols, int rows) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 1; j <= cols + 1; j++) {
//              System.out.print("|");
                buf.append("|");
                if (j == 1) {
//                  System.out.print(i+1);
                    buf.append((i + 1));
                }

            }
//          System.out.println();
            buf.append("\n");
        }
        return buf.toString();
    }
}
