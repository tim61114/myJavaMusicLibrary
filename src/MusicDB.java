import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MusicDB extends Database{
    public MusicDB(String dbName) {
        super(dbName);
    }

    public void init(){ //create tables in new musicDB
        Query("create table songs(songID INTEGER PRIMARY KEY NOT NULL, songName VARCHAR(50) NOT NULL , artist varchar(50) ,album varchar(50), length INTEGER)");
        Query("create table artists(artistID INTEGER PRIMARY KEY NOT NULL, artistName VARCHAR(50) NOT NULL)");
        Query("create table albums(albumID INTEGER PRIMARY KEY NOT NULL, albumName VARCHAR(50) NOT NULL, artistID INTEGER NOT NULL, numSongs INTEGER)");
    }

    public List<Song> readSongs(){
        List<Song> songs = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from songs");
            while (rs.next()) {
                songs.add(new Song(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5)));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return songs;
    }

    public List<Artist> readArtists(){
        List<Artist> artists = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from artists");
            while (rs.next()) {
                artists.add(new Artist(rs.getInt(1),rs.getString(2)));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return artists;
    }

    public List<Album> readAlbums(){
        List<Album> albums = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from albums");
            while (rs.next()) {
                albums.add(new Album(rs.getInt(1),rs.getString(2)));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return albums;
    }

    //public void writeDB()

    public static void main(String[] args) {
        MusicDB test = new MusicDB("data/test.db");
        System.out.println(test.readSongs());
        System.out.println(test.readArtists());
        System.out.println(test.readAlbums());

    }
}
