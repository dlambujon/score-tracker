package com.oyorooms.scoretracker.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oyorooms.scoretracker.model.Score;

public interface ScoreRepository extends JpaRepository<Score, Long>{
	@Query(nativeQuery = true, value = "SELECT id, name, MAX(score) as score, time FROM Scores WHERE name=?1")
	Score findMaxScoreByName(String name);

	@Query(nativeQuery = true, value = "SELECT id, name, MIN(score) as score, time FROM Scores WHERE name=?1")
	Score findMinScoreByName(String name);

	@Query("SELECT AVG(score) FROM Score WHERE name=?1")
	double findAvgScoreByName(String name);

	@Query("FROM Score WHERE (?1 IS NULL OR time > ?1) AND (?2 is null OR time < ?2)")
	List<Score> getScoresByDateRange(String afterDate, String beforeDate, Pageable pageable);

	@Query("FROM Score WHERE name IN (?1) AND (?2 IS NULL OR time > ?2) AND (?3 is null OR time < ?3)")
	List<Score> getScoresByNamesDateRange(List<String> nameList, String afterDate, String beforeDate, Pageable pageable);

	List<Score> findByName(String name);
}
