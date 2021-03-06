DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;

CREATE TABLE movies(
id INT NOT NULL AUTO_INCREMENT,
title VARCHAR(100) NOT NULL,
year INT NOT NULL, 
director VARCHAR(100) NOT NULL, 
banner_url VARCHAR(200), 
trailer_url VARCHAR(200),
PRIMARY KEY (id)
);

CREATE TABLE stars(
id INT NOT NULL AUTO_INCREMENT, 
first_name VARCHAR(50) NOT NULL, 
last_name VARCHAR(50) NOT NULL, 
dob DATE, 
photo_url VARCHAR(200), 
PRIMARY KEY(id)
); 

CREATE TABLE stars_in_movies(
star_id INT NOT NULL, 
movie_id INT NOT NULL, 
FOREIGN KEY (star_id) REFERENCES stars(id), 
FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE genres(
id INT NOT NULL AUTO_INCREMENT, 
name VARCHAR(32) NOT NULL, 
PRIMARY KEY(id)
);

CREATE TABLE genres_in_movies(
genre_id INT NOT NULL, 
movie_id INT NOT NULL, 
FOREIGN KEY (genre_id) REFERENCES genres(id), 
FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE creditcards(
id VARCHAR(20) NOT NULL, 
first_name VARCHAR(50) NOT NULL, 
last_name VARCHAR(50) NOT NULL, 
expiration DATE NOT NULL, 
PRIMARY KEY(id)
);

CREATE TABLE customers(
id INT NOT NULL AUTO_INCREMENT, 
first_name VARCHAR(50) NOT NULL, 
last_name VARCHAR(50) NOT NULL, 
cc_id VARCHAR(20) NOT NULL, 
address VARCHAR(200) NOT NULL, 
email VARCHAR(50) NOT NULL, 
password VARCHAR(20) NOT NULL, 
PRIMARY KEY(id),
FOREIGN KEY (cc_id) REFERENCES creditcards(id)
);

CREATE TABLE sales(
id INT NOT NULL AUTO_INCREMENT, 
customer_id INT NOT NULL, 
movie_id INT NOT NULL, 
sale_date DATE NOT NULL, 
PRIMARY KEY(id), 
FOREIGN KEY(customer_id) REFERENCES customers(id),
FOREIGN KEY(movie_id) REFERENCES movies(id)
);