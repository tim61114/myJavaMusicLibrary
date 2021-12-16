import java.io.File;

public class UserDB extends Database{
    private int userCount;


    public UserDB(String dbName) {
        super(dbName);
    }


    /**
     * @return the max id of the current users to prevent collision after deleting users.
     */
    public int getNumUsers(){
        return IDQuery("select id from users order by id desc limit 1");
    }

    /**
     * @param username is the username of the new user
     * @param password is the password of the new user
     * @return if the user is successfully created
     */
    public boolean createNewUser(String username, String password){
        Query("insert into users values(" + (getNumUsers() + 1) + ",'"+username+"','"+password+"','"+username+".db',false)");
        return true;
    }

    /**
     * @param username is to check if the username is in the user db
     * @return true if exists, false if it does not.
     */
    public boolean checkUsernameExists(String username){
        if(singleQuery("select username from users where username = '" + username + "'").size() == 0) {
            return false;
        }
        else return true;
    }

    /**
     * @param username is the username of the user
     * @return the password of the username
     */
    public String matchPassword(String username){
        return singleQuery("select password from users where username = '" + username + "'").get(0);
    }

    //admin method


    /**
     * @param username is limited to admin
     * @return true if the user is successfully deleted, otherwise false
     */
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
