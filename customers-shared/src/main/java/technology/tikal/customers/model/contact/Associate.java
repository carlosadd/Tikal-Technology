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
package technology.tikal.customers.model.contact;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * 
 * @author Nekorp
 *
 */
@JsonDeserialize(as=AssociatePojo.class)
@JsonTypeName("Associate")
public interface Associate extends Contact {

    @NotNull
    @Length(max=40)
    @Pattern(regexp="^[\\p{IsLatin}\\w\\.]+( [\\p{IsLatin}\\w\\.]+)*")
    public String getRole();

    public void setRole(String role);
    
}
