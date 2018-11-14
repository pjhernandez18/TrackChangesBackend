/*
-- CSCI 201L TrackChanges 
-- Database 
*/
DROP DATABASE IF EXISTS TrackChanges;
CREATE DATABASE TrackChanges;
USE TrackChanges;

/* --------------------------- Users Data --------------------------- */
/* Stores user's personal details */
CREATE TABLE User (
	user_id VARCHAR(100) PRIMARY KEY,
	user_login_timestamp TIMESTAMP NOT NULL,
	user_email VARCHAR(100) NOT NULL,
	user_firstname VARCHAR(100) NOT NULL,
	user_lastname VARCHAR(100) NOT NULL,
	user_username VARCHAR(100) NOT NULL,
	user_image_url VARCHAR(100) NOT NULL,
	user_is_active BOOL NOT NULL
);

/* Stores the artist's personal details */
CREATE TABLE Artist (
	artist_id VARCHAR(100) PRIMARY KEY,
	artist_firstname VARCHAR(100) NOT NULL,
	artist_lastname VARCHAR(100) NOT NULL
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
	album_id VARCHAR(100) PRIMARY KEY,
	artist_id VARCHAR(100) NOT NULL,
	FOREIGN KEY Album_artist_id (artist_id) REFERENCES Artist(artist_id)
);

/* Stores the song id to uniquely identify the song */
CREATE TABLE Song (
	song_id VARCHAR(100) PRIMARY KEY,
	artist_id VARCHAR(100) NOT NULL,
	FOREIGN KEY Song_artist_id (artist_id) REFERENCES Artist(artist_id)
);

/* Stores which songs are included inside an album */
CREATE TABLE AlbumSong (
	album_id VARCHAR(100) NOT NULL,
	song_id VARCHAR(100) NOT NULL,
	PRIMARY KEY (album_id, song_id),
	FOREIGN KEY AlbumSong_album_id (album_id) REFERENCES Album(album_id),
	FOREIGN KEY AlbumSong_song_id (song_id) REFERENCES Song(song_id)
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
   	post_timestamp TIMESTAMP NOT NULL,
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