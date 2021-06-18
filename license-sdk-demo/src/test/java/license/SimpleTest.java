package license;


import org.junit.Test;

import java.util.Arrays;

public class SimpleTest {
    @Test
    public void test001() {
        String[] edition = {"a", "b"};
        String licenseEdition = "b";
        System.out.println(Arrays.stream(edition).anyMatch(s -> s.equals(licenseEdition)));

    }
}
