package copy.target;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserVO {
    private String name;
    private List<DogVO> dogList;
}
