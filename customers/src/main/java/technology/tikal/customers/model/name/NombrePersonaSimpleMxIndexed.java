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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import technology.tikal.customers.dao.filter.NamePriorityFilterValues;
import technology.tikal.customers.model.OfyEntity;
import technology.tikal.string.util.StringNormalizer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;
import com.googlecode.objectify.condition.IfNotEmpty;

/**
 * @author Nekorp
 * Esta pensado para casos donde se quiera indexar por alguno de los atributos.
 *
 */
@Subclass
public class NombrePersonaSimpleMxIndexed extends NombrePersonaSimpleMx implements OfyEntity<NombrePersonaSimpleMx>, IndexedByString {

    @Index({IfNotEmpty.class}) @JsonIgnore
    private String normalizedPersonName;
    @Index({IfNotEmpty.class}) @JsonIgnore
    private String normalizedPersonSurname;
    @JsonIgnore
    private boolean active;
    private NombrePersonaSimpleMxIndexed() {
        super();
        this.active = true;
    }
    public NombrePersonaSimpleMxIndexed(NombrePersonaSimpleMx source) {
        this();
        this.update(source);
    }
    
    @Override
    public String toString() {
        return StringUtils.join(new String[]{
                this.getNombres()," ",
                this.getApellidos()
        });
    }
    @Override
    public void update(NombrePersonaSimpleMx source) {
        BeanUtils.copyProperties(source, this);
    }
    @Override
    public void updateInternalIndex() {
        if (active) {
            if (StringUtils.isEmpty(getNombres())) {
                this.normalizedPersonName = null;
            } else {
               this.normalizedPersonName = StringNormalizer.normalize(this.toString());
            }
            if (StringUtils.isEmpty(getApellidos())) {
                this.normalizedPersonSurname = null;
            } else {
                String surname = StringUtils.join(new String[]{
                        this.getApellidos()," ",
                        this.getNombres()
                });
                this.normalizedPersonSurname = StringNormalizer.normalize(surname);
            }
        } else {
            this.normalizedPersonName = null;
            this.normalizedPersonSurname = null;
        }
    }
    @Override
    public String getIndex(String idIndex) {
        NamePriorityFilterValues type = Enum.valueOf(NamePriorityFilterValues.class, idIndex);
        if (type == NamePriorityFilterValues.PersonName) {
            return this.normalizedPersonName;
        }
        if (type == NamePriorityFilterValues.PersonSurname) {
            return this.normalizedPersonSurname;
        }
        if (type == NamePriorityFilterValues.Name) {
            return this.normalizedPersonName;
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
