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
package technology.tikal.customers.dao.filter;

import technology.tikal.gae.dao.template.FiltroBusqueda;

/**
 * 
 * @author Nekorp
 *
 */
public class CustomerFilter implements FiltroBusqueda {

    private boolean active;
    
    private CustomerFilterSmall delegate;

    public CustomerFilter(CustomerFilterSmall delegate) {
        this.delegate = delegate;
        this.active = true;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFilter() {
        return delegate.getFilter();
    }

    public void setFilter(String filter) {
        delegate.setFilter(filter);
    }

    public String getIndex() {
        return delegate.getIndex();
    }

    public void setIndex(String index) {
        delegate.setIndex(index);
    }
    
    public String getGroup() {
        return delegate.getGroup();
    }

    public void setGroup(String group) {
        delegate.setGroup(group);
    }
}
