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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;

import technology.tikal.ventas.model.producto.Catalogo;
import technology.tikal.ventas.model.producto.LineaDeProductos;
import technology.tikal.ventas.model.producto.ProductoDeLinea;

/**
 * 
 * @author Nekorp
 *
 */
@Subclass(index=true)
public class ProductoDeLineaOfy extends AbstractProductoOfy implements ProductoDeLinea {

    @Index 
    @JsonIgnore
    private Ref<LineaDeProductos> refLinea;
    protected ProductoDeLineaOfy() {
        super();
    }
    
    public ProductoDeLineaOfy(ProductoDeLineaOfy base) {
        super(base);
        if (base.refLinea == null) {
            throw new NullPointerException();
        }
        this.refLinea = base.refLinea;
    }

    public ProductoDeLineaOfy(LineaDeProductos linea, Catalogo owner) {
        super(owner);
        if (linea == null) {
            throw new NullPointerException();
        }
        if (linea.getId() == null) {
            throw new IllegalArgumentException();
        }
        this.refLinea = Ref.create(linea);
    }

    @JsonIgnore
    @Override
    public LineaDeProductos getLineaDeProductos() {
        return refLinea.get();
    }
    
    public Long getLineaDeProductosId() {
        return refLinea.getKey().getId();
    }
}
