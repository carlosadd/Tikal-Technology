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
package technology.tikal.customers.model.name;

import org.springframework.beans.BeanUtils;

import technology.tikal.customers.dao.filter.NamePriorityFilterValues;
import technology.tikal.customers.model.OfyEntity;
import technology.tikal.string.util.StringNormalizer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;
import com.googlecode.objectify.condition.IfNotEmpty;

/**
 * 
 * @author Nekorp
 *
 */
@Subclass
public class OrganizationNameIndexed extends OrganizationName implements OfyEntity<OrganizationName>, IndexedByString {    

    @Index({IfNotEmpty.class}) @JsonIgnore
    private String normalizedOrganizationName;
    @JsonIgnore
    private boolean active;
    
    private OrganizationNameIndexed() {
        super();
        this.active = true;
    }
    public OrganizationNameIndexed(OrganizationName source) {
        this();
        this.update(source);
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    @Override
    public void update(OrganizationName source) {
        BeanUtils.copyProperties(source, this);
    }
    @Override
    public void updateInternalIndex() {
        if (active) {
            normalizedOrganizationName = StringNormalizer.normalize(this.getName());
        } else {
            normalizedOrganizationName = null;
        }
    }
    @Override
    public String getIndex(String idIndex) {
        NamePriorityFilterValues type = Enum.valueOf(NamePriorityFilterValues.class, idIndex);
        if (type == NamePriorityFilterValues.Name || type == NamePriorityFilterValues.OrganizationName) {
            return this.normalizedOrganizationName;
        }
        throw new IllegalArgumentException("tipo no soportado:" + type.toString());
    }
    @Override
    public boolean isActive() {
        return active;
    }
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
