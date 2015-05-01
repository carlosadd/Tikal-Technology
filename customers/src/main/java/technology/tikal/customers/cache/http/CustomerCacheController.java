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
package technology.tikal.customers.cache.http;

import org.apache.commons.lang.StringUtils;

import technology.tikal.customers.model.ClienteMxOfy;
import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.CustomerOfyFactory;
import technology.tikal.gae.http.cache.AbstractCacheController;
import technology.tikal.gae.http.cache.UpdatePair;

/**
 * 
 * @author Nekorp
 *
 */
public class CustomerCacheController extends AbstractCacheController<CustomerOfy> {

    @Override
    public boolean haveChanges(UpdatePair<CustomerOfy> pair) {
        if (!pair.getOriginal().getName().equals(pair.getUpdated().getName())) {
            return true;
        }
        if(pair.getOriginal() instanceof ClienteMxOfy && pair.getUpdated() instanceof ClienteMxOfy) {
            ClienteMxOfy original = (ClienteMxOfy) pair.getOriginal();
            ClienteMxOfy updated = (ClienteMxOfy) pair.getUpdated();
            if (!StringUtils.equals(original.getRfc(), updated.getRfc())) {
                return true;
            }
        }
        
        if (pair.getOriginal().isActive() != pair.getUpdated().isActive()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof CustomerOfy;
    }

    @Override
    public UpdatePair<CustomerOfy> cloneObject(Object retVal) {
        CustomerOfy objA = (CustomerOfy) retVal;
        CustomerOfy objB = CustomerOfyFactory.buildInternal(objA);
        objB.update(objA);
        objB.setActive(objA.isActive());
        return new UpdatePair<CustomerOfy>(objB, objA);
    }

}
