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
import technology.tikal.ventas.model.producto.Catalogo;

/**
 * 
 * @author Nekorp
 *
 */
public interface CatalogoController {

    Catalogo crear(Catalogo request);

    void actualizar(Long idCatalogo, Catalogo request);

    Catalogo[] consultar(PaginationDataLong pagination);

    Catalogo cargar(Long idCatalogo);

    void borrar(Long idCatalogo);

    
}
