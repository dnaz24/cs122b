USE moviedb; 

# Part 2 Problem 1
SELECT m.id, m.title, m.year, m.director, IF (m.banner_url IS NULL, "NULL", m.banner_url) AS BannerURLExists, IF (m.trailer_url IS NULL, "NULL", m.trailer_url) AS TrailerURLExists
FROM stars_in_movies sim, movies m, stars s
WHERE s.id = sim.star_id AND m.id = sim.movie_id AND (s.first_name = "1" OR s.last_name = "1" OR s.id = "1");

# Part 2 Problem 1
SELECT m.id, m.title, m.year, m.director, IF (m.banner_url IS NULL, "NULL", m.banner_url) AS BannerURLExists, IF (m.trailer_url IS NULL, "NULL", m.trailer_url) AS TrailerURLExists
FROM stars_in_movies sim, movies m, stars s
WHERE s.id = sim.star_id AND m.id = sim.movie_id AND ((s.first_name = "1" AND s.last_name = "2") OR (s.first_name = "1" OR s.last_name = "2"));

# Part 2 Problem 2
INSERT INTO stars VALUES(1, "2", "3", 4, "5");

# Part 2 Problem 3
INSERT INTO customers(id, first_name, last_name, cc_id, address, email, password)
SELECT 1, "2", "3", "4", "5", "6", "7"
FROM dual
WHERE EXISTS (SELECT cc.id FROM creditcards cc, customers c WHERE cc.id = c.cc_id AND cc.id = "4");

# Part 2 Problem 4
DELETE FROM customers
WHERE customers.id = 1; 
