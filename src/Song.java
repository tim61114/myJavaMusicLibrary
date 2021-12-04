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
}
