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

import technology.tikal.ventas.model.pedido.Partida;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.pedido.ofy.PartidaOfy;
import technology.tikal.ventas.model.pedido.ofy.intermediario.PartidaIntermediario;
import technology.tikal.ventas.model.pedido.ofy.intermediario.PartidaIntermediarioTransient;

/**
 * 
 * @author Nekorp
 *
 */
public class PartidaFactory {

    public static PartidaOfy build(Pedido owner, Partida partida) {
        if (partida instanceof PartidaIntermediarioTransient) {
            PartidaIntermediario nuevo = new PartidaIntermediario(owner);
            return nuevo;
        }
        throw new IllegalArgumentException();
    }

    public static PartidaOfy clone(PartidaOfy source) {
        if (source instanceof PartidaIntermediario) {
            PartidaIntermediario castedSource = (PartidaIntermediario) source;
            PartidaIntermediario response = new PartidaIntermediario(castedSource);
            response.update(castedSource);
            return response;
        }
        throw new IllegalArgumentException();
    }
}
