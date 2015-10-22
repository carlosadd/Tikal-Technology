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
package technology.tikal.ventas.controller.pedimento.imp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.controller.pedido.PedidoController;
import technology.tikal.ventas.controller.pedimento.PedimentoController;
import technology.tikal.ventas.controller.producto.ProductoController;
import technology.tikal.ventas.dao.pedimento.PedimentoDao;
import technology.tikal.ventas.dao.pedimento.PedimentoFilter;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.pedimento.GrupoPedimento;
import technology.tikal.ventas.model.pedimento.Pedimento;
import technology.tikal.ventas.model.pedimento.ofy.PedimentoOfy;
import technology.tikal.ventas.model.producto.Producto;
import technology.tikal.ventas.model.producto.ProductoDeLinea;

/**
 * 
 * @author Nekorp
 *
 */
public class PedimentoControllerImp implements PedimentoController {

    private PedimentoDao pedimentoDao;
    private PedidoController pedidoController;
    private ProductoController productoController;
    
    @Override
    public Pedimento crear(Long pedidoId, Pedimento request) {
        Pedido pedido = pedidoController.get(pedidoId);
        Producto producto = productoController.cargar(request.getProducto().getCatalogoId(), request.getProducto().getId());
        //se revisa que no exista otro para el mismo producto y proveedor
        PaginationDataLong pagination = new PaginationDataLong();
        PedimentoFilter filtro = new PedimentoFilter();
        filtro.setProducto(producto);
        filtro.setIdProveedor(request.getIdProveedor());
        List<PedimentoOfy> existentes = pedimentoDao.consultarTodos(pedido, filtro, pagination);
        if (existentes.size() > 0) {
            if (existentes.size() > 1) {
                //el caos
            }
            PedimentoOfy existente = existentes.get(0);
            existente.setCantidad(request.getCantidad() + existente.getCantidad());
            pedimentoDao.guardar(pedido, existente);
            return existente;
        } else {
            //continua con la creacion
            PedimentoOfy nuevo = PedimentoFactory.build(pedido, request, producto);
            nuevo.update(request);
            pedimentoDao.guardar(pedido, nuevo);
            return nuevo;
        }
    }

    @Override
    public void actualizar(Long pedidoId, Long pedimentoId, Pedimento request) {
        Pedido pedido = pedidoController.get(pedidoId);
        PedimentoOfy original = pedimentoDao.consultar(pedido, pedimentoId);
        original.update(request);
        pedimentoDao.guardar(pedido, original);
    }

    @Override
    public Pedimento[] consultar(Long pedidoId, PedimentoFilter filter, PaginationDataLong pagination) {
        Pedido pedido = pedidoController.get(pedidoId);
        List<PedimentoOfy> consulta = pedimentoDao.consultarTodos(pedido, filter, pagination);
        return sortByGroup(consulta);
    }
    
    private Pedimento[] sortByGroup(List<PedimentoOfy> source) {
        List<GrupoPedimento> grupos = new ArrayList<>();
        List<Pedimento> result = new LinkedList<>();
        for (PedimentoOfy x: source) {
            if (x.getProducto() instanceof ProductoDeLinea) {
                ProductoDeLinea p = (ProductoDeLinea) x.getProducto();
                GrupoPedimento grupo = new GrupoPedimento(x.getPedidoId(), x.getIdProveedor(), p.getLineaDeProductos());
                int index = grupos.indexOf(grupo);
                if (index < 0) {
                    result.add(grupo);
                    grupos.add(grupo);
                } else {
                    grupo = grupos.get(index);
                }
                grupo.updateFechaCreacion(x.getFechaDeCreacion());
                grupo.addPedimento(x);
            } else {
                result.add(x);
            }
        }
        Pedimento[] response = new Pedimento[result.size()];
        result.toArray(response);
        return response;
    }

    @Override
    public Pedimento get(Long pedidoId, Long pedimentoId) {
        Pedido pedido = pedidoController.get(pedidoId);
        return pedimentoDao.consultar(pedido, pedimentoId);
    }

    @Override
    public void borrar(Long pedidoId, Long pedimentoId) {
        Pedido pedido = pedidoController.get(pedidoId);
        PedimentoOfy target = pedimentoDao.consultar(pedido, pedimentoId);
        pedimentoDao.borrar(pedido, target);
    }

    public void setPedimentoDao(PedimentoDao pedimentoDao) {
        this.pedimentoDao = pedimentoDao;
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

    public void setProductoController(ProductoController productoController) {
        this.productoController = productoController;
    }    
}
