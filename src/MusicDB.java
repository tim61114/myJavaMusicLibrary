public class MusicDB extends Database{
    public MusicDB(String dbName) {
        super(dbName);
    }

    public void init(){ //create new musicDB
        Query("create table songs(songID INTEGER PRIMARY KEY NOT NULL, songName VARCHAR(50) NOT NULL , artistID INTEGER ,albumID INTEGER, length INTEGER)");
        Query("create table artists(artistID INTEGER PRIMARY KEY NOT NULL, artistName VARCHAR(50) NOT NULL)");
        Query("create table albums(albumID INTEGER PRIMARY KEY NOT NULL, albumName VARCHAR(50) NOT NULL, artistID INTEGER NOT NULL, numSongs INTEGER)");
    }

}
