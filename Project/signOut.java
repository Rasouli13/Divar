package Project;

public class signOut extends User{
    signOut(){
       reset();
    }
    void reset(){
        this.username = "";
        this.password = "";
        this.phoneNumber = "";
        this.email = "";
        this.fullName = "";
    }
}
