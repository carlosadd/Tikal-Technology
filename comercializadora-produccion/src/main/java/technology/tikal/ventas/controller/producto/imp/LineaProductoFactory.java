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

import technology.tikal.ventas.model.producto.LineaDeProductos;
import technology.tikal.ventas.model.producto.ofy.CatalogoOfy;
import technology.tikal.ventas.model.producto.ofy.LineaDeProductosOfy;
import technology.tikal.ventas.model.producto.ofy.intermediario.LineaProductosPorTalla;

/**
 * 
 * @author Nekorp
 *
 */
public class LineaProductoFactory {

    public static LineaDeProductosOfy create(CatalogoOfy catalogo, LineaDeProductos source) {
        if (source instanceof LineaProductosPorTalla) {
            return new LineaProductosPorTalla(catalogo);
        }
        throw new IllegalArgumentException();
    }

    public static LineaProductosPorTalla clone(LineaProductosPorTalla objA) {
        LineaProductosPorTalla response = new LineaProductosPorTalla(objA);
        response.update(objA);
        return response;
    }
}
