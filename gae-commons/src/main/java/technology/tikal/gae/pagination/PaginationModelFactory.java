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
package technology.tikal.gae.pagination;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UriUtils;
import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.gae.pagination.model.PaginationDataDual;

/**
 * @author Nekorp
 */
public class PaginationModelFactory {

    public static <T> Page<T> getPage() {
        return new Page<T>();
    }
    
    public static <T> Page<T> getPage(T items, String tipoItems, String baseUrl, FiltroBusqueda filtro, PaginationData<?> pagination) {
        Page<T> page =  new Page<T>();
        page.setType(tipoItems);
        if (pagination instanceof PaginationDataDual) {
            PaginationDataDual<?,?> paginationDual;
            paginationDual = (PaginationDataDual<?,?>) pagination;
            page.setLinkCurrentPage(armaUrl(baseUrl, filtro, pagination.getSinceId(), paginationDual.getSince(), pagination.getMaxResults()));
            if (pagination.hasNextId()) {
                page.setLinkNextPage(armaUrl(baseUrl, filtro, pagination.getNextId(), paginationDual.getNext(), pagination.getMaxResults()));
            }
        } else {
            page.setLinkCurrentPage(armaUrl(baseUrl, filtro, pagination.getSinceId(), null, pagination.getMaxResults()));
            if (pagination.hasNextId()) {
                page.setLinkNextPage(armaUrl(baseUrl, filtro, pagination.getNextId(), null, pagination.getMaxResults()));
            }
        }
        page.setItems(items);
        return page;
    }
    
    private static String armaUrl(String baseUrl, final FiltroBusqueda filtro, final Object sinceId, final Object since, final int maxResults) {
        String r = baseUrl;
        if (filtro != null) {
            PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(filtro);
            for(PropertyDescriptor x: descriptors){
                if (x.getName().compareTo("class") != 0) {
                    Object obj;
                    try {
                        obj = PropertyUtils.getProperty(filtro, x.getName());
                    } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException  e ) {
                        throw new IllegalArgumentException("no se puede construir la pagina con el filtro proporcionado");
                    }
                    if (obj != null) {
                        r = addUrlParameter(r, x.getName(), obj.toString());
                    }
                }
            }
        }
        if (sinceId != null) {
            r = addUrlParameter(r, "sinceId", sinceId.toString());
        }
        if (since != null) {
            r = addUrlParameter(r, "since", since.toString());
        }
        if (maxResults > 0) {
            r = addUrlParameter(r,"maxResults", maxResults + "");
        }
        return r;
    }
    
    private static String addUrlParameter(final String url, final String name, final String value) {
        String response = url;
        if (!StringUtils.isEmpty(value)) {
            if (!StringUtils.contains(response, '?')) {
                response = response + "?";
            } else {
                response = response + "&";
            }
            try {
                response = response + name + "=" + UriUtils.encodeQueryParam(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }
}
