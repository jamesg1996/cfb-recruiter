package io.github.jamesg1996.cfbrecruiter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class CfbRecruiterApplicationTests {
	@Test
	void contextLoads() {
	}

}
