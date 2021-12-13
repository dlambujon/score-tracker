package com.oyorooms.scoretracker.controller;

import com.oyorooms.scoretracker.model.Score;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@Sql({ "classpath:init-data.sql" })
class ScoreControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String URL = "/api/scores/";

    @Test
    public void createScore() {
        Score score = new Score(1, 1000, "TestPlayer", "2021-01-01 10:00:00");
        ResponseEntity<Score> responseEntity = restTemplate.postForEntity(URL, score, Score.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void deleteScoreById() {
        ResponseEntity<?> responseEntity = restTemplate.exchange(URL + "1", HttpMethod.DELETE,
                new HttpEntity<>(""),
                String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getScoreById() {
        ResponseEntity<Score> responseEntity = restTemplate.exchange(URL + "1",
                HttpMethod.GET,
                new HttpEntity<>(""),
                Score.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getListOfScores() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL,
                HttpMethod.GET,
                new HttpEntity<>(""),
                String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getListOfScoresWithParameters() {
        String parameterString = "?name=TestPlayer,TestPlayer2&afterDate=2020-01-01&beforeDate=2022-01-01&pageNumber=2&pageSize=5&sortBy=name&sortDir=desc";
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL + parameterString,
                HttpMethod.GET,
                new HttpEntity<>(""),
                String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getPlayerHistory() {
        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL + "history?name=TestPlayer",
                HttpMethod.GET,
                new HttpEntity<>(""),
                Object.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
