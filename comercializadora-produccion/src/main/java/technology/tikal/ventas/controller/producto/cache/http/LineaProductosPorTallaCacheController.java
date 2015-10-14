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
import technology.tikal.ventas.controller.producto.imp.LineaProductoFactory;
import technology.tikal.ventas.model.producto.ofy.intermediario.LineaProductosPorTalla;

/**
 * 
 * @author Nekorp
 *
 */
public class LineaProductosPorTallaCacheController extends AbstractCacheController<LineaProductosPorTalla> {

    @Override
    public boolean haveChanges(UpdatePair<LineaProductosPorTalla> pair) {
        if (!StringUtils.equals(pair.getOriginal().getDatosGenerales().getNombre(), pair.getUpdated().getDatosGenerales().getNombre())) {
            return true;
        }
        if (!StringUtils.equals(pair.getOriginal().getDatosGenerales().getDescripcion(), pair.getUpdated().getDatosGenerales().getDescripcion())) {
            return true;
        }
        if (!StringUtils.equals(pair.getOriginal().getDatosGenerales().getUnidadMedida(), pair.getUpdated().getDatosGenerales().getUnidadMedida())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof LineaProductosPorTalla;
    }

    @Override
    public UpdatePair<LineaProductosPorTalla> cloneObject(Object retVal) {
        LineaProductosPorTalla objA = (LineaProductosPorTalla) retVal;
        LineaProductosPorTalla objB = LineaProductoFactory.clone(objA);
        return new UpdatePair<LineaProductosPorTalla>(objB, objA);
    }

}
