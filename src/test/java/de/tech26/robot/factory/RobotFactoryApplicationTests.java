package de.tech26.robot.factory;

import de.tech26.robot.factory.controller.RobotFactoryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RobotFactoryApplicationTests {

	@Autowired
	private RobotFactoryController controller;

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
}
