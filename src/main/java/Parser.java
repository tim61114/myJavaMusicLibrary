import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;


public class Parser {

    DataFetcher df = new DataFetcher();
    public String getSongDescription(String artistName,String songName){
        String description = "";
        try {
            JSONParser p = new JSONParser();
            Object obj = p.parse(df.fetchJSONStringFromArtistAndSong(artistName,songName));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray arrayData = (JSONArray)jsonObject.get("track");
            JSONObject data = (JSONObject) arrayData.get(0);
            description = (String)data.get("strDescriptionEN");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return description.replaceAll("\n"," ");
    }

    public String getArtistDescription(String artistName){
        String description = "";
        try {
            JSONParser p = new JSONParser();
            Object obj = p.parse(df.fetchJSONStringFromArtist(artistName));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray arrayData = (JSONArray)jsonObject.get("artists");
            JSONObject data = (JSONObject) arrayData.get(0);
            description = (String)data.get("strBiographyEN");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return description.replaceAll("\n"," ");
    }

    public String getAlbumDescription(String artistName, String albumName){
        String description = "";
        try {
            JSONParser p = new JSONParser();
            Object obj = p.parse(df.fetchJSONStringFromArtistAndAlbum(artistName,albumName));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray arrayData = (JSONArray)jsonObject.get("album");
            JSONObject data = (JSONObject) arrayData.get(0);
            description = (String)data.get("strDescriptionEN");


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return description.replaceAll("\n"," ");
    }

    public List<Song> getFullAlbum(String[] a){
        List<Song> album= new ArrayList<>();
        String JSONData = df.fetchJSONAlbumFromID(a[0]);
        try{
            JSONParser p = new JSONParser();
            Object obj = p.parse(JSONData);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray data = (JSONArray)jsonObject.get("media");
            JSONObject media = (JSONObject) data.get(0);
            JSONArray albumData = (JSONArray) media.get("tracks");
            for(int i = 0;i < albumData.size();i++){
                JSONObject songData = (JSONObject) albumData.get(i);
                Song temp = new Song((String)songData.get("title"),a[2],a[1],((Long) songData.get("length")).intValue());
                album.add(temp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return album;
    }

    public static void main(String[] args) {
        Parser p = new Parser();
        //System.out.println(p.getSongDescription("Adele","Hello"));
        //System.out.println("----------------");
        //System.out.println(p.getAlbumDescription("Adele", "25"));
        //System.out.println("----------------");
        //System.out.println(p.getArtistDescription("Adele"));

        DataFetcher df = new DataFetcher();
        p.getFullAlbum(df.fetchAlbumID("seong-jin cho","debussy"));
        p.getFullAlbum(df.fetchAlbumID("adele","25"));

    }
}
