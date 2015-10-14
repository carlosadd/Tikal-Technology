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
import technology.tikal.ventas.model.address.ofy.PuntoEntregaOfy;
import technology.tikal.ventas.model.pedido.ofy.SubPedidoOfy;
import technology.tikal.ventas.model.pedido.ofy.intermediario.SubPedidoIntermediario;

/**
 * 
 * @author Nekorp
 *
 */
public class SubPedidoCacheController extends AbstractCacheController<SubPedidoOfy> {

    @Override
    public boolean haveChanges(UpdatePair<SubPedidoOfy> pair) {
        if (pair.getOriginal() instanceof SubPedidoIntermediario) {
            SubPedidoIntermediario objA = (SubPedidoIntermediario) pair.getOriginal();
            SubPedidoIntermediario objB = (SubPedidoIntermediario) pair.getUpdated();
            if (objA.getPuntoEntrega() instanceof PuntoEntregaOfy) {
                PuntoEntregaOfy a = (PuntoEntregaOfy) objA.getPuntoEntrega();
                PuntoEntregaOfy b = (PuntoEntregaOfy) objB.getPuntoEntrega();
                //para mejorar el rendimiento solo se comparara el nombre y nombre corto, 
                //se deveria comparar tambien la direccion si se llega a usar en la consulta de todos los elementos
                if (!StringUtils.equals(a.getNombre(), b.getNombre())) {
                    return true;
                }
                if (!StringUtils.equals(a.getNombreCorto(), b.getNombreCorto())) {
                    return true;
                }
                return false;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof SubPedidoOfy;
    }

    @Override
    public UpdatePair<SubPedidoOfy> cloneObject(Object retVal) {
        SubPedidoOfy objA = (SubPedidoOfy) retVal;
        SubPedidoOfy objB = PedidoFactory.clone(objA);
        return new UpdatePair<SubPedidoOfy>(objB, objA);
    }

}
