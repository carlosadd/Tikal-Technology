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
package technology.tikal.ventas.controller.almacen.cache.http;

import technology.tikal.gae.http.cache.AbstractCacheController;
import technology.tikal.gae.http.cache.UpdatePair;
import technology.tikal.ventas.controller.almacen.imp.RegistroAlmacenFactory;
import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenOfy;

/**
 * 
 * @author Nekorp
 *
 */
public class RegistroAlmacenCacheController extends AbstractCacheController<RegistroAlmacenOfy> {

    @Override
    public boolean haveChanges(UpdatePair<RegistroAlmacenOfy> pair) {
        if (pair.getOriginal().getCantidad().compareTo(pair.getUpdated().getCantidad()) != 0) {
            return true;
        }
        if (pair.getOriginal().getFechaRegistro().compareTo(pair.getUpdated().getFechaRegistro()) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof RegistroAlmacenOfy;
    }

    @Override
    public UpdatePair<RegistroAlmacenOfy> cloneObject(Object retVal) {
        RegistroAlmacenOfy objA = (RegistroAlmacenOfy) retVal;
        RegistroAlmacenOfy objB = RegistroAlmacenFactory.clone(objA);
        return new UpdatePair<RegistroAlmacenOfy>(objB, objA);
    }

}
