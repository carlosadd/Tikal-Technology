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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import technology.tikal.gae.http.cache.HttpCacheQueryService;
import technology.tikal.gae.http.cache.UpdatePair;

/**
 * Clase base para los controles del cache
 * @author Nekorp
 *
 */
public abstract class AbstractCacheController<T> extends HandlerInterceptorAdapter  {

    private static final Log LOGGER = LogFactory.getLog(AbstractCacheController.class);
    
    private HttpCacheQueryService httpCacheQueryService;
    private String cacheControl;
    private String resourceUri;
    private List<String> updateUriPattern;
    private Map<Long, UpdatePair<T>> originalData;
    
    public AbstractCacheController() {
        originalData = new HashMap<>();
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean next = true;
        if (request.getMethod().equals("POST") || request.getMethod().equals("DELETE")) {
            startUpdate(request, response);
        }
        if (request.getMethod().equals("GET") && isQueryRelated(request, response)) {
            String requestETag = request.getHeader("If-None-Match");
            if (httpCacheQueryService.validateEtag(request.getRequestURI(), requestETag)) {
                if (AbstractCacheController.LOGGER.isInfoEnabled()) {
                    AbstractCacheController.LOGGER.info("se pide al cliente que use el cache para el recurso:" + resourceUri);
                }
                response.setStatus(304);
                next = false;
            } else {
                response.setHeader("Cache-Control", cacheControl);
                httpCacheQueryService.setEtag(request, response);
            }
        }
        return next;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long id = Thread.currentThread().getId();
        this.originalData.remove(id);
    }

    public boolean isQueryRelated(HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.equals(resourceUri, request.getRequestURI())) {
            return true;
        }
        return false;
    }
    
    public void startUpdate(HttpServletRequest request, HttpServletResponse response) {
        String requestUri = request.getRequestURI();
        for (String x: this.updateUriPattern) {
            if (requestUri.matches(x)) {
                originalData.put(Thread.currentThread().getId(), null);
                return;
            }
        }
    }
    
    public void manageCreate() {
        if (AbstractCacheController.LOGGER.isInfoEnabled()) {
            AbstractCacheController.LOGGER.info("actualizando el etag:" + resourceUri);
        }
        httpCacheQueryService.updateUriEtagEntry(resourceUri);
    }
    
    public void manageUpdate() {
        long id = Thread.currentThread().getId();
        if (this.originalData.containsKey(id)) {
            try {
                UpdatePair<T> pair = this.originalData.get(id);
                if (haveChanges(pair)) {
                    if (AbstractCacheController.LOGGER.isInfoEnabled()) {
                        AbstractCacheController.LOGGER.info("actualizando el etag:" + resourceUri);
                    }
                    httpCacheQueryService.updateUriEtagEntry(resourceUri);
                }
            } catch (IllegalArgumentException e) {
                AbstractCacheController.LOGGER.warn("cambio el id de la entidad:" + e.getMessage());
            }
        }
    }
    
    /**
     * Busca si hay cambios en el objeto que indiquen que se tiene que refrescar el etag
     * @param pair el par del objeto original y el actualizado
     * @return true si hay cambios
     */
    public abstract boolean haveChanges(UpdatePair<T> pair);
    
    protected boolean safeEqualsLong(Long a, Long b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
    
    protected boolean safeEqualsDates(Date a, Date b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
    
    public void listenToBusqueda(Object retVal) {
        long id = Thread.currentThread().getId();
        if (this.originalData.containsKey(id) && this.originalData.get(id) == null) {
            if (isTypeOf(retVal)) {
                this.originalData.put(id, cloneObject(retVal));
            }
        }
    }
    
    /**
     * Indica si el objeto es del tipo donde se buscan cambios
     * @param retVal
     * @return
     */
    public abstract boolean isTypeOf(Object retVal);
    
    /**
     * crea una copia del objeto para guardarlo como el original
     * @param retVal el objeto antes de ser actualizado
     * @return el par de objetos original (el clonado) y el que se va a actualizar (retVal)
     */
    public abstract UpdatePair<T> cloneObject(Object retVal);

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public void setHttpCacheQueryService(HttpCacheQueryService httpCacheQueryService) {
        this.httpCacheQueryService = httpCacheQueryService;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public void setUpdateUriPattern(List<String> updateUriPattern) {
        this.updateUriPattern = updateUriPattern;
    }
}
