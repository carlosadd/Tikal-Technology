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
package technology.tikal.ventas.controller.almacen;

import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.dao.almacen.RegistroAlmacenFilter;
import technology.tikal.ventas.model.almacen.EntradaDevolucion;
import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenTransient;

/**
 * 
 * @author Nekorp
 *
 */
public interface EntradaDevolucionController {

    EntradaDevolucion crear(Long pedidoId, RegistroAlmacenTransient request);

    void actualizar(Long pedidoId, Long registroId, RegistroAlmacenTransient request);

    EntradaDevolucion[] consultar(Long pedidoId, RegistroAlmacenFilter filter, PaginationDataLong pagination);

    EntradaDevolucion get(Long pedidoId, Long registroId);

    void borrar(Long pedidoId, Long registroId);

}
