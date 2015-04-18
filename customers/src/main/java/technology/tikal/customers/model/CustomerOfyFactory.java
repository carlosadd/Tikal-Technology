package technology.tikal.customers.model;

public class CustomerOfyFactory {

    public static CustomerOfy buildInternal(Customer source) {
        if (source instanceof ClienteMx) {
            ClienteMx clienteMx = (ClienteMx) source;
            return new ClienteMxOfy(clienteMx);
        }
        return new CustomerOfy(source);
    }
    
    public static CustomerOfy buildInternalWithForcedId(Long id, Customer source) {
        if (source instanceof ClienteMx) {
            ClienteMx clienteMx = (ClienteMx) source;
            return new ClienteMxOfy(id, clienteMx);
        }
        return new CustomerOfy(id, source);
    }
}
