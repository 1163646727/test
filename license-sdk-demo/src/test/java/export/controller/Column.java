package export.controller;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Column {
    private int  id; //序号
    private String desc; //描述
    private String requestMethod; //请法方式
    private String url; //请求地址
    private String remark; //备注
    private String module;//模块
    private String className; //请求地址
    private String method; //方法名称
}
