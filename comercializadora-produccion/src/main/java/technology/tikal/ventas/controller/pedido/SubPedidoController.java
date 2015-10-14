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
package technology.tikal.ventas.controller.pedido;

import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.model.pedido.SubPedido;

/**
 * 
 * @author Nekorp
 *
 */
public interface SubPedidoController {

    SubPedido crear(Long pedidoId, SubPedido request);

    void actualizar(Long pedidoId, SubPedido request);

    SubPedido[] consultar(Long pedidoId, PaginationDataLong pagination);

    SubPedido get(Long pedidoId, Long subPedidoId);

    void borrar(Long pedidoId, Long subPedidoId);

}
