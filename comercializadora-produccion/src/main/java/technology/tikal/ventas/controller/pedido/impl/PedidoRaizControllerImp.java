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

/**
 * 
 * @author Nekorp
 *
 */
public class PedidoRaizControllerImp implements PedidoRaizController {

    private PedidoDao pedidoDao;
    private SubPedidoController subPedidoController;
    private PartidaController partidaController;
    @Override
    public PedidoRaiz crear(PedidoRaiz request) {
        if (request.getId() != null) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"NoValidRequest.PedidoRaizControllerImp.crear"}, 
                new String[]{request.getId() + ""}, 
                "El pedido no debe tener id en la creacion"));
        }
        pedidoDao.guardar((PedidoRaizOfy)request);
        return request;
    }

    @Override
    public void actualizar(PedidoRaiz request) {
        PedidoRaizOfy original = (PedidoRaizOfy) this.get(request.getId());
        original.update(request);
        pedidoDao.guardar(original);
    }

    @Override
    public PedidoRaiz[] consultar(PaginationDataLong pagination) {
        PedidoFilter filter = new PedidoFilter();
        filter.setType(PedidoRaizOfy.class);
        List<PedidoOfy> pedidos = pedidoDao.consultarTodos(filter, pagination);
        PedidoRaiz[] response = new PedidoRaiz[pedidos.size()];
        pedidos.toArray(response);
        return response;
    }

    @Override
    public PedidoRaiz get(Long idPedido) {
        PedidoOfy consulta = pedidoDao.consultar(idPedido);
        if (consulta instanceof PedidoRaiz) {
            return (PedidoRaiz) consulta;
        }
        throw new NotFoundException();
    }
    
    @Override
    public void borrar(Long pedidoId) {
        PedidoRaizOfy original = (PedidoRaizOfy) this.get(pedidoId);
        if(original instanceof PedidoCompuesto) {
            //si es compuesto que no tenga subPedidos
            PaginationDataLong pagination = new PaginationDataLong();
            pagination.setMaxResults(1);
            SubPedido[] subPedidos = subPedidoController.consultar(original.getId(), pagination);
            if (subPedidos.length > 0) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"ConSubPedidos.PedidoRaizControllerImp.borrar"}, 
                    new String[]{pedidoId + ""}, 
                    "El Pedido no esta vacio"));
            }
        } else {
            // que no tenga partidas
            PaginationDataLong pagination = new PaginationDataLong();
            pagination.setMaxResults(1);
            Partida[] partidas = partidaController.consultar(pedidoId, pagination);
            if (partidas.length > 0) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"ConPartidas.PedidoRaizControllerImp.borrar"}, 
                    new String[]{pedidoId + ""}, 
                    "El Pedido no esta vacio"));
            }
        }
        this.pedidoDao.borrar(original);
    }

    public void setPedidoDao(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }

    public void setPartidaController(PartidaController partidaController) {
        this.partidaController = partidaController;
    }

    public void setSubPedidoController(SubPedidoController subPedidoController) {
        this.subPedidoController = subPedidoController;
    }
}
