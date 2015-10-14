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
package technology.tikal.ventas.model.pedido.ofy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;

import technology.tikal.ventas.model.pedido.PedidoCompuesto;
import technology.tikal.ventas.model.pedido.SubPedido;

/**
 * 
 * @author Nekorp
 *
 */
@Subclass(index=true)
public class SubPedidoOfy extends PedidoOfy implements SubPedido {

    @Index 
    @JsonIgnore
    private Ref<PedidoCompuesto> owner;
    
    protected SubPedidoOfy() {
        super();
    }
    
    public SubPedidoOfy(SubPedidoOfy base) {
        this();
        if (base.owner == null) {
            throw new NullPointerException();
        }
        this.owner = base.owner;
    }
    
    public SubPedidoOfy(PedidoCompuesto owner) {
        this();
        if (owner == null) {
            throw new NullPointerException();
        }
        if (owner.getId() == null) {
            throw new IllegalArgumentException();
        }
        this.owner = Ref.create(owner);
    }
}
