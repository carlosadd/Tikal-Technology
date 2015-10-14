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
package technology.tikal.ventas.controller.producto.imp;

import technology.tikal.ventas.model.producto.Producto;
import technology.tikal.ventas.model.producto.ProductoDeLinea;
import technology.tikal.ventas.model.producto.ofy.CatalogoOfy;
import technology.tikal.ventas.model.producto.ofy.LineaDeProductosOfy;
import technology.tikal.ventas.model.producto.ofy.ProductoDeLineaOfy;
import technology.tikal.ventas.model.producto.ofy.ProductoOfy;
import technology.tikal.ventas.model.producto.ofy.intermediario.ProductoConTalla;
import technology.tikal.ventas.model.producto.ofy.intermediario.ProductoIntermediario;

/**
 * 
 * @author Nekorp
 *
 */
public class ProductoFactory {

    public static ProductoOfy create(CatalogoOfy catalogo, Producto source) {
        if (source instanceof ProductoIntermediario) {
            return new ProductoIntermediario(catalogo);
        }
        throw new IllegalArgumentException();
    }
    
    public static ProductoDeLineaOfy create(CatalogoOfy catalogo, LineaDeProductosOfy lineaDeProducto,  ProductoDeLinea source) {
        if (source instanceof ProductoConTalla) {
            return new ProductoConTalla(lineaDeProducto, catalogo);
        }
        throw new IllegalArgumentException();
    }

    public static ProductoIntermediario clone(ProductoIntermediario objA) {
        ProductoIntermediario response = new ProductoIntermediario(objA);
        response.update(objA);
        return response;
    }

    public static ProductoConTalla clone(ProductoConTalla objA) {
        ProductoConTalla response = new ProductoConTalla(objA);
        response.update(objA);
        return response;
    }
}
