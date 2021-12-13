import java.io.File;

public class UserDB extends Database{
    private int userCount;


    public UserDB(String dbName) {
        super(dbName);
    }


    public int getNumUsers(){
        return IDQuery("select id from users order by id desc limit 1");
    }

    public boolean createNewUser(String username, String password){
        Query("insert into users values(" + (getNumUsers() + 1) + ",'"+username+"','"+password+"','"+username+".db',false)");
        return true;
    }

    public boolean checkUsernameExists(String username){
        if(singleQuery("select username from users where username = '" + username + "'").size() == 0) {
            return false;
        }
        else return true;
    }

    public String matchPassword(String username){
        return singleQuery("select password from users where username = '" + username + "'").get(0);
    }

    //admin method
    public boolean removeUser(String username){
        if(!username.equals("admin")){
            String sql = "delete from users where username = '"+username+"'";
            Query(sql);
            File file = new File("data/"+username);
            for(File subFile: file.listFiles()){
                subFile.delete();
            }
            file.delete();
            return true;
        }else return false;
    }


}
