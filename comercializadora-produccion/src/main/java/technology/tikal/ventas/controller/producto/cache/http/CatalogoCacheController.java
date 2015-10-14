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
import technology.tikal.ventas.model.producto.ofy.CatalogoOfy;

/**
 * 
 * @author Nekorp
 *
 */
public class CatalogoCacheController extends AbstractCacheController<CatalogoOfy> {

    @Override
    public boolean haveChanges(UpdatePair<CatalogoOfy> pair) {
        if (!StringUtils.equals(pair.getOriginal().getNombre(), pair.getUpdated().getNombre())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof CatalogoOfy;
    }

    @Override
    public UpdatePair<CatalogoOfy> cloneObject(Object retVal) {
        CatalogoOfy objA = (CatalogoOfy) retVal;
        CatalogoOfy objB = new CatalogoOfy();
        objB.update(objA);
        return new UpdatePair<CatalogoOfy>(objB, objA);
    }

}
