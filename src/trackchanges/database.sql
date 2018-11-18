/*
-- CSCI 201L TrackChanges 
-- Database 
*/
DROP DATABASE IF EXISTS CSCI201ProjectDatabase;
CREATE DATABASE CSCI201ProjectDatabase;
USE CSCI201ProjectDatabase;

/* --------------------------- Users Data --------------------------- */
/* Stores user's personal details */
CREATE TABLE User (
	user_id VARCHAR(100) PRIMARY KEY,
	user_displayname VARCHAR(100) NOT NULL,
	user_imageurl VARCHAR(100) NOT NULL,
    user_logintimestamp VARCHAR(100) NOT NULL,
	user_is_active BOOL NOT NULL
);

/* Stores followers / following relationship */
CREATE TABLE Follow (
	user_id VARCHAR(100) NOT NULL,
	follower_id VARCHAR(100) NOT NULL,
	PRIMARY KEY (user_id, follower_id),
	FOREIGN KEY Follow_user_id (user_id) REFERENCES User(user_id),
	FOREIGN KEY Follow_follower_id (follower_id) REFERENCES User(user_id)
);

/* --------------------------- Album and Song Data --------------------------- */

/* Stores the album id for identification */
CREATE TABLE Album (
	album_id VARCHAR(100) PRIMARY KEY
);

/* Stores the song id to uniquely identify the song */
CREATE TABLE Song (
	song_id VARCHAR(100) PRIMARY KEY
);

/* Tracks which users like which songs */
CREATE TABLE SongLike (
	song_id VARCHAR(100) NOT NULL,
	user_id VARCHAR(100) NOT NULL,
	PRIMARY KEY (song_id, user_id),
	FOREIGN KEY SongLike_song_id (song_id) REFERENCES Song(song_id),
	FOREIGN KEY SongLike_user_id (user_id) REFERENCES User(user_id)
);

/* --------------------------- Post Data --------------------------- */
/* Stores the content of each post and creator of post */
CREATE TABLE Post (
	post_id VARCHAR(100) PRIMARY KEY,
   	post_timestamp VARCHAR(100) NOT NULL,
	user_id VARCHAR(100) NOT NULL,
	post_message VARCHAR(500) NOT NULL,
	FOREIGN KEY Post_user_id (user_id) REFERENCES User(user_id)
);

/* Stores the number of shares of each post */
CREATE TABLE PostShare (
	post_id VARCHAR(100) NOT NULL,
	user_id VARCHAR(100) NOT NULL,
	PRIMARY KEY (post_id, user_id),
	FOREIGN KEY PostShare_post_id (post_id) REFERENCES Post(post_id),
	FOREIGN KEY PostShare_user_id (user_id) REFERENCES User(user_id)
);

/* Stores the number of likes of each post */
CREATE TABLE PostLike (
	post_id VARCHAR(100) NOT NULL,
	user_id VARCHAR(100) NOT NULL,
	PRIMARY KEY (post_id, user_id),
	FOREIGN KEY PostLike_post_id (post_id) REFERENCES Post(post_id),
	FOREIGN KEY PostLike_user_id (user_id) REFERENCES User(user_id)
);

/* Stores the albums that are included in each post */
CREATE TABLE PostAlbum (
	album_id VARCHAR(100) NOT NULL,
	post_id VARCHAR(100) NOT NULL,
	PRIMARY KEY (album_id, post_id),
	FOREIGN KEY PostAlbum_album_id (album_id) REFERENCES Album(album_id),
	FOREIGN KEY PostAlbum_post_id (post_id) REFERENCES Post(post_id)
);

/* Stores the songs that are included in each post */
CREATE TABLE PostSong (
	song_id VARCHAR(100) NOT NULL,
	post_id VARCHAR(100) NOT NULL,
	PRIMARY KEY (song_id, post_id),
	FOREIGN KEY PostSong_song_id (song_id) REFERENCES Song(song_id),
	FOREIGN KEY PostSong_post_id (post_id) REFERENCES Post(post_id)
);