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
package technology.tikal.ventas.controller.envio.imp;

import technology.tikal.ventas.model.envio.Envio;
import technology.tikal.ventas.model.envio.ofy.EnvioOfy;
import technology.tikal.ventas.model.envio.ofy.EnvioTransient;
import technology.tikal.ventas.model.pedido.Pedido;

/**
 * 
 * @author Nekorp
 *
 */
public class EnvioFactory {

    public static EnvioOfy build(Pedido owner, Envio request) {
        if (request instanceof EnvioTransient) {
            EnvioOfy nuevo = new EnvioOfy(owner);
            nuevo.update(request);
            return nuevo;
        }
        throw new IllegalArgumentException();
    }

    public static EnvioOfy clone(EnvioOfy source) {
        if (source instanceof EnvioOfy) {
            EnvioOfy castedSource = (EnvioOfy) source;
            EnvioOfy response = new EnvioOfy(castedSource);
            response.update(castedSource);
            return response;
        }
        throw new IllegalArgumentException();
    }
}
