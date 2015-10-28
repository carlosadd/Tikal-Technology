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
import technology.tikal.ventas.controller.almacen.EntradaController;
import technology.tikal.ventas.controller.almacen.EntradaDevolucionController;
import technology.tikal.ventas.controller.almacen.SalidaDevolucionController;
import technology.tikal.ventas.controller.pedido.PedidoController;
import technology.tikal.ventas.controller.producto.ProductoController;
import technology.tikal.ventas.dao.almacen.RegistroAlmacenFilter;
import technology.tikal.ventas.dao.almacen.SalidaDevolucionDao;
import technology.tikal.ventas.model.almacen.EntradaDevolucion;
import technology.tikal.ventas.model.almacen.RegistroAlmacen;
import technology.tikal.ventas.model.almacen.SalidaDevolucion;
import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenTransient;
import technology.tikal.ventas.model.almacen.ofy.SalidaDevolucionOfy;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
public class SalidaDevolucionControllerImp implements SalidaDevolucionController {

    private SalidaDevolucionDao salidaDevolucionDao;
    private PedidoController pedidoController;
    private ProductoController productoController;
    private EntradaController entradaController;
    private EntradaDevolucionController entradaDevolucionController;
    
    @Override
    public SalidaDevolucion crear(Long pedidoId, RegistroAlmacenTransient request) {
        Pedido pedido = pedidoController.get(pedidoId);
        Producto producto = productoController.cargar(request.getProducto().getCatalogoId(), request.getProducto().getId());
        RegistroAlmacen referencia = null;
        if (request.getReferenciaRegistro() != null) {
            try {
                referencia = entradaController.get(pedidoId, request.getReferenciaRegistro());
            } catch (NotFoundException e) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                        new String[]{"RefNotFound.SalidaDevolucionControllerImp.crear"}, 
                        new String[]{pedidoId + ""}, 
                        "No se encontro la referencia"));
            }
        } else {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"RefRequired.SalidaDevolucionControllerImp.crear"}, 
                new String[]{pedidoId + ""}, 
                "No se encontro la referencia"));
        }
        SalidaDevolucionOfy nuevo = RegistroAlmacenFactory.buildDevolucion(pedido, request, producto, SalidaDevolucionOfy.class, referencia);
        salidaDevolucionDao.guardar(pedido, nuevo);
        return nuevo;
    }

    @Override
    public void actualizar(Long pedidoId, Long entradaId, RegistroAlmacenTransient request) {
        Pedido pedido = pedidoController.get(pedidoId);
        SalidaDevolucionOfy original = salidaDevolucionDao.consultar(pedido, entradaId);
        original.setCantidad(request.getCantidad());
        original.setFechaRegistro(request.getFechaRegistro());
        original.setDescripcion(request.getDescripcion());
        //de momento no se actualiza la referencia
        /*
        RegistroAlmacen referencia = null;
        if (request.getReferenciaRegistro() != null) {
            try {
                referencia = entradaController.get(pedidoId, request.getReferenciaRegistro());
            } catch (NotFoundException e) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                        new String[]{"RefNotFound.SalidaDevolucionControllerImp.actualizar"}, 
                        new String[]{pedidoId + ""}, 
                        "No se encontro la referencia"));
            }
        } else {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"RefRequired.SalidaDevolucionControllerImp.actualizar"}, 
                new String[]{pedidoId + ""}, 
                "No se encontro la referencia"));
        }
        original.setOrigen(referencia);*/
        salidaDevolucionDao.guardar(pedido, original);
    }

    @Override
    public SalidaDevolucion[] consultar(Long pedidoId, RegistroAlmacenFilter filter, PaginationDataLong pagination) {
        Pedido pedido = pedidoController.get(pedidoId);
        List<SalidaDevolucionOfy> consulta = salidaDevolucionDao.consultarTodos(pedido, filter, pagination);
        SalidaDevolucion[] response = new SalidaDevolucion[consulta.size()];
        consulta.toArray(response);
        return response;
    }

    @Override
    public SalidaDevolucion get(Long pedidoId, Long entradaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        return salidaDevolucionDao.consultar(pedido, entradaId);
    }

    @Override
    public void borrar(Long pedidoId, Long entradaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        SalidaDevolucionOfy target = salidaDevolucionDao.consultar(pedido, entradaId);
        RegistroAlmacenFilter filter = new RegistroAlmacenFilter();
        filter.setOrigen(target);
        PaginationDataLong pagination = new PaginationDataLong();
        pagination.setMaxResults(1);
        EntradaDevolucion[] referencias = entradaDevolucionController.consultar(pedidoId, filter, pagination);
        if (referencias.length > 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"Referenced.SalidaDevolucionControllerImp.borrar"}, 
                new String[]{pedidoId + ""}, 
                "No se debe referenciar este registro"));
        }
        salidaDevolucionDao.borrar(pedido, target);
    }    

    public void setSalidaDevolucionDao(SalidaDevolucionDao salidaDevolucionDao) {
        this.salidaDevolucionDao = salidaDevolucionDao;
    }
    
    public void setEntradaDevolucionController(
            EntradaDevolucionController entradaDevolucionController) {
        this.entradaDevolucionController = entradaDevolucionController;
    }

    public void setEntradaController(EntradaController entradaController) {
        this.entradaController = entradaController;
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

    public void setProductoController(ProductoController productoController) {
        this.productoController = productoController;
    }
    
}
