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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.controller.pedido.PartidaController;
import technology.tikal.ventas.controller.pedido.PedidoController;
import technology.tikal.ventas.controller.pedimento.PedimentoController;
import technology.tikal.ventas.controller.producto.ProductoController;
import technology.tikal.ventas.dao.pedido.PartidaDao;
import technology.tikal.ventas.dao.pedido.PartidaFilter;
import technology.tikal.ventas.dao.pedimento.PedimentoFilter;
import technology.tikal.ventas.model.pedido.GrupoPartida;
import technology.tikal.ventas.model.pedido.Partida;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.pedido.ofy.PartidaOfy;
import technology.tikal.ventas.model.pedimento.Pedimento;
import technology.tikal.ventas.model.producto.Producto;
import technology.tikal.ventas.model.producto.ProductoDeLinea;

/**
 * 
 * @author Nekorp
 *
 */
public class PartidaControllerImp implements PartidaController {

    private PartidaDao partidaDao;
    private PedidoController pedidoController;
    private ProductoController productoController;
    private PedimentoController pedimentoController;
    
    @Override
    public Partida crear(Long pedidoId, Partida request) {
        Pedido pedido = pedidoController.get(pedidoId);
        Producto producto = productoController.cargar(request.getProducto().getCatalogoId(), request.getProducto().getId());
        //revisa que no exista otra partida con el mismo producto
        PaginationDataLong pagination = new PaginationDataLong();
        PartidaFilter filtro = new PartidaFilter();
        filtro.setProducto(producto);
        List<PartidaOfy> existentes = partidaDao.consultarTodos(pedido, filtro, pagination);
        if (existentes.size() > 0) {
            if (existentes.size() > 1) {
                //el caos
            }
            PartidaOfy existente = existentes.get(0);
            existente.setCantidad(request.getCantidad() + existente.getCantidad());
            partidaDao.guardar(pedido, existente);
            return existente;
        } else {
            //continua con la creacion
            request.setProducto(producto);
            PartidaOfy nuevo = PartidaFactory.build(pedido, request);
            nuevo.update(request);
            partidaDao.guardar(pedido, nuevo);
            return nuevo;
        }
    }

    @Override
    public void actualizar(Long pedidoId, Long partidaId, Partida request) {
        Pedido pedido = pedidoController.get(pedidoId);
        Producto producto = productoController.cargar(request.getProducto().getCatalogoId(), request.getProducto().getId());
        request.setProducto(producto);
        PartidaOfy original = partidaDao.consultar(pedido, partidaId);
        original.update(request);
        partidaDao.guardar(pedido, original);
    }

    @Override
    public Partida[] consultar(Long pedidoId, PaginationDataLong pagination) {
        Pedido pedido = pedidoController.get(pedidoId);
        List<PartidaOfy> consulta = partidaDao.consultarTodos(pedido, null, pagination);
        return splitByGroup(consulta);
    }
    
    private Partida[] splitByGroup(List<PartidaOfy> consulta) {
        List<GrupoPartida> grupos = new ArrayList<>();
        List<Partida> result = new LinkedList<>();
        for (PartidaOfy x: consulta) {
            if (x.getProducto() instanceof ProductoDeLinea) {
                ProductoDeLinea p = (ProductoDeLinea) x.getProducto();
                GrupoPartida grupo = new GrupoPartida(x.getPedidoId(), p.getLineaDeProductos());
                int index = grupos.indexOf(grupo);
                if (index < 0) {
                    result.add(grupo);
                    grupos.add(grupo);
                } else {
                    grupo = grupos.get(index);
                }
                grupo.updateFechaCreacion(x.getFechaDeCreacion());
                grupo.addPartida(x);
            } else {
                result.add(x);
            }
        }
        Partida[] response = new Partida[result.size()];
        result.toArray(response);
        return response;
    }

    @Override
    public Partida get(Long pedidoId, Long partidaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        return partidaDao.consultar(pedido, partidaId);
    }

    @Override
    public void borrar(Long pedidoId, Long partidaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        PartidaOfy target = partidaDao.consultar(pedido, partidaId);
        PaginationDataLong pagination= new PaginationDataLong();
        pagination.setMaxResults(1);
        PedimentoFilter filter = new PedimentoFilter();
        filter.setProducto(target.getProducto());
        Pedimento[] pedimentos = pedimentoController.consultar(pedidoId, filter, pagination);
        if (pedimentos.length > 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"ConPedimentosParaElProducto.PartidaControllerImp.borrar"}, 
                new String[]{pedidoId + ""}, 
                "Hay pedimentos para el mismo producto"));
        }
        partidaDao.borrar(pedido, target);
    }

    public void setPartidaDao(PartidaDao partidaDao) {
        this.partidaDao = partidaDao;
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

    public void setProductoController(ProductoController productoController) {
        this.productoController = productoController;
    }

    public void setPedimentoController(PedimentoController pedimentoController) {
        this.pedimentoController = pedimentoController;
    }
}
