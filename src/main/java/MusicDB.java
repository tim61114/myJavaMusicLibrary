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

    /**
     * @param dbName is the name of the user, which is also the name of the db file
     * @param fresh is to create a new music db instead of reading the rows of the tables in the db.
     */
    public MusicDB(String dbName, Boolean fresh) {
        super(dbName);
        if(!fresh){
            numSongs = countRows("songs");
            numArtists = countRows("artists");
            numAlbums = countRows("albums");
            numPlaylists = countRows("playlists");
        }

    }


    /**
     * to generate all tables in a fresh db for a new user
     */
    public void init(){ //create tables in new musicDB
        Query("create table songs(songID INTEGER PRIMARY KEY NOT NULL, songName VARCHAR(50) NOT NULL , artist varchar(50) ,album varchar(50), length INTEGER)");
        Query("create table artists(artistID INTEGER PRIMARY KEY NOT NULL, artistName VARCHAR(50) NOT NULL)");
        Query("create table albums(albumID INTEGER PRIMARY KEY NOT NULL, albumName VARCHAR(50) NOT NULL, artistID INTEGER NOT NULL)");
        Query("CREATE TABLE playlists(playlistID INTEGER PRIMARY KEY NOT NULL, playlistName VARCHAR(50));");
    }

    /**
     * @return a list of songs by reading the 'songs' table in the user's db
     */
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

    /**
     * @return a list of artists by reading the 'artists' table in the user's db
     */
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


    /**
     * @return a list of albums by reading the 'albums' table in the user's db
     */
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

    /**
     * @return a list of lists which the inner list are the actual data of the playlists of the user and the outer list is the list to save all playlists as integers, representing the id of a song.
     */
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


    /**
     * @return the names of the playlists
     */
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

    /**
     * @param artistName is the name of the artist
     * @return the id of the artist
     */
    public int getArtistID(String artistName){
        return IDQuery("select artistID from artists where artistName ='"+artistName+"';");
    }

    /**
     * @param songs is the list of songs to be stored into the db.
     */
    public void writeSongsToDB(List<Song> songs){
        List<String> query = new ArrayList<>();
        for(Song s : songs){
            numSongs++;
            query.add("insert into songs values("+numSongs+","+s.toSQL()+")");
        }
        Query(query);
    }


    /**
     * @param artist is the name of the artist to be stored into the db.
     */
    public void writeArtistToDB(String artist){
        Query("insert into artists values("+numArtists+",'"+artist.replaceAll("'","''")+"')");
        numArtists++;
    }

    /**
     * @param artists is the list of artists to be stored into the db. Currently unused.
     */
    public void writeArtistsToDB(List<Artist> artists){
        List<String> query = new ArrayList<>();
        for(Artist a: artists){
            query.add(("insert into artists values("+numArtists+",'"+a.getName().replaceAll("'","''")+"')"));
            numArtists++;
        }
        Query(query);
    }

    /**
     * @param album is the name of the album to be written in to the db
     * @param artist is the name of the artist
     */
    public void writeAlbumToDB(String album,String artist){
        Query("insert into albums values("+numAlbums+",'"+album.replaceAll("'","''")+"',"+getArtistID(artist)+")");
        numAlbums++;
    }

    /**
     * @param albums is the list of albums to be stored into the db. Currently unused.
     */
    public void writeAlbumsToDB(List<Album> albums){
        List<String> query = new ArrayList<>();
        for(Album a:albums){
            query.add("insert into albums values("+numAlbums+",'"+a.getName().replaceAll("'","''")+"',"+getArtistID(a.getName()));
            numAlbums++;
        }
        Query(query);
    }

    /**
     * @param playlist is to write a playlist into the db, which is separated into writing the name into the playlist table and creating a new table for storing the songs of the playlist
     */
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
