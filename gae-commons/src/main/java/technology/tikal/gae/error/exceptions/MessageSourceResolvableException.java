package technology.tikal.gae.error.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;

public class MessageSourceResolvableException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DefaultMessageSourceResolvable msgResolve;
    
    public MessageSourceResolvableException(DefaultMessageSourceResolvable msgResolve) {
        this.msgResolve = msgResolve;
    }

    public DefaultMessageSourceResolvable getMsgResolve() {
        return msgResolve;
    }

}
