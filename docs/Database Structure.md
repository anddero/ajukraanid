# Relational Database Structure & Corresponding Java Objects

## Game
Game is an object associated with a session where a group of people are asked the same questions after each of which they can score each-others answers.

Database table **games**
```
+-----------+---------+------+-----+---------+----------------+
| Field     | Type    | Null | Key | Default | Extra          |
+-----------+---------+------+-----+---------+----------------+
| game_id   | int(11) | NO   | PRI | null    | auto_increment |
| game_code | char(4) | NO   |     | null    |                |
+-----------+---------+------+-----+---------+----------------+
```
* `game_id` is referenced by `players.game_id` - each player belongs to a certain game instance

Java class `Game` inaccessible fields:
* `id` is a primary key, there will be exactly one Java `Game` object associated with the same value of this field, therefore it can be ignored in game logic implementation

Java class `Game` accessible fields:
* `code` is a unique code for each game instance, the uniqueness of which is ensured by game logic and not enforced by database constraints - actually the code may be repeated in the future, it must only be unique among all active game instances (this is why PK `game_id` is necessary)
* `players` is a list of players associated with one game instance

## Player
Player is an object representing a single player who has been connected to a certain game instance.

Database table **players**
```
+-------------+-------------+------+-----+---------+----------------+
| Field       | Type        | Null | Key | Default | Extra          |
+-------------+-------------+------+-----+---------+----------------+
| player_id   | int(11)     | NO   | PRI | null    | auto_increment |
| player_name | varchar(32) | NO   |     | null    |                |
| game_id     | int(11)     | NO   | MUL | null    |                |
+-------------+-------------+------+-----+---------+----------------+
```
* `game_id` references `games.game_id` - the game session to which the player belongs

Java class `Player` inaccessible fields:
* `id` is the primary key similarly to `Game.id`

Java class `Player` accessible fields:
* `name` is the chosen name by the player as displayed in the user interface
