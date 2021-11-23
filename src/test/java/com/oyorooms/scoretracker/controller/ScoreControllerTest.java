package com.oyorooms.scoretracker.controller;

import com.oyorooms.scoretracker.model.Score;
import com.oyorooms.scoretracker.repository.ScoreRepository;
import com.oyorooms.scoretracker.service.ScoreService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ScoreControllerTest {

	@Autowired
	private ScoreService scoreService;

	@MockBean
	private ScoreRepository scoreRepository;

	@Test
	@Order(1)
	public void saveScoreTest(){
		Score score = new Score(1, 100, "Player1", "2020-10-10 20:00:00");
		scoreRepository.save(score);

		Assertions.assertNotEquals(score.getId(), 0);

	}

	@Test
	@Order(2)
	public void getScoreByIdTest() {
		Score score = scoreRepository.getById(1L);

		Assertions.assertEquals(score.getId(), 1L);
	}

	@Test
	@Order(4)
	public void deleteScoreTest(){
		scoreService.deleteScoreById(1);
		verify(scoreRepository, times(1)).deleteById(1L);
	}
}
