package copy.source;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Dog {
    private String name;
    private List<String> labels;
}
