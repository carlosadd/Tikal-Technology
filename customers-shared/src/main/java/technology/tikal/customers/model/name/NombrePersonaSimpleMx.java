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

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import technology.tikal.hibernate.validation.NotEmptyPojo;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 
 * @author Nekorp
 *
 */
@JsonTypeName("NombrePersonaSimpleMx")
@NotEmptyPojo
public class NombrePersonaSimpleMx extends Name {

    @Length(max=41)
    @Pattern(regexp="^[\\p{IsLatin}\\w]+( [\\p{IsLatin}\\w']+)*")
    private String nombres;
    @Length(max=41)
    @Pattern(regexp="^[\\p{IsLatin}\\w]+( [\\p{IsLatin}\\w']+)*")
    private String apellidos;
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nombres, apellidos);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof NombrePersonaSimpleMx) {
            NombrePersonaSimpleMx other = (NombrePersonaSimpleMx) obj;
            return Objects.equals(nombres, other.nombres)
                    && Objects.equals(apellidos, other.apellidos);
        } 
        return false;
    }
}
