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
import technology.tikal.ventas.controller.producto.ProductoController;
import technology.tikal.ventas.dao.producto.ProductoDao;
import technology.tikal.ventas.model.producto.Producto;
import technology.tikal.ventas.model.producto.ofy.AbstractProductoOfy;
import technology.tikal.ventas.model.producto.ofy.CatalogoOfy;
import technology.tikal.ventas.model.producto.ofy.ProductoOfy;

/**
 * 
 * @author Nekorp
 *
 */
public class ProductoControllerImp implements ProductoController {

    private ProductoDao dao;
    private CatalogoController catalogoController;
    
    @Override
    public Producto crear(Long idCatalogo, Producto request) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        if (request.getId() != null) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"NoValidRequest.ProductoControllerImp.crear"}, 
                new String[]{request.getId() + ""}, 
                "El producto no debe tener id en la creacion"));
        }
        ProductoOfy nuevo = ProductoFactory.create(catalogo, request);
        nuevo.update(request);
        dao.guardar(catalogo, nuevo);
        return nuevo;
    }

    @Override
    public void actualizar(Long idCatalogo, Long productoId, Producto request) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        AbstractProductoOfy original = dao.consultar(catalogo, productoId);
        original.update(request);
        dao.guardar(catalogo, original);
        
    }

    @Override
    public Producto[] consultar(Long idCatalogo, PaginationDataLong pagination) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        List<AbstractProductoOfy> consulta = dao.consultarTodos(catalogo, null, pagination);
        Producto[] response = consulta.toArray(new Producto[consulta.size()]);
        return response;
    }

    @Override
    public Producto cargar(Long idCatalogo, Long productoId) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        return dao.consultar(catalogo, productoId);
    }
    
    @Override
    public void borrar(Long idCatalogo, Long productoId) {
        CatalogoOfy catalogo = (CatalogoOfy) catalogoController.cargar(idCatalogo);
        AbstractProductoOfy producto = dao.consultar(catalogo, productoId);
        dao.borrar(catalogo, producto);
    }

    public void setDao(ProductoDao dao) {
        this.dao = dao;
    }

    public void setCatalogoController(CatalogoController catalogoController) {
        this.catalogoController = catalogoController;
    }
    
}
