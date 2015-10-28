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

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.googlecode.objectify.NotFoundException;

import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.controller.almacen.EntradaDevolucionController;
import technology.tikal.ventas.controller.almacen.SalidaDevolucionController;
import technology.tikal.ventas.controller.pedido.PedidoController;
import technology.tikal.ventas.controller.producto.ProductoController;
import technology.tikal.ventas.dao.almacen.EntradaDevolucionDao;
import technology.tikal.ventas.dao.almacen.RegistroAlmacenFilter;
import technology.tikal.ventas.model.almacen.EntradaDevolucion;
import technology.tikal.ventas.model.almacen.RegistroAlmacen;
import technology.tikal.ventas.model.almacen.ofy.EntradaDevolucionOfy;
import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenTransient;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
public class EntradaDevolucionControllerImp implements EntradaDevolucionController {

    private EntradaDevolucionDao entradaDevolucionDao;
    private PedidoController pedidoController;
    private ProductoController productoController;
    private SalidaDevolucionController salidaDevolucionController;
    
    @Override
    public EntradaDevolucion crear(Long pedidoId, RegistroAlmacenTransient request) {
        Pedido pedido = pedidoController.get(pedidoId);
        Producto producto = productoController.cargar(request.getProducto().getCatalogoId(), request.getProducto().getId());
        RegistroAlmacen referencia = null;
        if (request.getReferenciaRegistro() != null) {
            try {
                referencia = salidaDevolucionController.get(pedidoId, request.getReferenciaRegistro());
            } catch (NotFoundException e) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                        new String[]{"RefNotFound.EntradaDevolucionControllerImp.crear"}, 
                        new String[]{pedidoId + ""}, 
                        "No se encontro la referencia"));
            }
        } else {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"RefRequired.EntradaDevolucionControllerImp.crear"}, 
                new String[]{pedidoId + ""}, 
                "No se encontro la referencia"));
        }
        EntradaDevolucionOfy nuevo = RegistroAlmacenFactory.buildDevolucion(pedido, request, producto, EntradaDevolucionOfy.class, referencia);
        entradaDevolucionDao.guardar(pedido, nuevo);
        return nuevo;
    }

    @Override
    public void actualizar(Long pedidoId, Long entradaId, RegistroAlmacenTransient request) {
        Pedido pedido = pedidoController.get(pedidoId);
        EntradaDevolucionOfy original = entradaDevolucionDao.consultar(pedido, entradaId);
        original.setCantidad(request.getCantidad());
        original.setFechaRegistro(request.getFechaRegistro());
        original.setDescripcion(request.getDescripcion());
        //de momento no se actualiza la referencia
        /*
        RegistroAlmacen referencia = null;
        if (request.getReferenciaRegistro() != null) {
            try {
                referencia = salidaDevolucionController.get(pedidoId, request.getReferenciaRegistro());
            } catch (NotFoundException e) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                        new String[]{"RefNotFound.EntradaDevolucionControllerImp.actualizar"}, 
                        new String[]{pedidoId + ""}, 
                        "No se encontro la referencia"));
            }
        } else {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"RefRequired.EntradaDevolucionControllerImp.actualizar"}, 
                new String[]{pedidoId + ""}, 
                "No se encontro la referencia"));
        }
        original.setOrigen(referencia);*/
        entradaDevolucionDao.guardar(pedido, original);
    }

    @Override
    public EntradaDevolucion[] consultar(Long pedidoId, RegistroAlmacenFilter filter, PaginationDataLong pagination) {
        Pedido pedido = pedidoController.get(pedidoId);
        List<EntradaDevolucionOfy> consulta = entradaDevolucionDao.consultarTodos(pedido, filter, pagination);
        EntradaDevolucion[] response = new EntradaDevolucion[consulta.size()];
        consulta.toArray(response);
        return response;
    }

    @Override
    public EntradaDevolucion get(Long pedidoId, Long entradaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        return entradaDevolucionDao.consultar(pedido, entradaId);
    }

    @Override
    public void borrar(Long pedidoId, Long entradaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        EntradaDevolucionOfy target = entradaDevolucionDao.consultar(pedido, entradaId);
        entradaDevolucionDao.borrar(pedido, target);
    }    

    public void setEntradaDevolucionDao(EntradaDevolucionDao entradaDevolucionDao) {
        this.entradaDevolucionDao = entradaDevolucionDao;
    }

    public void setSalidaDevolucionController(SalidaDevolucionController salidaDevolucionController) {
        this.salidaDevolucionController = salidaDevolucionController;
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

    public void setProductoController(ProductoController productoController) {
        this.productoController = productoController;
    }
    
}
