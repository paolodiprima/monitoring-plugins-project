package threatarrest.monitoring.exceptions;

public class WrongArgumentsFormat extends Exception{
    public WrongArgumentsFormat(){
        super("wrong arguments format, it should be: <filename>:[<param_1><param_2>...<param_3>]");
    }
}
