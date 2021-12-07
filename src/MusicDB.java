import javax.xml.transform.Result;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MusicDB extends Database{
    int numSongs;
    int numArtists;
    int numAlbums;
    int numPlaylists;

    public MusicDB(String dbName) {
        super(dbName);
        numSongs = countRows("songs");
        numArtists = countRows("artists");
        numAlbums = countRows("albums");
        numPlaylists = countRows("playlists");
    }

    public void init(){ //create tables in new musicDB
        Query("create table songs(songID INTEGER PRIMARY KEY NOT NULL, songName VARCHAR(50) NOT NULL , artist varchar(50) ,album varchar(50), length INTEGER)");
        Query("create table artists(artistID INTEGER PRIMARY KEY NOT NULL, artistName VARCHAR(50) NOT NULL)");
        Query("create table albums(albumID INTEGER PRIMARY KEY NOT NULL, albumName VARCHAR(50) NOT NULL, artistID INTEGER NOT NULL)");
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

    public List<List<Integer>> readPlaylist(){
        List<List<Integer>> playlists = new ArrayList<>();
        List<String> pName = new ArrayList<>() ;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select playlistName from playlists");

            while (rs.next()) {
                pName.add(rs.getString("playlistName"));
            }
            for(String s: pName){
                List<Integer> eachPlaylist = new ArrayList<>();
                ResultSet rs2 = statement.executeQuery("select songID from "+s);
                while(rs2.next()){
                    eachPlaylist.add(rs2.getInt("songID"));
                }
                playlists.add(eachPlaylist);
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
        return playlists;
    }

    public List<String> readPlaylistNames(){
        List<String> pName = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+currentDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select playlistName from playlists");
            while (rs.next()) {
                pName.add(rs.getString("playlistName"));
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
        return pName;
    }

    public int getNumSongs(){
        return numSongs;
    }

    public int getArtistID(String artistName){
        return IDQuery("select id from artists where artistName ='"+artistName+"';");
    }

    public void writeSongsToDB(List<Song> songs){
        List<String> query = new ArrayList<>();
        for(Song s : songs){
            numSongs++;
            query.add("insert into songs values("+numSongs+","+s.toSQL()+")");
        }
        Query(query);
    }
    public void writeArtistsToDB(List<Artist> artists){
        List<String> query = new ArrayList<>();
        for(Artist a: artists){
            numArtists++;
            query.add("insert into artists values("+numArtists+",'"+a.getName()+"')");
        }
        Query(query);
    }
    public void writeAlbumsToDB(List<Album> albums){
        List<String> query = new ArrayList<>();
        for(Album a:albums){
            numAlbums++;
            query.add("insert into albums values("+numAlbums+",'"+a.getName()+"',"+getArtistID(a.getName()));
        }
        Query(query);
    }

    public void writeNewPlaylist(Playlist playlist){
        numPlaylists++;
        List<String> query = new ArrayList<>();
        Query("insert into playlists values("+numPlaylists+",'"+playlist.getName()+"')");
        Query("CREATE TABLE "+playlist.getName()+"(songID INTEGER NOT NULL,songName VARCHAR(50) NOT NULL, artistName VARCHAR(50))");
        for(Song s:playlist.getSongs()){
            query.add("insert into "+playlist.getName()+" values("+s.getSongID()+",'"+s.getName()+"','"+s.getArtist().getName()+"')");
        }
        Query(query);
    }

    public static void main(String[] args) {
        MusicDB test = new MusicDB("data/test.db");
        //System.out.println(test.readSongs());
        //System.out.println(test.readArtists());
        //System.out.println(test.readAlbums());
        System.out.println(test.readPlaylist());
        System.out.println(test.readPlaylistNames());

    }
}
