import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Library {

    MusicDB database;
    List<Song> songs;
    List<Artist> artists;
    List<Album> albums;
    List<Playlist> playlists = new ArrayList<>();
    Song nowPlaying = null;
    List<Song> nowPlayingList = new ArrayList<>();
    int nowPlayingIndex = -1;

    String currentArtistBio;
    String currentAlbumBio;
    String currentSongBio;

    /**
     * @param mDB is the music db of the user
     *            the constructor reads the data from the database and read it into java.
     */
    public Library(MusicDB mDB){
        database = mDB;
        songs = mDB.readSongs();
        artists = mDB.readArtists();
        albums = mDB.readAlbums();
        List<String> playlistNames = mDB.readPlaylistNames();
        List<List<Integer>> playlistData = mDB.readPlaylist();
        for(int i = 0;i < playlistNames.size();i++){
            Playlist temp = new Playlist(mDB.readPlaylistNames().get(i));
            playlistData.get(i).forEach(index -> temp.addSong(songs.get(index)));
            playlists.add(temp);
        }
        linkData();
    }

    /**
     * This links the Album and the Artists of a Song
     */
    public void linkData(){
        for(Album a: albums){
            for(Artist ar : artists){
                if (a.getArtistName().equals(ar.getName())) {
                    a.setArtist(ar);
                    ar.addToAlbums(a);
                }
            }
        }
    }

    //Show functions

    /**
     * Show all songs
     */
    public void showSongs(){
        System.out.println();
        if(songs.isEmpty()) System.out.println("Your library is empty! Consider adding some songs?");
        else{
            songs.forEach(song -> System.out.println(song.showSong()));
            System.out.println();
        }
    }

    /**
     * Show all songs with index
     */
    public void showSongsWithIndex(){
        System.out.println();
        if(songs.isEmpty()) System.out.println("Your library is empty! Consider adding some songs?");
        else{
            songs.forEach(song -> System.out.println((songs.indexOf(song) +1) +". "+song.showSong()));
            System.out.println();
        }
    }

    /**
     * Show all albums
     */
    public void showAlbums(){
        System.out.println();
        if(albums.isEmpty()){
            System.out.println("There's nothing here...");
        } else {
            albums.forEach(album -> System.out.println((albums.indexOf(album)+1)+". "+album.getName()));
            System.out.println();
        }
    }

    /**
     * Show all artists
     */
    public void showArtists(){
        System.out.println();
        if(artists.isEmpty()){
            System.out.println("It's quiet in here...");
        } else {
            artists.forEach(artist -> System.out.println((artists.indexOf(artist)+1)+". "+artist.getName()));
            System.out.println();
        }
    }

    /**
     * Show all playlists
     */
    public void showPlaylists(){
        System.out.println();
        if(playlists.size() == 0){
            System.out.println("You have no playlists! Create one!");
        } else{
            playlists.forEach(playlist -> {
                System.out.println(playlist.getName());
                playlist.show();
                System.out.println();
            });
            System.out.println();
        }
    }

    /**
     * Fetches the Bio of the current playing song for the Song, Artist and Album.
     * throws NullPointerException if nothing is found
     */
    public void showBio(){
        if(nowPlaying != null){
            Parser p = new Parser();
            currentSongBio = p.getSongDescription(nowPlaying.getArtist().getName(),nowPlaying.getName());
            currentArtistBio = p.getArtistDescription(nowPlaying.getArtist().getName());
            currentAlbumBio = p.getAlbumDescription(nowPlaying.getArtist().getName(),nowPlaying.getAlbum().getName());
            if(currentSongBio != null){
                System.out.println("About the song:");
                System.out.println(currentSongBio);
            }
            if(currentArtistBio != null){
                System.out.println("\nAbout the artist:");
                System.out.println(currentArtistBio);

            }
            if(currentAlbumBio != null){
                System.out.println("\nAbout the album:");
                System.out.println(currentAlbumBio);
            }
        } else {
            System.out.println("Uh oh so quiet");
        }

    }


    //Create new playlist

    /**
     * @param name is the name of the playlist
     * @param indexes are the index of the songs
     * @return a playlist
     */
    public Playlist createNewPlaylist(String name,List<Integer> indexes){
        Playlist temp = new Playlist(name);
        for(Integer i : indexes){
            temp.addSong(songs.get(i-1));
        }
        playlists.add(temp);
        database.writeNewPlaylist(temp);
        return temp;
    }

    //Create a new random list

    /**
     * @return a shuffled list of all songs
     */
    public List<Song> Shuffle(){
        Random random = new Random();
        if(songs.isEmpty()) return null;
        List<Song> songcopy = new ArrayList<>(songs);
        for(int i = songcopy.size() - 1;i >= 0;i--){
            int randomIndex = random.nextInt(songcopy.size()) ;
            nowPlayingList.add(songcopy.get(randomIndex));
            songcopy.remove(randomIndex);
        }
        nowPlayingIndex = 0;
        return nowPlayingList;
    }

    /**
     * @param index is the starting index
     * @return the list of songs
     */
    public List<Song> playSong(int index){
        nowPlayingIndex = 0;
        nowPlayingList = songs.subList(index-1,songs.size());
         return nowPlayingList;
    }


    /**
     * @param currentUser is the username of the current user.
     *                    This exports the playlist into an XML file stored in the users directory
     */
    public void exportPlaylists(String currentUser){
        playlists.forEach(playlist -> playlist.export(currentUser));
    }

    public List<Playlist> getPlaylists(){
        return playlists;
    }

    /**
     * @param newSongs is the list of songs (from an album) fetched from musicbrainz
     * @param newArtist is the artist name of the album
     * @param newAlbum is the name of the album
     *                 This writes the fetched album data into the db, and updates the current artist, album and song id from reading the db
     */
    public void addNewAlbumToDB(List<Song> newSongs, String newArtist, String newAlbum){
        database.writeSongsToDB(newSongs);
        if(!checkArtistExists(newArtist)) {
            database.writeArtistToDB(newArtist);
            artists = database.readArtists();
        }
        if(!checkAlbumExists(newAlbum)) {
            database.writeAlbumToDB(newAlbum,newArtist);
            albums = database.readAlbums();
        }
        songs = database.readSongs();
        linkData();
    }

    /**
     * @param artistName is the name of the artist
     * @return true if the artist is already in the 'artists' table, otherwise false
     */
    public boolean checkArtistExists(String artistName){
        for(Artist a: artists){
            if(a.getName().equalsIgnoreCase(artistName)) return true;
        }
        return false;
    }

    /**
     * @param albumName is the name of the album
     * @return true if the album is already in the 'albums' table, otherwise false
     */
    public boolean checkAlbumExists(String albumName){
        for(Album a: albums){
            if(a.getName().equalsIgnoreCase(albumName)) return true;
        }
        return false;
    }

}
