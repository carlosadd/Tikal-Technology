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

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.pedido.Pedido;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
@Cache
public class PedidoOfy implements Pedido, OfyEntity<Pedido>{

    @Id
    private Long id;
    
    private Date fechaDeCreacion;
    
    public PedidoOfy() {
        this.fechaDeCreacion = new Date();
    }
    
    @Override
    public Long getId() {
        return id;
    }
    
    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    @Override
    public void update(Pedido source) {
        BeanUtils.copyProperties(source, this);
    }   
}
