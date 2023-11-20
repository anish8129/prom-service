package com.prometheus;

import com.custom.endpoint.EndpointConfig;
import com.games24x7.RestCoreUtil;
import com.kafka.KafkaSpringConfig;
import com.micrometer.MicroMeterConfigs;
import com.monitoring.MonitoringConfig;
import com.redis.RedisSpringConfig;
import com.redis.config.RedisClientConfig;
import com.redis.config.jedis.JedisDefaultClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
		MonitoringConfig.class,
		RestCoreUtil.class,
		MicroMeterConfigs.class,
		EndpointConfig.class,
		RedisSpringConfig.class,
		KafkaSpringConfig.class
})

public class PrometheusApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrometheusApplication.class, args);
	}


	@Bean
	RedisClientConfig redisClientConfig() {
		return new JedisDefaultClientConfig("127.0.0.1", 6379);
	}
}
