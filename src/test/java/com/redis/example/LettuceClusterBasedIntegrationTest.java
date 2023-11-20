/* (C) Games24x7 */
package com.redis.example;

import com.redis.RedisSpringConfig;
import com.redis.container.LettuceClusterTestContainer;
import com.redis.service.CacheService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@LettuceClusterTestContainer
// why not keeping the @ContextConfiguration inside the @LettuceClusterTestContainer
// because it will not be helpful when using integration Test with Multiple Sources
@ContextConfiguration(classes = RedisSpringConfig.class)
public class LettuceClusterBasedIntegrationTest {
    @Autowired
    CacheService cacheService;

    @Autowired
    RedisScript<List> testLua;

    @Test
    void testGetKey() {
        String dummyObject = "DummyObject";
        cacheService.set("A", dummyObject);
        Optional<String> a = cacheService.get("A", String.class);
        assertTrue(a.isPresent());
        assertEquals(dummyObject, a.get());
        cacheService.delete("A");
    }

    @Test
    @DisplayName("Testing Lua Script Which Returns Value in UPPERCASE for the Keys which starts with A, D or E")
    void testLuaScript() {
        ImmutableMap<Object, Object> expected =
                ImmutableMap.of(
                        "A1", "AObject", "B1", "BObject", "C1", "CObject", "D1", "DObject", "E1", "EObject");
        cacheService.putAll("A", expected);
        var value = cacheService.executeScript(testLua, List.of("A"));
        Map<Object, Object> result = cacheService.getAllMap("A");
        System.out.println(result);
        assertNotNull(value);
        assertEquals(6, value.size());
        assertEquals("A1", value.get(0));
        assertEquals("AOBJECT", value.get(1));
        assertEquals("D1", value.get(2));
        assertEquals("DOBJECT", value.get(3));
        assertEquals("E1", value.get(4));
        assertEquals("EOBJECT", value.get(5));
        cacheService.delete("A");
    }
}
