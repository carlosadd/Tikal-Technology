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
package technology.tikal.gae.http.cache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Nekorp
 *
 */
public interface HttpCacheQueryService {

    public boolean validateEtag(String uri, String etag);
    /**
     * calcula el etag para el request y lo coloca en el response
     * @param request
     * @param response
     */
    public void setEtag(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * Invalida todas los etag asociados a una uri. usualmente esto sucede despues de un update o delete.
     * @param uri
     */
    public void updateUriEtagEntry(String uri);
    
    public void updateAllUriEtagEntry();
}
