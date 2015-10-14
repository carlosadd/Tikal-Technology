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
package technology.tikal.ventas.model.pedido.ofy.intermediario;

import com.fasterxml.jackson.annotation.JsonTypeName;

import technology.tikal.ventas.model.pedido.Partida;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
@JsonTypeName("PartidaIntermediarioTransient")
public class PartidaIntermediarioTransient implements Partida {
    
    private Long pedidoId;
    private Long id;
    private Long cantidad;
    private Producto producto;
    
    @Override
    public Long getCantidad() {
        return cantidad;
    }

    @Override
    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }    

    @Override
    public Producto getProducto() {
        return producto;
    }

    @Override
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }
    
    @Override
    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }
}
