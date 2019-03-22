package li.yifei4.data.exceptions;

public class ConcurrentException extends RuntimeException {
    public ConcurrentException(){
        super("Concurrent Error");
    }
}
