package app.grandeyardera.com.grandeyardera.model;

/**
 * Created by 13118467271 on 2017/8/23.
 */

public class User {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userSchool;
    private String userNumber;

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserSchool() {
        return userSchool;
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

    public String getUserNumber() {
        return userNumber;
    }


    public void setUserNumber(String userNuimber) {
        this.userNumber = userNuimber;
    }
}
