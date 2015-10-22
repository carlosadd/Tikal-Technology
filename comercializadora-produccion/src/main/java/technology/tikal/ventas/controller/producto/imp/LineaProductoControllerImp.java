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
import technology.tikal.ventas.controller.producto.ProductoDeLineaController;
import technology.tikal.ventas.dao.producto.LineaDeProductoDao;
import technology.tikal.ventas.model.producto.LineaDeProductos;
import technology.tikal.ventas.model.producto.ProductoDeLinea;
import technology.tikal.ventas.model.producto.ofy.CatalogoOfy;
import technology.tikal.ventas.model.producto.ofy.LineaDeProductosOfy;

/**
 * 
 * @author Nekorp
 *
 */
public class LineaProductoControllerImp implements LineaProductoController {

    private LineaDeProductoDao dao;
    private CatalogoController catalogoController;
    private ProductoDeLineaController productoDeLineaController;
    
    @Override
    public LineaDeProductos crear(Long idCatalogo, LineaDeProductos request) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        if (request.getId() != null) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"NoValidRequest.LineaProductoControllerImp.crearPedido"}, 
                new String[]{request.getId() + ""}, 
                "El pedido no debe tener id en la creacion"));
        }
        LineaDeProductosOfy nuevo = LineaProductoFactory.create(catalogo, request);
        nuevo.update(request);
        dao.guardar(catalogo, nuevo);
        return nuevo;
    }

    @Override
    public void actualizar(Long idCatalogo, Long lineaProductoId, LineaDeProductos request) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        LineaDeProductosOfy original = dao.consultar(catalogo, lineaProductoId);
        original.update(request);
        dao.guardar(catalogo, original);
    }

    @Override
    public LineaDeProductos[] consultar(Long idCatalogo, PaginationDataLong pagination) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        List<LineaDeProductosOfy> consulta = dao.consultarTodos(catalogo, null, pagination);
        LineaDeProductos[] response = new LineaDeProductos[consulta.size()];
        consulta.toArray(response);
        return response;
    }

    @Override
    public LineaDeProductos cargar(Long idCatalogo, Long lineaProductoId) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        return dao.consultar(catalogo, lineaProductoId);
    }
    
    @Override
    public void borrar(Long idCatalogo, Long lineaProductoId) {
        PaginationDataLong pagination = new PaginationDataLong();
        pagination.setMaxResults(1);
        ProductoDeLinea[] tallas = productoDeLineaController.consultar(idCatalogo, lineaProductoId, pagination);
        if (tallas.length > 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotEmpty.LineaProductoControllerImp.borrar"}, 
                    new String[]{idCatalogo + ""}, 
                    "La linea de productos no esta vacia"));
        }
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        LineaDeProductosOfy lineaDeProductos = dao.consultar(catalogo, lineaProductoId);
        dao.borrar(catalogo, lineaDeProductos);
    }
    
    public void setDao(LineaDeProductoDao dao) {
        this.dao = dao;
    }

    public void setCatalogoController(CatalogoController catalogoController) {
        this.catalogoController = catalogoController;
    }

    public void setProductoDeLineaController(ProductoDeLineaController productoDeLineaController) {
        this.productoDeLineaController = productoDeLineaController;
    }
    
}
