package com.letschores.demo;

import com.letschores.demo.model.Chore;
import com.letschores.demo.service.ChoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DemoApplicationTests {
	private ChoreService choreService;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	public void setUp(){
		choreService=new ChoreService(null,null,null);
	}

	@Test
	public void testGetValueWithEasyDifficulty() {
		Chore chore = new Chore();
		chore.setDifficulty("EASY");
		chore.setDuration(5);
		int expectedValue = 5 * 1;
		int actualValue = choreService.getValue(chore);
		assertEquals(expectedValue, actualValue);
	}

	@Test
	public void testGetValueWithMediumDifficulty() {
		Chore chore = new Chore();
		chore.setDifficulty("MEDIUM");
		chore.setDuration(5);
		int expectedValue = 5 * 2;
		int actualValue = choreService.getValue(chore);
		assertEquals(expectedValue, actualValue);
	}

	@Test
	public void testGetValueWithHardDifficulty() {
		Chore chore = new Chore();
		chore.setDifficulty("HARD");
		chore.setDuration(5);
		int expectedValue = 5 * 3;
		int actualValue = choreService.getValue(chore);
		assertEquals(expectedValue, actualValue);
	}



}
