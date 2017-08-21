USE foodie;
CREATE TABLE restaurants (business_id VARCHAR(255) NOT NULL, name VARCHAR(255), categories VARCHAR(255), city VARCHAR(255), state VARCHAR(255), stars FLOAT, full_address VARCHAR(255), latitude FLOAT, longitude FLOAT, image_url VARCHAR(255), url VARCHAR(255), PRIMARY KEY ( business_id ));
CREATE TABLE users (user_id VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, first_name VARCHAR(255), last_name VARCHAR(255), PRIMARY KEY ( user_id ));
CREATE TABLE history (visit_history_id bigint(20) unsigned NOT NULL AUTO_INCREMENT, user_id VARCHAR(255) NOT NULL , business_id VARCHAR(255) NOT NULL, last_visited_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (visit_history_id), FOREIGN KEY (business_id) REFERENCES restaurants(business_id), FOREIGN KEY (user_id) REFERENCES users(user_id));
INSERT INTO users VALUES ("1111", "3229c1097c00d497a0fd282d586be050", "Xiangyu", "Xiao");
