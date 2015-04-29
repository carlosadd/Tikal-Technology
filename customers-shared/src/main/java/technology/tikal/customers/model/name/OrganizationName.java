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

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 
 * @author Nekorp
 *
 */
@JsonTypeName("OrganizationName")
public class OrganizationName extends Name {

    @NotNull
    @Length(max=83)
    @Pattern(regexp="^[\\w\\p{IsLatin}\\p{Punct}]+( [\\w\\p{IsLatin}\\p{Punct}]+)*")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof OrganizationName) {
            OrganizationName other = (OrganizationName) obj;
            return Objects.equals(name, other.name)
                    && Objects.equals(name, other.name);
        } 
        return false;
    }
}
