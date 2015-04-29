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
package technology.tikal.gae.http.cache.imp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.googlecode.objectify.NotFoundException;

import technology.tikal.gae.http.cache.HttpCacheQueryService;
import technology.tikal.gae.http.cache.dao.UriEtagEntryDao;
import technology.tikal.gae.http.cache.model.UriEtagEntry;
import technology.tikal.gae.http.cache.model.UriEtagEntryFactory;
import technology.tikal.gae.pagination.model.PaginationDataString;
/**
 * 
 * @author Nekorp
 *
 */
public class PreconditionalQueryService implements HttpCacheQueryService {

    private static final int DEFAULT_FLUSH_CACHE_SIZE = 50;
    private UriEtagEntryFactory uriEtagEntryFactory;
    private UriEtagEntryDao uriEtagEntryDao;
    private int flushCacheSize = PreconditionalQueryService.DEFAULT_FLUSH_CACHE_SIZE;
    
    @Override
    public boolean validateEtag(String uri, String requestETag) {
        boolean valid = false;
        if (requestETag != null) {
            String idEntry = uriEtagEntryFactory.resolveId(uri);
            try {
                UriEtagEntry entry = uriEtagEntryDao.consultar(idEntry);
                String storedETag = entry.getEtag();
                if (StringUtils.equals(requestETag, storedETag)) {
                    valid = true;
                }
            } catch (NotFoundException ex) {
            }
        }
        return valid;
    }
    
    @Override
    public void setEtag(HttpServletRequest request, HttpServletResponse response) {
        String entryId = uriEtagEntryFactory.resolveId(request.getRequestURI());
        UriEtagEntry entry;
        try {
            entry = uriEtagEntryDao.consultar(entryId);
        } catch (NotFoundException e) {
            entry = uriEtagEntryFactory.buildUriEtagEntry(request.getRequestURI());
            uriEtagEntryDao.guardar(entry);
        }
        response.setHeader("Etag", entry.getEtag());
    }

    @Override
    public void updateUriEtagEntry(String uri) {
        UriEtagEntry entry = uriEtagEntryFactory.buildUriEtagEntry(uri);
        uriEtagEntryDao.guardar(entry);
    }

    @Override
    public void updateAllUriEtagEntry() {
        PaginationDataString pag = new PaginationDataString();
        pag.setMaxResults(flushCacheSize);
        do {
            List<UriEtagEntry> entryList = uriEtagEntryDao.consultarTodos(null, pag);
            for (UriEtagEntry x: entryList) {
                UriEtagEntry entry = uriEtagEntryFactory.buildUriEtagEntry(x.getUri());
                uriEtagEntryDao.guardar(entry);
            }
            pag.setSinceId(pag.getNextId());
        } while (pag.hasNextId());
    }

    public void setFlushCacheSize(int flushCacheSize) {
        this.flushCacheSize = flushCacheSize;
    }

    public void setUriEtagEntryFactory(UriEtagEntryFactory uriEtagEntryFactory) {
        this.uriEtagEntryFactory = uriEtagEntryFactory;
    }

    public void setUriEtagEntryDao(UriEtagEntryDao uriEtagEntryDao) {
        this.uriEtagEntryDao = uriEtagEntryDao;
    }    
}
