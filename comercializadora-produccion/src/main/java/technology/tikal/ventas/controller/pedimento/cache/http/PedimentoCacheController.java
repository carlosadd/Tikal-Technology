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
package technology.tikal.ventas.controller.pedimento.cache.http;

import technology.tikal.gae.http.cache.AbstractCacheController;
import technology.tikal.gae.http.cache.UpdatePair;
import technology.tikal.ventas.controller.pedimento.imp.PedimentoFactory;
import technology.tikal.ventas.model.pedimento.ofy.PedimentoOfy;
import technology.tikal.ventas.model.pedimento.ofy.intermediario.PedimentoIntermediario;

/**
 * 
 * @author Nekorp
 *
 */
public class PedimentoCacheController extends AbstractCacheController<PedimentoOfy> {

    @Override
    public boolean haveChanges(UpdatePair<PedimentoOfy> pair) {
        if (pair.getOriginal() instanceof PedimentoIntermediario) {
            PedimentoIntermediario a = (PedimentoIntermediario) pair.getOriginal();
            PedimentoIntermediario b = (PedimentoIntermediario) pair.getUpdated();
            if (a.getCantidad().compareTo(b.getCantidad()) != 0) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof PedimentoOfy;
    }

    @Override
    public UpdatePair<PedimentoOfy> cloneObject(Object retVal) {
        PedimentoOfy objA = (PedimentoOfy) retVal;
        PedimentoOfy objB = PedimentoFactory.clone(objA);
        return new UpdatePair<PedimentoOfy>(objB, objA);
    }

}
