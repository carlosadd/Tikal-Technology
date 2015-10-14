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
package technology.tikal.ventas.model.pedimento.ofy;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.pedimento.Pedimento;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
public class PedimentoOfy implements Pedimento, OfyEntity<Pedimento> {
    
    @Parent @JsonIgnore
    private Ref<Pedido> owner;
    @Id
    private Long id;
    @Index
    private Long idProveedor;
    @Index
    private Ref<Producto> producto;
    private Long cantidad;
    private Date fechaDeCreacion;
    
    protected PedimentoOfy() {
        this.fechaDeCreacion = new Date();
    }
    
    public PedimentoOfy(PedimentoOfy base) {
        this();
        if (base.owner == null) {
            throw new NullPointerException();
        }
        this.owner = base.owner;
        if (base.producto == null) {
            throw new NullPointerException();
        }
        this.producto = base.producto;
        this.idProveedor = base.idProveedor;
    }
    
    public PedimentoOfy(Pedido owner, Producto producto, Long idProveedor) {
        this();
        if (owner == null) {
            throw new NullPointerException();
        }
        if (owner.getId() == null) {
            throw new IllegalArgumentException();
        }
        this.owner = Ref.create(owner);
        if (producto == null) {
            throw new NullPointerException();
        }
        if (producto.getId() == null) {
            throw new IllegalArgumentException();
        }
        this.producto = Ref.create(producto);
        if (idProveedor == null) {
            throw new NullPointerException();
        }
        this.idProveedor = idProveedor;
    }
    
    @Override
    public Long getPedidoId() {
        return this.owner.getKey().getId();
    }   
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    @Override
    public Producto getProducto() {
        return producto.get();
    }
    
    @Override
    public Long getIdProveedor() {
        return idProveedor;
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
    public void update(Pedimento source) {
        BeanUtils.copyProperties(source, this, "owner", "producto", "idProveedor");
    }

}
