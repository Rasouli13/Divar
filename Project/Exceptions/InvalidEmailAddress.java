package Project.Exceptions;

public class InvalidEmailAddress extends Exception{
    public InvalidEmailAddress(){
        super("Enter a valid Email: example.2@example.com");
    }
}
