/**
 *   Copyright 2015 Tikal-Technology
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */
package technology.tikal.customers.model;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.googlecode.objectify.annotation.Subclass;

import technology.tikal.customers.model.address.Address;
import technology.tikal.customers.model.address.AddressFactory;
import technology.tikal.customers.model.proxy.CustomerProxy;
import technology.tikal.customers.model.proxy.SmallClienteMxProxy;
import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;

/**
 * 
 * @author Nekorp
 *
 */
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
