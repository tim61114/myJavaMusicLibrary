public class Song {
    protected int SongID;
    protected String name;
    protected Artist artist;
    protected Album album;
    protected int length;

    /**
     * @param id is the songID in the db
     * @param name is the songName
     * @param artistName is to store the name of the artist, and will then be linked to its album in the library when reading from the db.
     * @param albumName is to store the name of teh album, and will then be linked to its album in the library when reading from the db.
     * @param length is the length of the song.
     */
    public Song(int id, String name, String artistName, String albumName, int length){
        SongID = id;
        this.name = name;
        artist = new Artist(artistName);
        album = new Album(albumName);
        this.length = length;
    }

    /**
     * This constructor is used when id is unknown, which is when a song is being imported from musicbrainz instead of our db.
     */
    public Song(String name, String artistName, String albumName, int length){
        this.name = name;
        artist = new Artist(artistName);
        album = new Album(albumName);
        this.length = length;
    }

    @Override
    public String toString() {
        return "Song{" +
                "SongID=" + SongID +
                ", name='" + name + '\'' +
                ", artist=" + artist +
                ", album=" + album +
                ", length=" + length +
                '}';
    }

    /**
     * @return the data of the song to be shown
     */
    public String showSong(){
        return "\"" + name + "\" " + "by " + artist + " in Album " + album;
    }


    /**
     * @return the song data in a SQL format
     */
    public String toSQL(){
        return ("'"+name.replaceAll("'","''")+"','"+artist.getName().replaceAll("'","''")+"','"+album.getName().replaceAll("'","''")+"',"+length);
    }

    public int getSongID(){
        return SongID;
    }

    public String getName(){
        return name;
    }

    public Artist getArtist(){
        return artist;
    }

    public Album getAlbum() { return album; }


    /**
     * @param pause is indicating the current playing state(play or pause)
     * this will show the data of the song that is currently playing
     */
    public void play(Boolean pause){
        if(pause){
            System.out.println("Now playing: "+showSong() + " (Paused)");
        } else {
            System.out.println("Now playing: "+showSong());
        }

    }
}
