package com.oyorooms.scoretracker.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.oyorooms.scoretracker.exception.ResourceNotFoundException;
import com.oyorooms.scoretracker.model.Score;
import com.oyorooms.scoretracker.repository.ScoreRepository;
import com.oyorooms.scoretracker.service.ScoreService;

@Service
public class ScoreServiceImpl implements ScoreService {
	private final ScoreRepository scoreRepository;

	public ScoreServiceImpl(ScoreRepository scoreRepository) {
		super();
		this.scoreRepository = scoreRepository;
	}

	@Override
	public Score saveScore(Score score) {
		return scoreRepository.save(score);
	}

	@Override
	public void deleteScoreById(long id) {
		scoreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Score", "Id", id));
		scoreRepository.deleteById(id);
	}

	@Override
	public Score getScoreById(long id) {
		return scoreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Score", "Id", id));
	}

	@Override
	public List<Score> getScoresByDateRange(String afterDate, String beforeDate, Pageable pageable) {
		return scoreRepository.findScoresByDateRange(afterDate, beforeDate, pageable);
	}

	@Override
	public List<Score> getScoresByNameListDateRange(List<String> nameList, String afterDate, String beforeDate, Pageable pageable) {
		return scoreRepository.findScoresByNamesDateRange(nameList, afterDate, beforeDate, pageable);
	}

	@Override
	public HashMap<String, Object> getPlayerHistory(String name) {
		List<Score> scoresByName = scoreRepository.findByName(name);
		if (scoresByName.isEmpty()) {
			throw new ResourceNotFoundException("Score", "Name", name);
		}

		// remove id and name
		List<HashMap<String, Object>> newScoresByName = new ArrayList<>();
		for (Score score: scoresByName) {
			HashMap<String, Object> newScore = new HashMap<>();
			newScore.put("score", score.getScore());
			newScore.put("time", score.getTime());
			newScoresByName.add(newScore);
		}

		// remove id and name
		HashMap<String, Object> hashedMaxScore = new HashMap<>();
		Score maxScore = scoreRepository.findTopByNameOrderByScoreDesc(name);
		hashedMaxScore.put("score", maxScore.getScore());
		hashedMaxScore.put("time", maxScore.getTime());

		// remove id and name
		HashMap<String, Object> hashedMinScore = new HashMap<>();
		Score minScore = scoreRepository.findTopByNameOrderByScoreAsc(name);
		hashedMinScore.put("score", minScore.getScore());
		hashedMinScore.put("time", minScore.getTime());

		HashMap<String, Object> playerHistory = new HashMap<>();
		playerHistory.put("topScore", hashedMaxScore);
		playerHistory.put("lowestScore", hashedMinScore);
		playerHistory.put("averageScore", scoreRepository.findAvgScoreByName(name));
		playerHistory.put("scoreList", newScoresByName);

		return playerHistory;
	}
}
