/* (C) Games24x7 */
package com.redis.container;

import com.redis.RedisSpringConfig;
import com.redis.container.cluster.RedisClusterTestContainer;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TestPropertySource(properties = {"client.type=LETTUCE", "redis.mode=CLUSTER"})
@Import(RedisClusterTestContainer.class)
public @interface LettuceClusterTestContainer {
}
