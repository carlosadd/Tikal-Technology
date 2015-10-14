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
package technology.tikal.ventas.controller.producto;

import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
public interface ProductoController {

    Producto crear(Long idCatalogo, Producto request);

    void actualizar(Long idCatalogo, Long productoId, Producto request);

    Producto[] consultar(Long idCatalogo, PaginationDataLong pagination);

    Producto cargar(Long idCatalogo, Long productoId);

    void borrar(Long idCatalogo, Long productoId);

}
