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
package technology.tikal.ventas.model.producto.ofy.intermediario;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.googlecode.objectify.annotation.Subclass;

import technology.tikal.ventas.model.producto.Catalogo;
import technology.tikal.ventas.model.producto.LineaDeProductos;
import technology.tikal.ventas.model.producto.ofy.ProductoDeLineaOfy;

/**
 * 
 * @author Nekorp
 *
 */
@Subclass(index=true)
@JsonTypeName("ProductoConTalla")
public class ProductoConTalla extends ProductoDeLineaOfy {

    @NotNull
    @Length(max=40)
    @Pattern(regexp="^[\\w\\p{IsLatin}\\p{Punct}]+( [\\w\\p{IsLatin}\\p{Punct}]+)*")
    private String talla;
    
    protected ProductoConTalla() {
        super();
    }

    public ProductoConTalla(ProductoConTalla base) {
        super(base);
    }
    
    public ProductoConTalla(LineaDeProductos linea, Catalogo owner) {
        super(linea, owner);
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }    
}
