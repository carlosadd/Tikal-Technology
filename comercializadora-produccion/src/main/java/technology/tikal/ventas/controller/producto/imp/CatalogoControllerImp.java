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
package technology.tikal.ventas.controller.producto.imp;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.controller.producto.CatalogoController;
import technology.tikal.ventas.controller.producto.LineaProductoController;
import technology.tikal.ventas.controller.producto.ProductoController;
import technology.tikal.ventas.dao.producto.CatalogoProductoDao;
import technology.tikal.ventas.model.producto.Catalogo;
import technology.tikal.ventas.model.producto.LineaDeProductos;
import technology.tikal.ventas.model.producto.Producto;
import technology.tikal.ventas.model.producto.ofy.CatalogoOfy;

/**
 * 
 * @author Nekorp
 *
 */
public class CatalogoControllerImp implements CatalogoController {

    private CatalogoProductoDao dao;
    private LineaProductoController lineaProductoController;
    private ProductoController productoController;
    
    @Override
    public Catalogo crear(Catalogo request) {
        if (request.getId() != null) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"NoValidRequest.CatalogoControllerImp.crearPedido"}, 
                new String[]{request.getId() + ""}, 
                "El pedido no debe tener id en la creacion"));
        }
        if (request instanceof CatalogoOfy) {
            dao.guardar((CatalogoOfy) request);
            return request;
        } else {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NoValidRequest.CatalogoControllerImp.crearPedido"}, 
                    new String[]{request.getId() + ""}, 
                    "El catalogo no es de un tipo valido"));
        }
    }

    @Override
    public void actualizar(Long idCatalogo, Catalogo request) {
        CatalogoOfy original = dao.consultar(idCatalogo);
        original.update(request);
        dao.guardar(original);
    }

    @Override
    public Catalogo[] consultar(PaginationDataLong pagination) {
        List<CatalogoOfy> consulta = dao.consultarTodos(null, pagination);
        Catalogo[] response = new Catalogo[consulta.size()];
        consulta.toArray(response);
        return response;
    }
    
    @Override
    public Catalogo cargar(Long idCatalogo) {
        return dao.consultar(idCatalogo);
    }
    
    @Override
    public void borrar(Long idCatalogo) {
        PaginationDataLong lineaPagination = new PaginationDataLong();
        lineaPagination.setMaxResults(1);
        LineaDeProductos[] lineas = lineaProductoController.consultar(idCatalogo, lineaPagination);
        if (lineas.length > 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotEmpty.CatalogoControllerImp.borrar"}, 
                    new String[]{idCatalogo + ""}, 
                    "El catalogo no esta vacio"));
        }
        PaginationDataLong productoPagination = new PaginationDataLong();
        productoPagination.setMaxResults(1);
        Producto[] productos = productoController.consultar(idCatalogo, productoPagination);
        if (productos.length > 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotEmpty.CatalogoControllerImp.borrar"}, 
                    new String[]{idCatalogo + ""}, 
                    "El catalogo no esta vacio"));
        }
        CatalogoOfy catalogo = dao.consultar(idCatalogo);
        dao.borrar(catalogo);
    }

    public void setDao(CatalogoProductoDao dao) {
        this.dao = dao;
    }

    public void setLineaProductoController(
            LineaProductoController lineaProductoController) {
        this.lineaProductoController = lineaProductoController;
    }

    public void setProductoController(ProductoController productoController) {
        this.productoController = productoController;
    }
}
