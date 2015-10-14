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
package technology.tikal.ventas.model.producto.ofy;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.producto.Catalogo;
import technology.tikal.ventas.model.producto.LineaDeProductos;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
@Cache
public class LineaDeProductosOfy implements LineaDeProductos, OfyEntity<LineaDeProductos> {

    @Parent @JsonIgnore
    private Key<Catalogo> owner;
    @Id
    private Long id;
    
    private Date fechaDeCreacion;
    
    protected LineaDeProductosOfy() {
        super();
        this.fechaDeCreacion = new Date();
    }
    
    protected LineaDeProductosOfy(LineaDeProductosOfy base) {
        this();
        if (base == null) {
            throw new NullPointerException();
        }
        if (base.owner == null) {
            throw new IllegalArgumentException();
        }
        this.owner = base.owner;
    }
    
    public LineaDeProductosOfy(Catalogo owner) {
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
    public Long getCatalogoId() {
        return owner.getId();
    }
    
    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    @Override
    public void update(LineaDeProductos source) {
        BeanUtils.copyProperties(source, this);
    }

}
