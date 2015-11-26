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

import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.controller.almacen.SalidaController;
import technology.tikal.ventas.controller.pedido.PedidoController;
import technology.tikal.ventas.controller.producto.ProductoController;
import technology.tikal.ventas.dao.almacen.RegistroAlmacenFilter;
import technology.tikal.ventas.dao.almacen.SalidaDao;
import technology.tikal.ventas.model.almacen.GrupoRegistroAlmacen;
import technology.tikal.ventas.model.almacen.Salida;
import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenTransient;
import technology.tikal.ventas.model.almacen.ofy.SalidaOfy;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;
import technology.tikal.ventas.model.producto.ProductoDeLinea;

/**
 * 
 * @author Nekorp
 *
 */
public class SalidaControllerImp implements SalidaController {

    private SalidaDao salidaDao;
    private PedidoController pedidoController;
    private ProductoController productoController;
    
    @Override
    public Salida crear(Long pedidoId, RegistroAlmacenTransient request) {
        Pedido pedido = pedidoController.get(pedidoId);
        Producto producto = productoController.cargar(request.getProducto().getCatalogoId(), request.getProducto().getId());
        SalidaOfy nuevo = RegistroAlmacenFactory.build(pedido, request, producto, SalidaOfy.class);
        salidaDao.guardar(pedido, nuevo);
        return nuevo;
    }

    @Override
    public void actualizar(Long pedidoId, Long entradaId, RegistroAlmacenTransient request) {
        Pedido pedido = pedidoController.get(pedidoId);
        SalidaOfy original = salidaDao.consultar(pedido, entradaId);
        original.setCantidad(request.getCantidad());
        original.setFechaRegistro(request.getFechaRegistro());
        original.setDescripcion(request.getDescripcion());
        original.setTag(request.getTag());
        original.setReferenciaEnvio(request.getReferenciaEnvio());
        salidaDao.guardar(pedido, original);
    }

    @Override
    public Salida[] consultar(Long pedidoId, RegistroAlmacenFilter filter, PaginationDataLong pagination) {
        Pedido pedido = pedidoController.get(pedidoId);
        List<SalidaOfy> consulta = salidaDao.consultarTodos(pedido, filter, pagination);
        return sortByGroup(consulta);
    }
    
    private Salida[] sortByGroup(List<SalidaOfy> source) {
        List<Salida> result = new LinkedList<>();
        List<GrupoRegistroAlmacen> grupos = new ArrayList<>(); 
        for (SalidaOfy x: source) {
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
        Salida[] response = new Salida[result.size()];
        result.toArray(response);
        return response;
    }

    @Override
    public Salida get(Long pedidoId, Long entradaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        return salidaDao.consultar(pedido, entradaId);
    }

    @Override
    public void borrar(Long pedidoId, Long entradaId) {
        Pedido pedido = pedidoController.get(pedidoId);
        SalidaOfy target = salidaDao.consultar(pedido, entradaId);
        salidaDao.borrar(pedido, target);
    }

    public void setSalidaDao(SalidaDao salidaDao) {
        this.salidaDao = salidaDao;
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

    public void setProductoController(ProductoController productoController) {
        this.productoController = productoController;
    }
    
}
