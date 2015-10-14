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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.producto.Catalogo;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
@Cache
@JsonTypeName("CatalogoOfy")
public class CatalogoOfy implements Catalogo, OfyEntity<Catalogo>{
    @Id
    private Long id;
    @NotNull
    @Length(max=40)
    @Pattern(regexp="^[\\w\\p{IsLatin}\\p{Punct}]+( [\\w\\p{IsLatin}\\p{Punct}]+)*")
    private String nombre;
    private Date fechaDeCreacion;
    public CatalogoOfy() {
        super();
        this.fechaDeCreacion = new Date();
    }
    @Override
    public Long getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }    
    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }
    @Override
    public void update(Catalogo source) {
        BeanUtils.copyProperties(source, this);
    }
}
