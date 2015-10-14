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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.googlecode.objectify.annotation.Subclass;

import technology.tikal.ventas.model.producto.Catalogo;
import technology.tikal.ventas.model.producto.ofy.ProductoOfy;

/**
 * 
 * @author Nekorp
 *
 */
@Subclass(index=true)
@JsonTypeName("ProductoIntermediario")
public class ProductoIntermediario extends ProductoOfy {

    @NotNull
    @Valid
    private DatosGenerales datosGenerales;
    
    protected ProductoIntermediario() {
        super();
        this.datosGenerales = new DatosGenerales();
    }

    public ProductoIntermediario(Catalogo owner) {
        super(owner);
        this.datosGenerales = new DatosGenerales();
    }
    
    public ProductoIntermediario(ProductoIntermediario base) {
        super(base);
        this.datosGenerales = new DatosGenerales();
    }

    public DatosGenerales getDatosGenerales() {
        return datosGenerales;
    }

    public void setDatosGenerales(DatosGenerales datosGenerales) {
        this.datosGenerales = datosGenerales;
    }
    
}
