# Custom DBMS
- This project is about an implementation of a custom database management system.
- Was a noob at programming when I implemented this project.

**Features**

- Efficient parsing of database files using StAX parser.
- XSD validation over XML database files.

**Anti-features**

1- Perfect example of spaghetti code.
2- Lack of design patterns and OOP.
3- SQL queries are not flexible due limited usage of regular expressions.

**Example for acceptable queries:**

- CREATE TABLE employees (name varchar, age int);

- INSERT INTO employees (name varchar, age int) VALUES (Mohamed, 24);

- SELECT name, age FROM employees WHERE age < 30;

- DROP TABLE employees;

** Sample run**

- Creating table

![](create.PNG)

- Inserting a new tuple

![](insert1.PNG)

- Inserting another tuple

![](insert2.PNG)

- Selecting tuples with age < 70

![](select1.PNG)

- Selecting tuples with age < 50

![](select2.PNG)

- Deleting tuples with age > 50

![](delete.PNG)

- Selecting tuples with age < 100

![](select3.PNG)

- Dropping the whole table

![](drop1.PNG)

![](drop2.PNG)
