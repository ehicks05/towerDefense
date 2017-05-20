## Eric's Tower Defense

This is a game I have been developing as a way to learn about game development using Java.

* * *

### Design Notes

#### A Simple Object Hierarchy

**Unit** - Everything in the game world (except for the map) extends from the Unit class. The Unit class is responsible for basic information required for drawing objects on the screen. It handles the position, size, and movement speed of game objects. The following classes extend Unit: 
*   **Mob** - This class stores information about the 'bad guys' in the game. Their toughness, appearance, and what frame of animation they are on is all stored here.
*   **Tower** - These are the structures you can build to defeat Mobs. Towers have attributes such as attack range, attack speed, price to build, and the type of projectile they can fire.
*   **Projectile** - This is what a tower shoots. Projectiles know what image they should be drawn with as well as their theta so we can rotate the image appropriately. There are also fields for how much damage it does and whether or not it can do splash damage.

#### Random Map Generation and Pathfinding

The game map is a grid of rows and columns. It contains road tiles that mobs walk on and grass tiles that towers are built on. When the game starts up the random map generator builds a random path from the left of the screen to the right. That path information is turned into road tiles when we generate the visual map that you see when you play the game. The path information is also referred to by each mob so that they know to stay off the green!

#### The Game Loop

The majority of the game engine is split between two tasks: **update game state**, and **paint the world**. The game attempts to do both every 16.<span style="text-decoration: overline">6</span>ms which is 60 fps. On faster machines, the engine will cap itself at 60 fps and not waste additional power trying to draw additional frames or update state. On slower machines, the engine can postpone repainting the world and catch up on updating game state. The ability of the engine to separate the updating and painting steps makes the game run more robustly. It also prevents situations where if the machine gets bogged down the speed of the game simulation actually slows down as well. Please see [Gaffer on Games](http://gafferongames.com/game-physics/fix-your-timestep/) for more info.

#### Collision Detection

All units are considered to be circles with radius r when it comes to collision detection. Every game tick, the position of every projectile and mob is updated. The collision detection engine checks each projectile to see if its collision circle intersects the circle of any mob. If a collision is detected, the mob has been hit!

#### Basic High Score Tracking Over a Network

As a first step towards integrating networking functionality into the game, I added a high score tracking daemon. It is a simple program that listens for messages over the network from the game client and responds appropriately. It accepts two commands: **INSERT_SCORE** and **GET_HIGH_SCORES.** Using those commands a game client can send a new score at the end of a game session, and then ask for a list of all the top scores to see how their score stacks up.

#### Sound Engine

When certain events occur, like an arrow being fired, or a cannonball exploding, the game generates sound effect. When the sound engine is told to play a sound it first checks how many sound effects are currently being played. If too many are being played the new sound effect will not be played. This prevents situations where 50 arrows hit a mob at once and the combination results in a clipped waveform.

#### Data Files

The different types of mobs, towers, and projectiles are all stored in csv files to make it easier to make changes and additions. The files are loaded into memory parsed on game startup.