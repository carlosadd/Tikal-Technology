package technology.tikal.customers.model;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.googlecode.objectify.annotation.Subclass;

import technology.tikal.customers.model.address.Address;
import technology.tikal.customers.model.address.AddressFactory;
import technology.tikal.customers.model.proxy.CustomerProxy;
import technology.tikal.customers.model.proxy.SmallClienteMxProxy;
import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;

@Subclass
public class ClienteMxOfy extends CustomerOfy implements ClienteMx {

    private Address domicilioFiscal;
    private String rfc;
    
    protected ClienteMxOfy() {
        super();
    }
    
    public ClienteMxOfy(Customer source) {
        super();
        this.update(source);
    }
    
    public ClienteMxOfy(Long id, Customer source) {
        super(id);
        this.update(source);
    }

    @Override
    public String getRfc() {
        return rfc;
    }

    @Override
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @Override
    public Address getDomicilioFiscal() {
        return this.domicilioFiscal;
    }

    @Override
    public void setDomicilioFiscal(Address domicilioFiscal) {
        this.domicilioFiscal = domicilioFiscal;
    }

    @Override
    public void update(Customer source) {
        if (source instanceof ClienteMx) {
            super.update(source);
            ClienteMx cliente = (ClienteMx) source;
            this.setDomicilioFiscal(AddressFactory.buildInternal(cliente.getDomicilioFiscal()));
            this.setRfc(cliente.getRfc());
        } else {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotSupportedClass.ClienteMxOfy.update"}, 
                    new String[]{source.getClass().getSimpleName()}, 
                    "ClienteMx object expected"));
        }
    }
    
    @Override
    public CustomerProxy buildProxy() {
        return new SmallClienteMxProxy(this);
    }
}
