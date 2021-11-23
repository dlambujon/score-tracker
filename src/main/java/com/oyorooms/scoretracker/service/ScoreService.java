package com.oyorooms.scoretracker.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.oyorooms.scoretracker.model.Score;

public interface ScoreService {
	Score saveScore(Score score);

	Score getScoreById(long id);
		
	List<Score> getScoresByDateRange(String afterDate, String beforeDate, Pageable pageable);
	
	List<Score> getScoresByNamesDateRange(List<String> nameList, String afterDate, String beforeDate, Pageable pageable);

	HashMap<String, Object> getPlayerHistory(String name);

	void deleteScoreById(long id);
}
