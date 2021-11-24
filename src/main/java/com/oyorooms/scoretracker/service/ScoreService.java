package com.oyorooms.scoretracker.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.oyorooms.scoretracker.model.Score;

public interface ScoreService {
	Score saveScore(Score score);

	void deleteScoreById(long id);

	Score getScoreById(long id);
		
	List<Score> getScoresByDateRange(String afterDate, String beforeDate, Pageable pageable);
	
	List<Score> getScoresByNameListDateRange(List<String> nameList, String afterDate, String beforeDate, Pageable pageable);

	HashMap<String, Object> getPlayerHistory(String name);
}
