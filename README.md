# "Build Your Own World", a Randomly Generated Maze Challenge Made with Java! 

![A gif demonstrating gameplay](readme_demo.gif)

## Features
- Control user avatar (the @ symbol) using the WASD keys
- Pursuer: A red block that follow the use avatar, and ends the game if it catches the user.
- Toggle path: Pressing the 'p' key on the keyboard shows the pursuer's path in red. 
- Toggle lights: Pressing the 'q' key turns the lights on and off and highlights only the avatar and the pursuer
- Teleportation square: Green square to teleport the avatar the location of the corresponding square
- Power-up: Pink square that lets you destroy wall blocks to move through the mae more easily.
- Locked door: Collect the key and go to the yellow door square to win the game!
- Descriptive text: Description of tiles and game state are shown in the upper right-hand corner.


## Algorithms
### Generating the rooms 
The algorithm starts by splitting the world into four rectangles of random size using the randomsplit() method, creating four rooms. 
These are then split into four rooms as well, and this process continues until the algorithm has created at least one hallway
(a room with a width of 1 or 2). The result is that the world area is filled with rectangular areas with randomized widths and heights. 

### Generating a path through the world
This algorithm is based on a  [randomized depth-first search algorithm](https://en.wikipedia.org/wiki/Maze_generation_algorithm#Randomized_depth-first_search).
After the grid has been generated, the visit function starts at the bottom left corner of the world, and randomly pick a direction to go in. 
If the room it would arrive in either is off the world area, or if the room in that direction has been visited already, it randomly selects a new 
direction again, and keeps doing this until it finds a valid room to go to. It then removes one of the wall tiles between the two rooms, 
and replaces it with a floor, creating a path. A room that has no valid neighbors to go to (all already visited or off the world map) is a dead end, 
and the algorithm will backtrack until it find a room with a "visit-able" neighbor. This process continues until every room has been visited,
meaning there is now a path to each room. One quirk of this algorithm is that it tends to produce very long hallways, since it keeps recursing until it 
reaches the end of one "branch" of rooms.
