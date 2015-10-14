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
import technology.tikal.ventas.dao.producto.ProductoDeLineaDao;
import technology.tikal.ventas.dao.producto.ProductoDeLineaFilter;
import technology.tikal.ventas.model.producto.ProductoDeLinea;
import technology.tikal.ventas.model.producto.ofy.CatalogoOfy;
import technology.tikal.ventas.model.producto.ofy.LineaDeProductosOfy;
import technology.tikal.ventas.model.producto.ofy.ProductoDeLineaOfy;

/**
 * 
 * @author Nekorp
 *
 */
public class ProductoDeLineaControllerImp implements ProductoDeLineaController {

    private ProductoDeLineaDao dao;
    private CatalogoController catalogoController;
    private LineaProductoController lineaProductoController;
    @Override
    public ProductoDeLinea crear(Long idCatalogo, Long lineaProductoId, ProductoDeLinea request) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        LineaDeProductosOfy lineaProducto = (LineaDeProductosOfy) lineaProductoController.cargar(idCatalogo, lineaProductoId);
        if (request.getId() != null) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"NoValidRequest.ProductoDeLineaControllerImp.crear"}, 
                new String[]{request.getId() + ""}, 
                "El producto no debe tener id en la creacion"));
        }
        ProductoDeLineaOfy nuevo = ProductoFactory.create(catalogo, lineaProducto, request);
        nuevo.update(request);
        dao.guardar(catalogo, nuevo);
        return nuevo;
    }

    @Override
    public void actualizar(Long idCatalogo, Long lineaProductoId, Long productoId, ProductoDeLinea request) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        ProductoDeLineaOfy original = dao.consultar(catalogo, productoId);
        original.update(request);
        dao.guardar(catalogo, original);
        
    }

    @Override
    public ProductoDeLinea[] consultar(Long idCatalogo, Long lineaProductoId, PaginationDataLong pagination) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        LineaDeProductosOfy lineaProducto = (LineaDeProductosOfy) lineaProductoController.cargar(idCatalogo, lineaProductoId);
        ProductoDeLineaFilter filter = new ProductoDeLineaFilter();
        filter.setLineaDeProducto(lineaProducto);
        List<ProductoDeLineaOfy> consulta = dao.consultarTodos(catalogo, filter, pagination);
        ProductoDeLinea[] response = consulta.toArray(new ProductoDeLinea[consulta.size()]);
        return response;
    }

    @Override
    public ProductoDeLinea cargar(Long idCatalogo, Long lineaProductoId, Long productoId) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        return dao.consultar(catalogo, productoId);
    }
    
    @Override
    public void borrar(Long idCatalogo, Long lineaProductoId, Long productoId) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        ProductoDeLineaOfy producto = dao.consultar(catalogo, productoId);
        dao.borrar(catalogo, producto);
    }

    public void setDao(ProductoDeLineaDao dao) {
        this.dao = dao;
    }

    public void setCatalogoController(CatalogoController catalogoController) {
        this.catalogoController = catalogoController;
    }

    public void setLineaProductoController(LineaProductoController lineaProductoController) {
        this.lineaProductoController = lineaProductoController;
    }
}
