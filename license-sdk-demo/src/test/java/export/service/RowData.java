package export.service;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RowData {
    private String module;
    private String service;
    private String method;
}
