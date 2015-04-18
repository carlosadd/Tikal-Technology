package technology.tikal.customers.model;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import technology.tikal.customers.model.address.Address;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=ClienteMxPojo.class)
@JsonTypeName("ClienteMx")
public interface ClienteMx extends Customer {

    @Size(max=13)
    @Pattern(regexp="\\w*")
    String getRfc();
    void setRfc(String rfc);
    @Valid
    Address getDomicilioFiscal();
    void setDomicilioFiscal(Address domicilioFiscal);
}
