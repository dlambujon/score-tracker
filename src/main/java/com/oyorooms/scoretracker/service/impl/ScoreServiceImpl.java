package com.oyorooms.scoretracker.service.impl;

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
	private ScoreRepository scoreRepository;

	public ScoreServiceImpl(ScoreRepository scoreRepository) {
		super();
		this.scoreRepository = scoreRepository;
	}

	@Override
	public Score saveScore(Score score) {
		return scoreRepository.save(score);
	}

	@Override
	public Score getScoreById(long id) {
		return scoreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Score", "Id", id));
	}

	@Override
	public List<Score> getScoresByDateRange(String afterDate, String beforeDate, Pageable pageable) {
		return scoreRepository.getScoresByDateRange(afterDate, beforeDate, pageable);
	}

	@Override
	public List<Score> getScoresByNamesDateRange(List<String> nameList, String afterDate, String beforeDate,
			Pageable pageable) {
		return scoreRepository.getScoresByNamesDateRange(nameList, afterDate, beforeDate, pageable);
	}

	@Override
	public HashMap<String, Object> getPlayerHistory(String name) {
		List<Score> scoresByName = scoreRepository.findByName(name);
		if (scoresByName.isEmpty()) {
			throw new ResourceNotFoundException("Score", "Name", name);
		}

		int topScore = 0;
		int lowestScore = 0;
		int averageScore = 0;
		for (Score score : scoresByName) {
			if (score.getScore() > topScore) {
				topScore = score.getScore();
			}
			if (score.getScore() < lowestScore) {
				lowestScore = score.getScore();
			}
		}
		System.out.println(topScore);
		System.out.println(lowestScore);

		HashMap<String, Object> playerHistory = new HashMap<String, Object>();
		playerHistory.put("topScore", scoreRepository.findMaxScoreByName(name));
		playerHistory.put("lowestScore", scoreRepository.findMinScoreByName(name));
		playerHistory.put("averageScore", scoreRepository.findAvgScoreByName(name));
		playerHistory.put("scoreList", scoresByName);

		return playerHistory;
	}

	@Override
	public void deleteScoreById(long id) {
		scoreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player", "Id", id));
		scoreRepository.deleteById(id);
	}
}
