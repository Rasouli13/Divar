package Project.Exceptions;

public class InvalidAddress extends Exception{
    public InvalidAddress(){
        super("Address must be like-> "+'"'+"Tehran: Shahriari Square,..."+'"');
    }
}
