package copy;


import com.alibaba.fastjson.JSON;
import copy.source.Dog;
import copy.source.User;
import copy.target.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CopyTest {
    @Test
    public void testSpringBC() {
        List<Dog> dogs = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        labels.add("1");
        labels.add("2");
        dogs.add(new Dog().setName("Dog1").setLabels(labels));
        dogs.add(new Dog().setName("Dog2").setLabels(labels));
        User user = new User().setName("用户1").setDogList(dogs);
        UserVO uv = new UserVO();
        BeanUtils.copyProperties(user,uv);
        log.info(JSON.toJSONString(uv));
    }
}
