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
package technology.tikal.customers.cache.http;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import technology.tikal.customers.model.ClienteMxOfy;
import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.CustomerOfyFactory;
import technology.tikal.gae.http.cache.HttpCacheQueryService;

/**
 * Controla el cache por resource.
 * @author Nekorp
 *
 */
public class CustomerCacheController extends HandlerInterceptorAdapter  {

    //private static final Log LOGGER = LogFactory.getLog(CustomerCacheController.class);
    
    private HttpCacheQueryService httpCacheQueryService;
    private String cacheControl;
    private String resourceUri;
    private Map<Long, UpdatePair<CustomerOfy>> originalData;
    
    public CustomerCacheController() {
        resourceUri = "/api/customer";
        originalData = new HashMap<>();
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean next = true;
        if (isUpdateRelated(request, response)) {
            //CustomerCacheController.LOGGER.debug("se detecta un POST de interes");
            originalData.put(Thread.currentThread().getId(), null);
        }
        if (isQueryRelated(request, response)) {
            //CustomerCacheController.LOGGER.debug("se detecta GET de interes:" + request.getRequestURI());
            String requestETag = request.getHeader("If-None-Match");
            if (httpCacheQueryService.validateEtag(request.getRequestURI(), requestETag)) {
                //CustomerCacheController.LOGGER.debug("se pide al cliente que use el cache");
                response.setStatus(304);
                next = false;
            } else {
                //CustomerCacheController.LOGGER.debug("se realiza la consulta");
                response.setHeader("Cache-Control", cacheControl);
                httpCacheQueryService.setEtag(request, response);
            }
        }
        return next;
    }
    
    public boolean isQueryRelated(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equals("GET") && StringUtils.equals(resourceUri, request.getRequestURI())) {
            return true;
        }
        return false;
    }
    
    public boolean isUpdateRelated(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equals("POST") || request.getMethod().equals("DELETE")) {
            if (StringUtils.equals(resourceUri, StringUtils.substringBeforeLast(request.getRequestURI(), "/"))) {
                return true;
            }
        }
        return false;
    }
    
    public void manageCreate() {
        //CustomerCacheController.LOGGER.debug("se detecto la creacion de un item");
        httpCacheQueryService.updateUriEtagEntry(resourceUri);
    }
    
    public void manageUpdate() {
        long id = Thread.currentThread().getId();
        if (this.originalData.containsKey(id)) {
            //CustomerCacheController.LOGGER.debug("buscando algun cambio de interes");
            UpdatePair<CustomerOfy> pair = this.originalData.get(id);
            boolean update = false;
            if (!pair.getOriginal().getName().equals(pair.getUpdated().getName())) {
                //CustomerCacheController.LOGGER.debug("se identifico cambio de nombre");
                update = true;
            }
            if(pair.getOriginal() instanceof ClienteMxOfy && pair.getUpdated() instanceof ClienteMxOfy) {
                ClienteMxOfy original = (ClienteMxOfy) pair.getOriginal();
                ClienteMxOfy updated = (ClienteMxOfy) pair.getUpdated();
                if (!StringUtils.equals(original.getRfc(), updated.getRfc())) {
                    //CustomerCacheController.LOGGER.debug("se identifico cambio de rfc");
                    update = true;
                }
            }
            
            if (pair.getOriginal().isActive() != pair.getUpdated().isActive()) {
                //CustomerCacheController.LOGGER.debug("se identifico cambio de status");
                update = true;
            }
            if (update) {
                httpCacheQueryService.updateUriEtagEntry(resourceUri);
                //CustomerCacheController.LOGGER.debug("actualizando el etag");
            } else {
                //CustomerCacheController.LOGGER.debug("no se cambio nada de interes");
            }
            this.originalData.remove(id);
        }
    }
    
    public void listenToBusqueda(Object retVal) {
        long id = Thread.currentThread().getId();
        if (this.originalData.containsKey(id)) {
            if (retVal instanceof CustomerOfy) {
                CustomerOfy objA = (CustomerOfy) retVal;
                CustomerOfy objB = CustomerOfyFactory.buildInternal(objA);
                objB.update(objA);
                objB.setActive(objA.isActive());
                this.originalData.put(id, new UpdatePair<CustomerOfy>(objB, objA));
                //CustomerCacheController.LOGGER.debug("se clona el valor consultado y actualizado");
            }
        }
    }    

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public void setHttpCacheQueryService(HttpCacheQueryService httpCacheQueryService) {
        this.httpCacheQueryService = httpCacheQueryService;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
