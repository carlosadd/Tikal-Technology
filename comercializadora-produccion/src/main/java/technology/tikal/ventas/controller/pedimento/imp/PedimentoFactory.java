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
package technology.tikal.ventas.controller.pedimento.imp;

import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.pedimento.Pedimento;
import technology.tikal.ventas.model.pedimento.ofy.PedimentoOfy;
import technology.tikal.ventas.model.pedimento.ofy.intermediario.PedimentoIntermediario;
import technology.tikal.ventas.model.pedimento.ofy.intermediario.PedimentoIntermediarioTransient;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
public class PedimentoFactory {

    public static PedimentoOfy build(Pedido owner, Pedimento request, Producto producto) {
        if (request instanceof PedimentoIntermediarioTransient) {
            PedimentoIntermediario nuevo = new PedimentoIntermediario(owner, producto, request.getIdProveedor());
            nuevo.update(request);
            return nuevo;
        }
        throw new IllegalArgumentException();
    }

    public static PedimentoOfy clone(PedimentoOfy source) {
        if (source instanceof PedimentoIntermediario) {
            PedimentoIntermediario castedSource = (PedimentoIntermediario) source;
            PedimentoIntermediario response = new PedimentoIntermediario(castedSource);
            response.update(castedSource);
            return response;
        }
        throw new IllegalArgumentException();
    }
}
