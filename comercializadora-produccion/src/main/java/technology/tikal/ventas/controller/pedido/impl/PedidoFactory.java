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
package technology.tikal.ventas.controller.pedido.impl;

import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.pedido.PedidoCompuesto;
import technology.tikal.ventas.model.pedido.ofy.PedidoRaizOfy;
import technology.tikal.ventas.model.pedido.ofy.SubPedidoOfy;
import technology.tikal.ventas.model.pedido.ofy.intermediario.PedidoCompuestoIntermediario;
import technology.tikal.ventas.model.pedido.ofy.intermediario.PedidoIntermediario;
import technology.tikal.ventas.model.pedido.ofy.intermediario.SubPedidoIntermediario;

/**
 * 
 * @author Nekorp
 *
 */
public class PedidoFactory {

    public static SubPedidoOfy createSubPedido(Pedido source, PedidoCompuesto owner) {
        if (source instanceof SubPedidoIntermediario) {
            SubPedidoIntermediario r = new SubPedidoIntermediario(owner);
            return r;
        }
        throw new IllegalArgumentException();
    }

    public static PedidoRaizOfy clone(PedidoRaizOfy source) {
        if (source instanceof PedidoIntermediario) {
            PedidoIntermediario castedSource = (PedidoIntermediario) source;
            PedidoIntermediario response = new PedidoIntermediario();
            response.update(castedSource);
            return response;
        }
        if (source instanceof PedidoCompuestoIntermediario) {
            PedidoCompuestoIntermediario castedSource = (PedidoCompuestoIntermediario) source;
            PedidoCompuestoIntermediario response = new PedidoCompuestoIntermediario();
            response.update(castedSource);
            return response;
        }
        return null;
    }

    public static SubPedidoOfy clone(SubPedidoOfy source) {
        if (source instanceof SubPedidoIntermediario) {
            SubPedidoIntermediario castedSource = (SubPedidoIntermediario) source;
            SubPedidoIntermediario response = new SubPedidoIntermediario(castedSource);
            response.update(castedSource);
            return response;
        }
        return null;
    }
}
