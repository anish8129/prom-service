package com.prometheus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PrometheusApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void context() {
		String formatted = String.format("student:%d", 1);
		System.out.println("The oramtted String is " + formatted);
	}
}
