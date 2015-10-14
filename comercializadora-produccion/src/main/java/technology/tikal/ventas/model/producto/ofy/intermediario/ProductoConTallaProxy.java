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

import com.fasterxml.jackson.annotation.JsonTypeName;

import technology.tikal.ventas.model.producto.LineaDeProductos;
import technology.tikal.ventas.model.producto.ProductoDeLinea;

/**
 * 
 * @author Nekorp
 *
 */
@JsonTypeName("ProductoConTalla")
public class ProductoConTallaProxy implements ProductoDeLinea {

    private ProductoConTalla delegate;
    public ProductoConTallaProxy(ProductoConTalla delegate) {
        this.delegate = delegate;
    }
    @Override
    public Long getCatalogoId() {
        return this.delegate.getId();
    }

    @Override
    public Long getId() {
        return this.delegate.getId();
    }

    @Override
    public LineaDeProductos getLineaDeProductos() {
        return null;
    }
    
    public String getTalla() {
        return delegate.getTalla();
    }

}
