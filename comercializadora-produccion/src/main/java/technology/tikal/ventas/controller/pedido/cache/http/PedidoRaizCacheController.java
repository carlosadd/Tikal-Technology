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

import org.apache.commons.lang.StringUtils;

import technology.tikal.gae.http.cache.AbstractCacheController;
import technology.tikal.gae.http.cache.UpdatePair;
import technology.tikal.ventas.controller.pedido.impl.PedidoFactory;
import technology.tikal.ventas.model.pedido.ofy.PedidoRaizOfy;
import technology.tikal.ventas.model.pedido.ofy.intermediario.PedidoCompuestoIntermediario;
import technology.tikal.ventas.model.pedido.ofy.intermediario.PedidoIntermediario;

/**
 * 
 * @author Nekorp
 *
 */
public class PedidoRaizCacheController extends AbstractCacheController<PedidoRaizOfy> {

    @Override
    public boolean haveChanges(UpdatePair<PedidoRaizOfy> pair) {
        if (pair.getOriginal() instanceof PedidoIntermediario) {
            PedidoIntermediario a = (PedidoIntermediario) pair.getOriginal();
            PedidoIntermediario b = (PedidoIntermediario) pair.getUpdated();
            //para mejorar el rendimiento solo se comparara el nombre, aunque se deveria comparar completo
            if (!StringUtils.equals(a.getNombre(), b.getNombre())) {
                return true;
            }
            //TODO comparar el punto de entrega
            return false;
        }
        if (pair.getOriginal() instanceof PedidoCompuestoIntermediario) {
            PedidoCompuestoIntermediario a = (PedidoCompuestoIntermediario) pair.getOriginal();
            PedidoCompuestoIntermediario b = (PedidoCompuestoIntermediario) pair.getUpdated();
            if (!StringUtils.equals(a.getNombre(), b.getNombre())) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof PedidoRaizOfy;
    }

    @Override
    public UpdatePair<PedidoRaizOfy> cloneObject(Object retVal) {
        PedidoRaizOfy objA = (PedidoRaizOfy) retVal;
        PedidoRaizOfy objB = PedidoFactory.clone(objA);
        return new UpdatePair<PedidoRaizOfy>(objB, objA);
    }

}
