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

import java.util.Date;

import org.springframework.beans.BeanUtils;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.pedido.Partida;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;
import technology.tikal.ventas.model.producto.ofy.AbstractProductoOfy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
public class PartidaOfy implements Partida, OfyEntity<Partida> {

    @Parent @JsonIgnore
    private Key<Pedido> owner;
    @Id
    private Long id;
    @Index
    private Date fechaDeCreacion;
    @Index
    private Ref<Producto> referenciaProducto;
    private Long cantidad;
    
    protected PartidaOfy() {
        super();
        this.fechaDeCreacion = new Date();
    }
    
    public PartidaOfy(PartidaOfy base) {
        this();
        if (base.owner == null) {
            throw new NullPointerException();
        }
        this.owner = base.owner;
    }
    public PartidaOfy(Pedido owner) {
        this();
        if (owner == null) {
            throw new NullPointerException();
        }
        if (owner.getId() == null) {
            throw new IllegalArgumentException();
        }
        this.owner = Key.create(owner);
    }
    
    @Override
    public Long getId() {
        return id;
    }
    
    @Override
    public Long getPedidoId() {
        return owner.getId();
    }

    @Override
    public Producto getProducto() {
        return this.referenciaProducto.get();
    }
    
    public void setProducto(Producto param) {
        if (param instanceof AbstractProductoOfy) {
            this.referenciaProducto = Ref.create(param);
        }
    }
    
    @Override
    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }   
    
    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }
    
    @Override
    public void update(Partida source) {
        BeanUtils.copyProperties(source, this, "owner");
    }
}
