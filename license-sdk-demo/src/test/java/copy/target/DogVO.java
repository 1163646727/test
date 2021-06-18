package copy.target;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DogVO {
    private String name;
    private List<String> labels;
}
