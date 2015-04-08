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
package technology.tikal.customers.dao.objectify.paginator;

import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.gae.pagination.model.PaginationDataDual;

/**
 * 
 * @author Nekorp
 *
 * @param <T> parent type
 */
public class PaginationContext<T> {

    private String indexOfy;
    private String index;
    private FiltroBusqueda filter;
    private PaginationDataDual<Long, String> pagination;
    private T parent;
    
    public PaginationContext(String indexOfy, String index, FiltroBusqueda filter, PaginationDataDual<Long, String> pagination) {
        this.indexOfy = indexOfy;
        this.index = index;
        this.filter = filter;
        this.pagination = pagination;
    }
    
    public PaginationContext(String indexOfy, String index, FiltroBusqueda filter, PaginationDataDual<Long, String> pagination, T parent) {
        this(indexOfy, index, filter, pagination);
        this.parent = parent;
    }
    
    public String getIndexOfy() {
        return indexOfy;
    }
    public void setIndexOfy(String indexOfy) {
        this.indexOfy = indexOfy;
    }
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public FiltroBusqueda getFilter() {
        return filter;
    }
    public void setFilter(FiltroBusqueda filter) {
        this.filter = filter;
    }
    public PaginationDataDual<Long, String> getPagination() {
        return pagination;
    }
    public void setPagination(PaginationDataDual<Long, String> pagination) {
        this.pagination = pagination;
    }
    public T getParent() {
        return parent;
    }
    public void setParent(T parent) {
        this.parent = parent;
    }
    
}
