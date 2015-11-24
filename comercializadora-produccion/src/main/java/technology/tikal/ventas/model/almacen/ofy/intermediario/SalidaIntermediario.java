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
package technology.tikal.ventas.model.almacen.ofy.intermediario;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.googlecode.objectify.annotation.Subclass;

import technology.tikal.ventas.model.almacen.ofy.SalidaOfy;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
@JsonTypeName("SalidaIntermediario")
@Subclass(index=true)
public class SalidaIntermediario extends SalidaOfy {

    protected SalidaIntermediario() {
        super();
    }

    public SalidaIntermediario(SalidaIntermediario base) {
        super(base);
    }
    public SalidaIntermediario(Pedido owner, Producto producto) {
        //meh, no deveria ser obligatorio el proveedor para la salida
        super(owner, producto, 0L);
    }
    
}
