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

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.googlecode.objectify.NotFoundException;

import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.controller.pedido.PartidaController;
import technology.tikal.ventas.controller.pedido.PedidoRaizController;
import technology.tikal.ventas.controller.pedido.SubPedidoController;
import technology.tikal.ventas.dao.pedido.PedidoDao;
import technology.tikal.ventas.dao.pedido.PedidoFilter;
import technology.tikal.ventas.model.pedido.Partida;
import technology.tikal.ventas.model.pedido.PedidoCompuesto;
import technology.tikal.ventas.model.pedido.PedidoRaiz;
import technology.tikal.ventas.model.pedido.SubPedido;
import technology.tikal.ventas.model.pedido.ofy.PedidoOfy;
import technology.tikal.ventas.model.pedido.ofy.PedidoRaizOfy;
import technology.tikal.ventas.model.pedido.ofy.SubPedidoOfy;

/**
 * 
 * @author Nekorp
 *
 */
public class SubPedidoControllerImp implements SubPedidoController {

    private PedidoRaizController pedidoRaizController;
    private PartidaController partidaController;
    private PedidoDao pedidoDao;
    @Override
    public SubPedido crear(Long pedidoId, SubPedido request) {
        if (request.getId() != null) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"NoValidRequest.SubPedidoControllerImp.crear"}, 
                new String[]{request.getId() + ""}, 
                "El pedido no debe tener id en la creacion"));
        }
        PedidoRaiz pedidoRaiz = pedidoRaizController.get(pedidoId);
        if (pedidoRaiz instanceof PedidoCompuesto) {
            SubPedidoOfy nuevo = PedidoFactory.createSubPedido(request, (PedidoCompuesto) pedidoRaiz);
            nuevo.update(request);
            pedidoDao.guardar(nuevo);
            return nuevo;
        } else {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"NoValidPedido.SubPedidoControllerImp.crear"}, 
                new String[]{pedidoId + ""}, 
                "El pedido no es compuesto"));
        }
    }

    @Override
    public void actualizar(Long pedidoId, SubPedido request) {
        SubPedidoOfy original = (SubPedidoOfy) this.get(pedidoId, request.getId());
        original.update(request);
        pedidoDao.guardar(original);
    }

    @Override
    public SubPedido[] consultar(Long pedidoId, PaginationDataLong pagination) {
        PedidoRaiz pedidoRaiz = pedidoRaizController.get(pedidoId);
        PedidoFilter filter = new PedidoFilter();
        filter.setType(SubPedidoOfy.class);
        filter.setPedidoRaiz((PedidoRaizOfy) pedidoRaiz);
        List<PedidoOfy> pedidos = pedidoDao.consultarTodos(filter, pagination);
        SubPedido[] response = new SubPedido[pedidos.size()];
        pedidos.toArray(response);
        return response;
    }

    @Override
    public SubPedido get(Long pedidoId, Long subPedidoId) {
        PedidoOfy loaded = pedidoDao.consultar(subPedidoId);
        if (loaded instanceof SubPedido) {
            return (SubPedido) loaded;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void borrar(Long pedidoId, Long subPedidoId) {
        SubPedidoOfy original = (SubPedidoOfy) this.get(pedidoId, subPedidoId);
        PaginationDataLong pagination = new PaginationDataLong();
        pagination.setMaxResults(1);
        Partida[] partidas = partidaController.consultar(subPedidoId, pagination);
        if (partidas.length > 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"ConPartidas.SubPedidoControllerImp.borrar"}, 
                new String[]{pedidoId + ""}, 
                "El Pedido no esta vacio"));
        }
        this.pedidoDao.borrar(original);
    }

    public void setPedidoRaizController(PedidoRaizController pedidoRaizController) {
        this.pedidoRaizController = pedidoRaizController;
    }

    public void setPedidoDao(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }

    public void setPartidaController(PartidaController partidaController) {
        this.partidaController = partidaController;
    }
}
