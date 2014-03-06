Bowling
=======
Live Score Bowling Rest API

Create a bowling game
curl -v -X POST http://localhost:8080/Bowling/game 
Location Header as well as the response stream will have the new bowling game uri and id 

Update an existing bowling game
curl -v -X PUT -d @data http://localhost:8080/Bowling/game/{id}

Sample PUT data: 
{"bowlingGameId":0,"playersScoresMap":{"ash":{"1":[10],"2":[10],"3":[7,3],"4":[8,2],"5":[10],"6":[9,1],"7":[10],"8":[10],"9":[10],"10":[10,7,3]},"nick":{"1":[10],"2":[10],"3":[10],"4":[10],"5":[10],"6":[10],"7":[10],"8":[10],"9":[10],"10":[10,10,10]},"mel":{"1":[6,1],"2":[9,0],"3":[8,2],"4":[5,5],"5":[8,0],"6":[6,2],"7":[9,1],"8":[7,2],"9":[8,2],"10":[9,1,7]}}}

Get an existing bowling game:
curl -v -X GET http://localhost:8080/Bowling/game/{id}

Add a player to existing bowling game
curl -v -X PUT http://localhost:8080/Bowling/game/{id}?username={unique-name}

Delete a player from game
curl -v -X DELETE http://localhost:8080/Bowling/game/{id}/player/{username}

Add/Update score for a player in a bowling game
curl -v -X PUT http://localhost:8080/Bowling/game/{id}/player/{username}/frame/{frameid}?score={score}
Note: frame resource implicitly exists and value is from 1 through 10

Get live scores for an ongoing bowling game
curl -v -X GET http://localhost:8080/Bowling/game/{id}/livescores

Delete a bowling game:
curl -v -X DELETE http://localhost:8080/Bowling/game/{id}

