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

import technology.tikal.customers.model.address.Address;

/**
 * 
 * @author Nekorp
 *
 */
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
