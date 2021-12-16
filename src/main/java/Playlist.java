import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    protected String name;
    protected List<Song> songs;

    public Playlist(String name){
        this.name = name;
        songs = new ArrayList<>();
    }

    public void addSong(Song s){
        songs.add(s);
    }

    public String getName(){
        return name;
    }

    public void show(){
        songs.forEach(song -> System.out.println(song.showSong()));
    }

    public List<Song> getSongs(){
        return songs;
    }

    /**
     * @param currentUser is used for finding the correct data path for exporting the XML file.
     */
    public void export(String currentUser){
        try {
            File dir = new File("data/"+currentUser);
            dir.mkdirs();
            FileWriter wf = new FileWriter("data/"+currentUser+"/"+name+".xml");
            wf.write("<?xml version=\"1.0\"?>\n");
            wf.write("<playlist>\n");
            wf.write("<songs>\n");
            for(Song s: songs){
                wf.write("<song id=\""+s.getSongID()+"\">");
                wf.write("<title>");
                wf.write(s.getName());
                wf.write("</title>");
                wf.write("<artist id=\"" + s.getArtist().getArtistID() + "\">");
                wf.write(s.getArtist().getName());
                wf.write("</artist>");
                wf.write("<album id=\"" + s.getAlbum().getAlbumID() + "\">");
                wf.write(s.getAlbum().getName());
                wf.write("</album>");
                wf.write("</song>\n");
            }
            wf.write("</songs>\n");
            wf.write("</playlist>");
            wf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
