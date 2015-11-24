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
package technology.tikal.ventas.controller.almacen.imp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.controller.almacen.EntradaController;
import technology.tikal.ventas.controller.almacen.SalidaDevolucionController;
import technology.tikal.ventas.controller.pedido.PedidoController;
import technology.tikal.ventas.controller.producto.ProductoController;
import technology.tikal.ventas.dao.almacen.EntradaDao;
import technology.tikal.ventas.dao.almacen.RegistroAlmacenFilter;
import technology.tikal.ventas.model.almacen.Entrada;
import technology.tikal.ventas.model.almacen.GrupoRegistroAlmacen;
import technology.tikal.ventas.model.almacen.SalidaDevolucion;
import technology.tikal.ventas.model.almacen.ofy.EntradaOfy;
import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenTransient;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;
import technology.tikal.ventas.model.producto.ProductoDeLinea;

/**
 * 
 * @author Nekorp
 *
 */
public class EntradaControllerImp implements EntradaController {

    private EntradaDao entradaDao;
    private PedidoController pedidoController;
    private ProductoController productoController;
    private SalidaDevolucionController salidaDevolucionController;
    
    @Override
    public Entrada crear(Long pedidoId, RegistroAlmacenTransient request) {
        Pedido pedido = pedidoController.get(pedidoId);
        Producto producto = productoController.cargar(request.getProducto().getCatalogoId(), request.getProducto().getId());
        EntradaOfy nuevo = RegistroAlmacenFactory.build(pedido, request, producto, EntradaOfy.class);
        entradaDao.guardar(pedido, nuevo);
        return nuevo;
    }

    @Override
    public void actualizar(Long pedidoId, Long entradaId, RegistroAlmacenTransient request) {
        Pedido pedido = pedidoController.get(pedidoId);
        EntradaOfy original = entradaDao.consultar(pedido, entradaId);
        original.setCantidad(request.getCantidad());
        original.setFechaRegistro(request.getFechaRegistro());
        original.setDescripcion(request.getDescripcion());
        original.setTag(request.getTag());
        entradaDao.guardar(pedido, original);
    }

    @Override
    public Entrada[] consultar(Long pedidoId, RegistroAlmacenFilter filter, PaginationDataLong pagination) {
        Pedido pedido = pedidoController.get(pedidoId);
        List<EntradaOfy> consulta = entradaDao.consultarTodos(pedido, filter, pagination);
        return sortByGroup(consulta);
    }
    
    private Entrada[] sortByGroup(List<EntradaOfy> source) {
        List<Entrada> result = new LinkedList<>();
        List<GrupoRegistroAlmacen> grupos = new ArrayList<>(); 
        for (EntradaOfy x: source) {
            if (x.getProducto() instanceof ProductoDeLinea) {
                ProductoDeLinea p = (ProductoDeLinea) x.getProducto();
                GrupoRegistroAlmacen grupo = new GrupoRegistroAlmacen(x.getPedidoId(), x.getIdProveedor(), p.getLineaDeProductos());
                int index = grupos.indexOf(grupo);
                if (index < 0) {
                    result.add(grupo);
                    grupos.add(grupo);
                } else {
                    grupo = grupos.get(index);
                }
                grupo.updateFechaCreacion(x.getFechaDeCreacion());
                grupo.addRegistro(x);
            } else {
                result.add(x);
            }
        }
        Entrada[] response = new Entrada[result.size()];
        result.toArray(response);
        return response;
    }

    @Override
    public Entrada get(Long pedidoId, Long entradaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        return entradaDao.consultar(pedido, entradaId);
    }

    @Override
    public void borrar(Long pedidoId, Long entradaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        EntradaOfy target = entradaDao.consultar(pedido, entradaId);
        RegistroAlmacenFilter filter = new RegistroAlmacenFilter();
        filter.setOrigen(target);
        PaginationDataLong pagination = new PaginationDataLong();
        pagination.setMaxResults(1);
        SalidaDevolucion[] referencias = salidaDevolucionController.consultar(pedidoId, filter, pagination);
        if (referencias.length > 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"Referenced.EntradaControllerImp.borrar"}, 
                new String[]{pedidoId + ""}, 
                "No se debe referenciar este registro"));
        }
        entradaDao.borrar(pedido, target);
    }

    public void setEntradaDao(EntradaDao entradaDao) {
        this.entradaDao = entradaDao;
    }

    public void setSalidaDevolucionController(
            SalidaDevolucionController salidaDevolucionController) {
        this.salidaDevolucionController = salidaDevolucionController;
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

    public void setProductoController(ProductoController productoController) {
        this.productoController = productoController;
    }
    
}
