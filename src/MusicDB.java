import javax.xml.transform.Result;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MusicDB extends Database{
    protected int numSongs;
    protected int numArtists;
    protected int numAlbums;
    protected int numPlaylists;

    public MusicDB(String dbName) {
        super(dbName);
        numSongs = countRows("songs");
        numArtists = countRows("artists");
        numAlbums = countRows("albums");
        numPlaylists = countRows("playlists");
    }

    public MusicDB(String dbName,Boolean fresh){
        super(dbName);
    }

    public void init(){ //create tables in new musicDB
        Query("create table songs(songID INTEGER PRIMARY KEY NOT NULL, songName VARCHAR(50) NOT NULL , artist varchar(50) ,album varchar(50), length INTEGER)");
        Query("create table artists(artistID INTEGER PRIMARY KEY NOT NULL, artistName VARCHAR(50) NOT NULL)");
        Query("create table albums(albumID INTEGER PRIMARY KEY NOT NULL, albumName VARCHAR(50) NOT NULL, artistID INTEGER NOT NULL)");
        Query("CREATE TABLE playlists(playlistID INTEGER PRIMARY KEY NOT NULL, playlistName VARCHAR(50));");
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
            ResultSet rs2 = statement.executeQuery("select artistName from artists ar, albums al where al.artistID = ar.artistID;");
            int i = 0;
            while(rs2.next()){
                albums.get(i).setArtistName(rs2.getString("artistName"));
                i++;
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
        return IDQuery("select artistID from artists where artistName ='"+artistName+"';");
    }

    public void writeSongsToDB(List<Song> songs){
        List<String> query = new ArrayList<>();
        for(Song s : songs){
            numSongs++;
            query.add("insert into songs values("+numSongs+","+s.toSQL()+")");
        }
        Query(query);
    }


    public void writeArtistToDB(String artist){
        Query("insert into artists values("+numArtists+",'"+artist.replaceAll("'","''")+"')");
        numArtists++;
    }

    public void writeArtistsToDB(List<Artist> artists){
        List<String> query = new ArrayList<>();
        for(Artist a: artists){
            query.add(("insert into artists values("+numArtists+",'"+a.getName().replaceAll("'","''")+"')"));
            numArtists++;
        }
        Query(query);
    }

    public void writeAlbumToDB(String album,String artist){
        Query("insert into albums values("+numAlbums+",'"+album.replaceAll("'","''")+"',"+getArtistID(artist)+")");
        numAlbums++;
    }

    public void writeAlbumsToDB(List<Album> albums){
        List<String> query = new ArrayList<>();
        for(Album a:albums){
            numAlbums++;
            query.add("insert into albums values("+numAlbums+",'"+a.getName().replaceAll("'","''")+"',"+getArtistID(a.getName()));
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
        //MusicDB test = new MusicDB("data/test.db");
        //System.out.println(test.readAlbums());
    }
}
