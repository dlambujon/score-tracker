package com.oyorooms.scoretracker.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oyorooms.scoretracker.model.Score;
import com.oyorooms.scoretracker.repository.ScoreRepository;
import com.oyorooms.scoretracker.service.ScoreService;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
class ScoreControllerUnitTest {

	@Autowired
	private ScoreService scoreService;

	@MockBean
	private ScoreRepository scoreRepository;

	@Test
	public void saveScoreTest(){
		Score score = new Score(1, 100, "TestPlayer", "2020-10-10 20:00:00");
		when(scoreRepository.save(score)).thenReturn(score);

		assertEquals(score, scoreService.saveScore(score));
	}

	@Test
	public void deleteScoreTest(){
		Score score = new Score(1, 100, "TestPlayer", "2020-10-10 20:00:00");

		when(scoreRepository.findById(score.getId())).thenReturn(Optional.of(score));

		scoreService.deleteScoreById(score.getId());
		verify(scoreRepository).deleteById(score.getId());
	}

	@Test
	public void getScoreByIdTest() {
		Score score = new Score(1, 100, "TestPlayer", "2020-10-10 20:00:00");

		when(scoreRepository.findById(score.getId())).thenReturn(Optional.of(score));

		Score result = scoreService.getScoreById(score.getId());

		assertEquals(score.getId(), result.getId());
	}

	@Test
	public void getScoresByDateRangeTest(){
		Score score1 = new Score(1, 100, "TestPlayer", "2020-10-10 20:00:00");
		Score score2 = new Score(2, 200, "TestPlayer", "2021-10-10 20:00:00");

		Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

		when(scoreRepository
				.findScoresByDateRange("2020-01-01", "2022-01-01", pageable))
				.thenReturn(Stream.of(score1, score2).collect(Collectors.toList()));

		List<Score> result = scoreService.getScoresByDateRange("2020-01-01", "2022-01-01", pageable);

		assertEquals(2, result.size());
	}

	@Test
	public void getScoresByNamesDateRangeTest(){
		Score score1 = new Score(1, 100, "TestPlayer", "2020-10-10 20:00:00");
		Score score2 = new Score(2, 200, "TestPlayer", "2021-10-10 20:00:00");

		Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

		List<String> nameListInput = new ArrayList<>();
		nameListInput.add("TestPlayer");
		when(scoreRepository
				.findScoresByNamesDateRange(nameListInput, "2020-01-01", "2022-01-01", pageable))
				.thenReturn(Stream.of(score1, score2).collect(Collectors.toList()));

		List<String> nameListResult = new ArrayList<>();
		nameListResult.add("TestPlayer");
		List<Score> result = scoreService.getScoresByNameListDateRange(nameListResult,
				"2020-01-01",
				"2022-01-01",
				pageable);

		assertEquals(2, result.size());
	}

	@Test
	public void getPlayerHistoryTest(){
		Score score1 = new Score(1, 100, "TestPlayer", "2020-10-10 20:00:00");
		Score score2 = new Score(2, 200, "TestPlayer", "2021-10-10 20:00:00");

		List<Score> scoreList = new ArrayList<>();
		scoreList.add(score1);
		scoreList.add(score2);

		when(scoreRepository.findByName("TestPlayer")).thenReturn(scoreList);
		when(scoreRepository.findTopByNameOrderByScoreDesc("TestPlayer")).thenReturn(score2);
		when(scoreRepository.findTopByNameOrderByScoreAsc("TestPlayer")).thenReturn(score1);
		when(scoreRepository.findAvgScoreByName("TestPlayer")).thenReturn((double) ((score1.getScore() + score2.getScore()) / scoreList.size()));

		Object result = scoreService.getPlayerHistory("TestPlayer");

		assertNotNull(result);
	}
}
