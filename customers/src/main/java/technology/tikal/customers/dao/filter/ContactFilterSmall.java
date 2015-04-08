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

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import technology.tikal.gae.dao.template.FiltroBusqueda;

/**
 * 
 * @author Nekorp
 *
 */
public class ContactFilterSmall implements FiltroBusqueda {

    @Length(min=1, max=43)
    @Pattern(regexp="^[\\p{IsLatin}\\w]+( [\\p{IsLatin}\\w']+)*")
    private String filter;
    
    @Length(min=1, max=20)
    @Pattern(regexp="\\w*")
    private String index;
    
    public ContactFilterSmall() {
        this.index = NamePriorityFilterValues.Name.toString();
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
    
}
