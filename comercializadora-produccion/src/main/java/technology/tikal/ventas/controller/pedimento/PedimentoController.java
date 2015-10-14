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
package technology.tikal.ventas.controller.pedimento;

import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.dao.pedimento.PedimentoFilter;
import technology.tikal.ventas.model.pedimento.Pedimento;

/**
 * 
 * @author Nekorp
 *
 */
public interface PedimentoController {

    Pedimento crear(Long pedidoId, Pedimento request);

    void actualizar(Long pedidoId, Long pedimentoId, Pedimento request);

    Pedimento[] consultar(Long pedidoId, PedimentoFilter filter, PaginationDataLong pagination);

    Pedimento get(Long pedidoId, Long pedimentoId);

    void borrar(Long pedidoId, Long pedimentoId);    

}
