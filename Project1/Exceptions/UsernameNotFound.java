package Project.Exceptions;

public class UsernameNotFound extends Exception{
    public UsernameNotFound(){
        super("There is no such user!");
    }
}
