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
package technology.tikal.customers.model.contact;

import com.googlecode.objectify.annotation.Subclass;

import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.contact.proxy.ContactProxy;
import technology.tikal.customers.model.contact.proxy.SmallPrimaryContactProxy;

/**
 * 
 * @author Nekorp
 *
 */
@Subclass
public class PrimaryContactOfy extends ContactOfy implements PrimaryContact {

    protected PrimaryContactOfy() {
        super();
        super.setIndexedName(false);
    }
    
    public PrimaryContactOfy(CustomerOfy owner) {
        super(owner);
        super.setIndexedName(false);
    }

    public PrimaryContactOfy(CustomerOfy owner, Contact source) {
        this(owner);
        this.update(source);
    }
    
    @Override
    public ContactProxy buildProxy() {
        return new SmallPrimaryContactProxy(this);
    }
}
