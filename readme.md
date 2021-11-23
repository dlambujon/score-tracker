# Score Tracker Spring Boot

===================================================
## Purpose
This is an API which keep scores for a certain game for a group of players. See available queries below.

## Prerequisites

* Java 8
* Gradle 7.2
* Spring Boot 2.6.0
* JUnit 5
* IntelliJ

Clone
--------

```sh
git clone https://github.com/dlambujon/score-tracker.git
```

### Create Score

```
http://localhost:8080/api/scores
```

JSON _Request_:

```json
{
  "name" : "Player1",
  "score" : 1000,
  "time" : "2020-01-01 10:00:00"
}
```
JSON _Response_:

```json
{
  "id": 1,
  "score": 1000,
  "name": "Player1",
  "time" : "2020-01-01 10:00:00"
}
```

### Get Score 

```
http://localhost:8080/api/scores/1
```

JSON Response:

```json
{
  "id": 1,
  "score": 1000,
  "name": "Player1",
  "time" : "2020-01-01 10:00:00"
}
```

### Delete Score

```
http://localhost:8080/api/scores/1
```

Response Message:

```json
"Score with id=1 was deleted successfully!."
```

### Get list of all scores

```
http://localhost:8080/api/scores
```

JSON Response:

```json
[
  {
    "id": 1,
    "score": 1000,
    "name": "Player1",
    "time": "2019-01-01 10:00:00"
  },
  {
    "id": 2,
    "score": 1000,
    "name": "Player1",
    "time": "2020-10-01 10:00:00"
  },
  {
    "id": 3,
    "score": 1000,
    "name": "Player2",
    "time": "2021-01-01 10:00:00"
  }
]
```
Other possible queries
```
Get all scores by playerX
Get all score after 1st November 2020
Get all scores by player1, player2 and player3 before 1st december 2020
Get all scores after 1 Jan 2020 and before 1 Jan 2021
(Pagination and sorting also available)
```

### Get players' history

```
http://localhost:8080/api/scores/history?name=Player1
```

JSON Response:

```json
{
  "scoreList": [
    {
      "id": 1,
      "score": 1000,
      "name": "Player1",
      "time": "2019-01-01 10:00:00"
    },
    {
      "id": 2,
      "score": 1000,
      "name": "Player1",
      "time": "2020-01-01 10:00:00"
    }
  ],
  "topScore": [
    1000,
    "2019-01-01 10:00:00"
  ],
  "lowestScore": [
    1000,
    "2019-01-01 10:00:00"
  ],
  "averageScore": 1000.0
}
```
