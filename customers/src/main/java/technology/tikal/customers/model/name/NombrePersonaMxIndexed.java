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
public class NombrePersonaMxIndexed extends NombrePersonaMx implements OfyEntity<NombrePersonaMx>, IndexedByString {

    @Index({IfNotEmpty.class}) @JsonIgnore
    private String normalizedPersonName;
    @Index({IfNotEmpty.class}) @JsonIgnore
    private String normalizedPersonSurname;
    @JsonIgnore
    private boolean active;
    
    private NombrePersonaMxIndexed() {
        super();
        this.active = true;
    }
    public NombrePersonaMxIndexed(NombrePersonaMx base) {
        this();
        this.update(base);
    }
    
    @Override
    public String toString() {
        return StringUtils.join(new String[]{
                this.getPrimerNombre()," ",
                this.getSegundoNombre()," ",
                this.getApellidoPaterno()," ",
                this.getApellidoMaterno()
        });
    }
    @Override
    public void update(NombrePersonaMx source) {
        BeanUtils.copyProperties(source, this);
    }
    @Override
    public void updateInternalIndex() {
        if (active) {
            if (StringUtils.isEmpty(this.getPrimerNombre()) && StringUtils.isEmpty(this.getSegundoNombre())) {
                this.normalizedPersonName = null;
            } else {
                this.normalizedPersonName = StringNormalizer.normalize(this.toString());
            }
            if (StringUtils.isEmpty(this.getApellidoPaterno()) && StringUtils.isEmpty(this.getApellidoMaterno())) {
                this.normalizedPersonSurname = null;
            } else {
                String apellidos = StringUtils.join(new String[]{
                        this.getApellidoPaterno()," ",
                        this.getApellidoMaterno()," ",
                        this.getPrimerNombre()," ",
                        this.getSegundoNombre()
                });
                this.normalizedPersonSurname = StringNormalizer.normalize(apellidos);
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
