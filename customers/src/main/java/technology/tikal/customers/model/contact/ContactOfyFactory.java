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

import org.springframework.context.support.DefaultMessageSourceResolvable;

import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;

/**
 * 
 * @author Nekorp
 *
 */
public class ContactOfyFactory {

    public static ContactOfy build(CustomerOfy parent, Contact source) {
        if (source == null) {
            return null;
        }
        ContactOfy nuevo;
        if (source instanceof PrimaryContact) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"IllegalBuild.ContactOfyFactory.build.PrimaryContact"}, 
                    new String[]{PrimaryContact.class.getSimpleName()},
                    "Can't create PrimaryContact objects"));
        }
        if (source instanceof Associate) {
            Associate associateData = (Associate) source;
            nuevo = new AssociateOfy(parent, associateData);
        } else {
            nuevo = new ContactOfy(parent, source);
        }
        return nuevo;
    }
}
