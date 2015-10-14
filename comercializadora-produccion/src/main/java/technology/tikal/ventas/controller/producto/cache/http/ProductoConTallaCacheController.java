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
package technology.tikal.ventas.controller.producto.cache.http;

import org.apache.commons.lang.StringUtils;

import technology.tikal.gae.http.cache.AbstractCacheController;
import technology.tikal.gae.http.cache.UpdatePair;
import technology.tikal.ventas.controller.producto.imp.ProductoFactory;
import technology.tikal.ventas.model.producto.ofy.intermediario.ProductoConTalla;

/**
 * 
 * @author Nekorp
 *
 */
public class ProductoConTallaCacheController extends AbstractCacheController<ProductoConTalla> {

    @Override
    public boolean haveChanges(UpdatePair<ProductoConTalla> pair) {
        if (!StringUtils.equals(pair.getOriginal().getTalla(), pair.getUpdated().getTalla())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof ProductoConTalla;
    }

    @Override
    public UpdatePair<ProductoConTalla> cloneObject(Object retVal) {
        ProductoConTalla objA = (ProductoConTalla) retVal;
        ProductoConTalla objB = ProductoFactory.clone(objA);
        return new UpdatePair<ProductoConTalla>(objB, objA);
    }

}
