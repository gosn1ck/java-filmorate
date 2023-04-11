# java-filmorate
## Description
This is a simple backend application like imbd.com

## Frameworks and Technologies
- Java 11
- Spring Boot
- JDBC, SQL, H2
- Maven, Junit
- Postman

# ER Diagram
![er-filmorate.png](er-filmorate.png)
[click here to edit](https://dbdiagram.io/d/642494195758ac5f17253c14)

## SQL queries:

- Film names with genres:
```
SELECT 
  f.film_name, 
  g.genre_name 
FROM 
  films AS f 
  JOIN film_genre AS fg ON fg.film_id = f.film_id 
  JOIN genres AS g ON fg.genre_id = g.genre_id
```

- Film's likes amount:
```
SELECT 
  f.film_name, 
  COUNT(l.user_id) 
FROM 
  films AS f 
  JOIN likes AS l ON l.film_id = f.film_id 
GROUP BY 
  f.film_name
```
- Nikolay's friends:
```
SELECT 
  fu.user_name 
FROM 
  users AS u 
  JOIN friends AS f ON u.user_id = f.user_id 
  JOIN users AS fu ON fu.user_id = f.friend_id 
WHERE 
  u.user_name = 'Nikolay'
```

## Support
please contact me at gosn1ck@yandex.ru