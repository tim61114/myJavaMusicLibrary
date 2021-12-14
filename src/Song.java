public class Song {
    protected int SongID;
    protected String name;
    protected Artist artist;
    protected Album album;
    protected int length;

    public Song(int id, String name, String artistName, String albumName, int length){
        SongID = id;
        this.name = name;
        artist = new Artist(artistName);
        album = new Album(albumName);
        this.length = length;
    }

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

    public String showSong(){
        return "\"" + name + "\" " + "by " + artist + " in Album " + album;
    }

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

    public void play(Boolean pause){
        if(pause){
            System.out.println("Now playing: "+showSong() + " (Paused)");
        } else {
            System.out.println("Now playing: "+showSong());
        }

    }
}
