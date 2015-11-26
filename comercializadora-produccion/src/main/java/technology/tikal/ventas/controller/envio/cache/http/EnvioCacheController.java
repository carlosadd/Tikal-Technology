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
package technology.tikal.ventas.controller.envio.cache.http;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import technology.tikal.gae.http.cache.AbstractCacheController;
import technology.tikal.gae.http.cache.UpdatePair;
import technology.tikal.ventas.controller.envio.imp.EnvioFactory;
import technology.tikal.ventas.model.envio.ofy.EnvioOfy;

/**
 * 
 * @author Nekorp
 *
 */
public class EnvioCacheController extends AbstractCacheController<EnvioOfy> {

    @Override
    public boolean haveChanges(UpdatePair<EnvioOfy> pair) {
        if (compareDates(pair.getOriginal().getFechaEntrega(), pair.getUpdated().getFechaEntrega())) {
            return true;
        }
        if (compareDates(pair.getOriginal().getFechaSalida(), pair.getUpdated().getFechaSalida())) {
            return true;
        }
        if (pair.getOriginal().getIdTransportista().compareTo(pair.getUpdated().getIdTransportista()) != 0) {
            return true;
        }
        if (StringUtils.equals(pair.getOriginal().getName(), pair.getOriginal().getName())) {
            return true;
        }
        if (StringUtils.equals(pair.getOriginal().getNota(), pair.getOriginal().getNota())) {
            return true;
        }
        if (StringUtils.equals(pair.getOriginal().getStatus(), pair.getOriginal().getStatus())) {
            return true;
        }
        return false;
    }
    
    private boolean compareDates(Date a, Date b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.compareTo(b) != 0;
    }

    @Override
    public boolean isTypeOf(Object retVal) {
        return retVal instanceof EnvioOfy;
    }

    @Override
    public UpdatePair<EnvioOfy> cloneObject(Object retVal) {
        EnvioOfy objA = (EnvioOfy) retVal;
        EnvioOfy objB = EnvioFactory.clone(objA);
        return new UpdatePair<EnvioOfy>(objB, objA);
    }

}
