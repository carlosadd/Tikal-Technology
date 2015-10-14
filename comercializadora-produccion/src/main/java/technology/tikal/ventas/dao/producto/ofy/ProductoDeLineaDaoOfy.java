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
package technology.tikal.ventas.dao.producto.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.ventas.dao.producto.ProductoDeLineaDao;
import technology.tikal.ventas.dao.producto.ProductoDeLineaFilter;
import technology.tikal.ventas.model.producto.ofy.CatalogoOfy;
import technology.tikal.ventas.model.producto.ofy.ProductoDeLineaOfy;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * 
 * @author Nekorp
 *
 */
public class ProductoDeLineaDaoOfy implements ProductoDeLineaDao {

    @Override
    public List<ProductoDeLineaOfy> consultarTodos(CatalogoOfy parent, ProductoDeLineaFilter filtro, PaginationData<Long> pagination) {
        List<ProductoDeLineaOfy> result;
        Query<ProductoDeLineaOfy> query = ofy().load().type(ProductoDeLineaOfy.class);
        query = query.ancestor(Key.create(parent));
        //query = query.order("-fechaDeCreacion");
        query = query.filter("refLinea", Ref.create(filtro.getLineaDeProducto()));
        if (pagination.hasSinceId()) {
            query = query.filterKey(">=", Key.create(ProductoDeLineaOfy.class, pagination.getSinceId()));
        }
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            ProductoDeLineaOfy ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getId());
            result.remove(pagination.getMaxResults());
        }
        return result;
    }

    @Override
    public void guardar(CatalogoOfy parent, ProductoDeLineaOfy objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public ProductoDeLineaOfy consultar(CatalogoOfy parent, Long id, Class<?>... group) {
        return ofy().load().group(group).key(Key.create(Key.create(parent), ProductoDeLineaOfy.class, id)).safe();
    }

    @Override
    public void borrar(CatalogoOfy parent, ProductoDeLineaOfy objeto) {
        ofy().delete().entities(objeto).now();
    }

}
