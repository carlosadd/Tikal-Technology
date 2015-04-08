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
package technology.tikal.gae.pagination.model;

/**
 * @author Nekorp 
 */
public class Page<T> {

    private String type;
    private String linkCurrentPage;
    private String linkNextPage;
    private T items;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    public String getLinkCurrentPage() {
        return linkCurrentPage;
    }
    public void setLinkCurrentPage(String linkCurrentPage) {
        this.linkCurrentPage = linkCurrentPage;
    }
    public String getLinkNextPage() {
        return linkNextPage;
    }
    public void setLinkNextPage(String linkNextPage) {
        this.linkNextPage = linkNextPage;
    }
    public T getItems() {
        return items;
    }
    public void setItems(T items) {
        this.items = items;
    }
}
