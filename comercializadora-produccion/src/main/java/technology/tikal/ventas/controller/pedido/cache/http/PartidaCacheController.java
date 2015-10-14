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
package technology.tikal.ventas.controller.pedido.cache.http;

import technology.tikal.gae.http.cache.AbstractCacheController;
import technology.tikal.gae.http.cache.UpdatePair;
import technology.tikal.ventas.controller.pedido.impl.PartidaFactory;
import technology.tikal.ventas.model.pedido.ofy.PartidaOfy;
import technology.tikal.ventas.model.pedido.ofy.intermediario.PartidaIntermediario;

/**
 * 
 * @author Nekorp
 *
 */
public class PartidaCacheController extends AbstractCacheController<PartidaOfy> {

    @Override
    public boolean haveChanges(UpdatePair<PartidaOfy> pair) {
        if (pair.getOriginal() instanceof PartidaIntermediario) {
            PartidaIntermediario a = (PartidaIntermediario) pair.getOriginal();
            PartidaIntermediario b = (PartidaIntermediario) pair.getUpdated();
            if (a.getCantidad().compareTo(b.getCantidad()) != 0) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof PartidaOfy;
    }

    @Override
    public UpdatePair<PartidaOfy> cloneObject(Object retVal) {
        PartidaOfy objA = (PartidaOfy) retVal;
        PartidaOfy objB = PartidaFactory.clone(objA);
        return new UpdatePair<PartidaOfy>(objB, objA);
    }

}
