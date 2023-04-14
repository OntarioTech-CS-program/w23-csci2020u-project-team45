# Final Course Group Project - Team 45
Assignment completed for Software Systems Development and Integration "CSCI - 2020U" for the Winter 2023 semester by:
- Sara Bhoira - 100826813
- Chantel George - 100825272
- Fareeha Malik - 100785443
- Alagu Vallikkannan - 100822256


## Description
In this final course assignment, our group created a game "SnowbotRush". The objective of the game is for two players 
to compete with each other if they are playing at the same difficulty level. Once the game is started,
a game board appears on both the players screen individually. Both the game boards will have various "good" 
assets such as coins, diamonds, snowflakes or jewels. The game boards will also have various "bad" such as icicles 
or a water monster which causes the game to freeze or lose lives respectively. These assets have to be unveiled by clicking 
the "question marks" The robot character reflects these changes through its appearance. For instance when a player 
unveils the icicle, the robot character will have a frozen appearance. 

Essentially this is a game of chance as you will not know what is behind the qustion mark squares until it is clicked.
You are also competing with another player, who has the same selection of question mark boxes to unveil.

Players may also use with the in-game chat functionality.

## Interface and main functionalities
The screenshots below will help describe the interface and main functionalities of the game.

#### Figure 1
Figure 1 displays the front page of the game, which players will see once successfuly running the index.html file. You can click on the "Play Game" or "Game Rules" button.
![](Interface_Screenshots/SnowBotRush_FrontPage.PNG)

#### Figure 2
Figure 2 displays the gameRules.html file which will display if the player clicks on the "Game Rules" button on the index.html page.
![](Interface_Screenshots/SnowBotRush_GameRulesPage.png)

#### Figure 3
Figure 3 shows the main game interface. There will be a "waiting" buffer on the page if another player has not joined the same level in a different tab. The game will not start unless there are 2 players playing at the same level.
![](Interface_Screenshots/SnowBotRush_MainGame_Loading.PNG)

#### Figure 4
Figure 4 shows the player "Fareeha" who has selected the "Easy" difficulty level. The game board is rendered. In this instance "Fareeha" selected a question mark square which unveiled a "bad" incicle asset. The robot character is now frozen. This is also displayed in the game message text box. There is also a high score display. "Kate", "Sally" and "Jack" are placeholders for reference. Only the top 5 players are showcased on the High Score board. The "Chat With Other Players!" chat box also shows a conversation between players "Fareeha" and "Sara". Also, player "Fareeha" selected on many boxes which display orange "X" symbols. That is because player "Sara" has already selected those on her interface as shown in Figure 5.
![](Interface_Screenshots/Main_Game_PlayerFareeha_FrozenRobot.png)

#### Figure 5
Figure 5 shows the interface of player "Sara", who is playing against player "Fareeha" in a different tab.
![](Interface_Screenshots/MainGame_Player2Sara_FrozenRobot.png)






### In-course concept implementation
Below is detailed description of various concepts implemented within this project that include the course concepts
which we were taught throughout the semester:
- 
- 

## How To Clone and Run Application
1. Clone repository using the GitHub repository link by using the command: git clone.
2. Open the project in IntelliJ
3. Build the project
4. Edit configurations - add GlassFish Local Server, select domain, add artifact war exploded in the deployment, select 
apply and save
5. Run the server
6. Run the index.html file

## Resources
#### Various assets and front page background retrieved from:
1. Front page background retrieved from: https://www.freepik.com/free-photos-vectors/snow-city
2. Robot character retrieved from: https://www.vecteezy.com/vector-art/623956-cute-robot-box-character-designs-with-emotions-poses
3. Water monster character retrieved from: https://publicdomainvectors.org/en/free-clipart/Water-monster/67042.html
4. Coin asset retrieved from: https://pixlok.com/images/game-coin-clipart-png-image-free-download/
5. Diamond asset retrieved from: https://creazilla.com/nodes/35237-diamond-jewelry-clipart
6. Icicle asset retrieved from: https://creazilla.com/nodes/1688816-icicles-clipart
7. Jewel asset retrieved from: https://www.dreamstime.com/illustration/jewels.html

#### Resources used for a deeper understanding of concepts and to implement within our game:
1. https://www.w3schools.com/howto/howto_css_center_button.asp
2. https://www.geeksforgeeks.org/singleton-class-java/
3. https://www.w3schools.com/java/java_enums.asp
4. https://stackoverflow.com/questions/3990319/storing-integer-values-as-constants-in-enum-manner-in-java
5. https://www.java67.com/2014/10/how-to-create-and-initialize-two-dimensional-array-java-example.html
6. https://stackoverflow.com/questions/20389890/generating-a-random-number-between-1-and-10-java
7. https://docs.oracle.com/javase/8/docs/api/java/util/Set.html#method.summary
8. https://mkyong.com/java/java-get-keys-from-value-hashmap/#hashmap-only-has-one-item
9. https://www.w3schools.com/java/java_iterator.asp
10. https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
11. https://stackoverflow.com/questions/13546424/how-to-wait-for-a-websockets-readystate-to-change
12. https://www.w3schools.com/jsref/met_win_setinterval.asp
13. https://www.w3schools.com/jsref/met_element_removeattribute.asp
14. https://www.w3schools.com/jsref/met_document_createelement.asp
15. https://www.encodedna.com/javascript/populate-json-data-to-html-table-using-javascript.htm
16. https://fasterxml.github.io/jackson-databind/javadoc/2.7/index.html?com/fasterxml/jackson/databind/JsonNode.html
17. https://docs.oracle.com/javaee/6/tutorial/doc/bnags.html
18. https://www.w3schools.com/jsref/prop_style_cursor.asp
19. https://www.html-code-generator.com/css/textbox-style

### Libraries 
- jakarta
- jackson 

### Packages
- java.util
- java.io
- org.apache
- java.net







