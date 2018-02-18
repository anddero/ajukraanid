# MySQL Bash Commands

## Global Escaped Commands

##### Switch to SQL query processing mode.
```
\sql
```
Example:
```
mysql-js> \sql
Switching to SQL mode... Commands end with ;
```

##### Switch to JavaScript processing mode (default).
```
\js
```
Example:
```
mysql-sql> \js
Switching to JavaScript mode...
```

##### Connect to the database server.
```
\connect name@server
```
Example:
```
mysql-sql> \connect root@localhost
Creating a Session to 'root@localhost'
Enter password: **********************
Your MySQL connection id is 12
Server version: 5.7.21-log MySQL Community Server (GPL)
No default schema selected; type \use <schema> to set one.
```

##### Use a specific schema (database) in the server for future queries.
In MySQL `schema` and `database` are synonyms.
```
\use <schema>
```
Example:
```
mysql-sql> \use ajukraanid
Schema set to `ajukraanid`.
```

## SQL QUERIES (Switched to SQL mode)

##### List the databases (schemas) in the connected server.
```sql
show schemas;
```
Example:
```
mysql-sql> show schemas;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| ajukraanid         |
| mysql              |
| performance_schema |
| sakila             |
| sys                |
| world              |
+--------------------+
7 rows in set (0.00 sec)
```

##### Create a new database.
```sql
create database <name>;
```
Example:
```
mysql-sql> create database test;
Query OK, 1 row affected (0.04 sec)
```

##### List the tables in the currently selected database.
You can select a database with `\use <database_name>`, see above.
```sql
show tables;
```
Example with `ajukraanid` as the selected database:
```
mysql-sql> show tables;
+----------------------+
| Tables_in_ajukraanid |
+----------------------+
| games                |
+----------------------+
1 row in set (0.00 sec)
```

##### Create a new table.
Create a new table with a primary key field and other fields all prefixed by the object's type `<name>`, all fields `NOT NULL`.
```sql
create table <name>s(
	<name>_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	<name>_<other> <type> NOT NULL,
	...);
```
Example:
```sql
mysql-sql> create table player(
       ... player_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
       ... player_name VARCHAR(32) NOT NULL);
Query OK, 0 rows affected (0.37 sec)
```

##### Display the structure of an existing table.
```sql
describe <table_name>;
```
Example:
```
mysql-sql> describe player;
+-------------+-------------+------+-----+---------+----------------+
| Field       | Type        | Null | Key | Default | Extra          |
+-------------+-------------+------+-----+---------+----------------+
| player_id   | int(11)     | NO   | PRI | null    | auto_increment |
| player_name | varchar(32) | NO   |     | null    |                |
+-------------+-------------+------+-----+---------+----------------+
2 rows in set (0.01 sec)
```

##### Rename a falsely named table.
```sql
RENAME <old_name> TO <new_name>;
```
Example:
```sql
mysql-sql> RENAME TABLE player TO players;
Query OK, 0 rows affected (0.41 sec)
```

##### Add a column to an existing table as a foreign key.
```sql
ALTER TABLE <table_name> ADD COLUMN(
	<field_name> <type> NOT NULL,
	FOREIGN KEY(<field_name>) REFERENCES <other_table>(<other_field>)
);
```
Example:
```sql
mysql-sql> ALTER TABLE players ADD COLUMN (game_id INT NOT NULL, FOREIGN KEY(game_id) REFERENCES games(game_id));
Query OK, 0 rows affected (2.14 sec)

Records: 0  Duplicates: 0  Warnings: 0
```
