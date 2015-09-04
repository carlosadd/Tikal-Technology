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
package technology.tikal.customers.model;

import org.springframework.beans.BeanUtils;

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
public class GroupOfy extends Group implements OfyEntity<Group> {

    @Index({IfNotEmpty.class}) @JsonIgnore
    private String standarizedName;

    private GroupOfy() {
        super();
    }
    
    public GroupOfy(Group source) {
        this();
        if (source != null) {
            this.update(source);
        }
    }
    
    @Override
    public void setName(String name) {
        super.setName(name);
        standarizedName = StringNormalizer.normalize(name);
    }

    @Override
    public void update(Group source) {
        BeanUtils.copyProperties(source, this);
    }
}
