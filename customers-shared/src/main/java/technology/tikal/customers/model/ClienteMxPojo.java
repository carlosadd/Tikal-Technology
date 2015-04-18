package technology.tikal.customers.model;

import technology.tikal.customers.model.address.Address;

public class ClienteMxPojo extends CustomerPojo implements ClienteMx {

    private Address domicilioFiscal;
    private String rfc;
    
    @Override
    public Address getDomicilioFiscal() {
        return domicilioFiscal;
    }
    @Override
    public void setDomicilioFiscal(Address domicilioFiscal) {
        this.domicilioFiscal = domicilioFiscal;
    }
    @Override
    public String getRfc() {
        return rfc;
    }
    @Override
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    
}
