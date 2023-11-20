/* (C) Games24x7 */
package com.funnel;

import com.funnel.configreader.YamlPropertySourceFactory;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class YamlPropertySourceFactoryTest {

    @Test
    void testYml() {
        GamePlayFunnel gamePlayFunnel = YamlPropertySourceFactory.readFile(new ClassPathResource("test.yaml"));
        System.out.println(gamePlayFunnel);
    }
}
