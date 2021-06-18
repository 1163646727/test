package copy.source;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class User {
    private String name;
    private List<Dog> dogList;
}
