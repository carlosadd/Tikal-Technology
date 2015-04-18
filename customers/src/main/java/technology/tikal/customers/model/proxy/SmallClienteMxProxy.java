package technology.tikal.customers.model.proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import technology.tikal.customers.model.ClienteMx;
import technology.tikal.customers.model.address.Address;
import technology.tikal.customers.model.contact.PrimaryContact;
import technology.tikal.customers.model.name.Name;

public class SmallClienteMxProxy implements ClienteMx, CustomerProxy {

    @JsonIgnore
    private ClienteMx delegate;
    
    public SmallClienteMxProxy(ClienteMx delegate) {
        this.delegate = delegate;
    }
    @Override
    public Long getId() {
        return delegate.getId();
    }

    @Override
    public Name getName() {
        return delegate.getName();
    }

    @Override
    public void setName(Name name) {
        delegate.setName(name);
    }

    @Override
    public PrimaryContact getPrimaryContact() {
        return null;
    }

    @Override
    public String getRfc() {
        return null;
    }

    @Override
    public void setRfc(String rfc) {
        delegate.setRfc(rfc);
        
    }

    @Override
    public Address getDomicilioFiscal() {
        return null;
    }

    @Override
    public void setDomicilioFiscal(Address domicilioFiscal) {
        delegate.setDomicilioFiscal(domicilioFiscal);
    }

}
