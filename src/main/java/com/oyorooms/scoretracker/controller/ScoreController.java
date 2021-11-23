package com.oyorooms.scoretracker.controller;

import java.util.HashMap;
import java.util.List;
import java.lang.Object;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oyorooms.scoretracker.model.Score;
import com.oyorooms.scoretracker.service.ScoreService;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {
	private ScoreService scoreService;

	public ScoreController(ScoreService scoreService) {
		super();
		this.scoreService = scoreService;
	}
	
	// build create score of player REST API
	@PostMapping()
	// http://localhost:8080/api/scores
 	public ResponseEntity<Score> saveScore(@RequestBody Score score) {
		return new ResponseEntity<Score>(scoreService.saveScore(score), HttpStatus.CREATED);
	}

	// build get score by id REST API
	// http://localhost:8080/api/scores/1
	@GetMapping("{id}")
	public ResponseEntity<Score> getScoreById(@PathVariable("id") long id) {
		return new ResponseEntity<Score>(scoreService.getScoreById(id), HttpStatus.OK);
	}
	
	// build get all scores REST API
	// http://localhost:8080/api/scores?name=player1,player2&afterDate=2020-01-01&beforeDate=2021-01-01
	@GetMapping
	@ResponseBody
	public ResponseEntity<List<Score>> getListOfScores(
		@RequestParam(required = false) List<String> name, 
		@RequestParam(required = false) String afterDate, 
		@RequestParam(required = false) String beforeDate,
		@RequestParam(required = false, defaultValue = "0") int pageNumber,
		@RequestParam(required = false, defaultValue = "10") int pageSize,
		@RequestParam(required = false, defaultValue = "id") String sortBy,
		@RequestParam(required = false, defaultValue = "asc") String sortDir
	) {
		Sort sort = (sortDir.equals("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		List<Score> scores = (name == null || name.size() == 0) ? 
			scoreService.getScoresByDateRange(afterDate, beforeDate, pageable) :
			scoreService.getScoresByNamesDateRange(name, afterDate, beforeDate, pageable);

		return new ResponseEntity<List<Score>>(scores, HttpStatus.OK);
	}

	// build get all scores REST API
	// http://localhost:8080/api/scores/history?name=player1
	@GetMapping("/history")
	@ResponseBody
	public ResponseEntity<HashMap<String, Object>> getPlayerHistory(@RequestParam String name) {
		return new ResponseEntity<HashMap<String, Object>>(scoreService.getPlayerHistory(name), HttpStatus.OK);
	}	

	// build delete score REST API
	// http://localhost:8080/api/scores/1
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteScore(@PathVariable("id") long id){
		scoreService.deleteScoreById(id);
		
		return new ResponseEntity<String>(String.format("Score with id=%s was deleted successfully!.", id), HttpStatus.OK);
	}
	
}
