package threatarrest.monitoring.exceptions;

public class MissingPluginsInputException extends Exception{
    public MissingPluginsInputException(){
        super("missing input arguments");
    }
}
