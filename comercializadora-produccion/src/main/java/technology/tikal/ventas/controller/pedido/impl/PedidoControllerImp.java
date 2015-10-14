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
package technology.tikal.ventas.controller.pedido.impl;

import technology.tikal.ventas.controller.pedido.PedidoController;
import technology.tikal.ventas.dao.pedido.PedidoDao;
import technology.tikal.ventas.model.pedido.Pedido;

/**
 * 
 * @author Nekorp
 *
 */
public class PedidoControllerImp implements PedidoController {

    private PedidoDao pedidoDao;
    @Override
    public Pedido get(Long pedidoId) {
        return pedidoDao.consultar(pedidoId);
    }
    public void setPedidoDao(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }    
}
