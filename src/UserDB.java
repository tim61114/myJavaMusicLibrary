public class UserDB extends Database{


    public UserDB(String dbName) {
        super(dbName);
    }

    public int getNumUsers(){
        return countRows("users") - 1;
    }
    public boolean createNewUser(String username, String password){
        Query("insert into users values(" + (getNumUsers() + 1) + ",'"+username+"','"+password+"','"+username+".db',false)");
        return true;
    }


}
