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
@JsonTypeName("NombrePersonaMx")
@NotEmptyPojo
public class NombrePersonaMx extends Name {
    
    @Length(max=20)
    @Pattern(regexp="^[\\p{IsLatin}\\w]+( [\\p{IsLatin}\\w']+)*")
    private String primerNombre;
    @Length(max=20)
    @Pattern(regexp="^[\\p{IsLatin}\\w]+( [\\p{IsLatin}\\w']+)*")
    private String segundoNombre;
    @Length(max=20)
    @Pattern(regexp="^[\\p{IsLatin}\\w]+( [\\p{IsLatin}\\w']+)*")
    private String apellidoPaterno;
    @Length(max=20)
    @Pattern(regexp="^[\\p{IsLatin}\\w]+( [\\p{IsLatin}\\w']+)*")
    private String apellidoMaterno;
    public String getPrimerNombre() {
        return primerNombre;
    }
    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }
    public String getSegundoNombre() {
        return segundoNombre;
    }
    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(primerNombre, segundoNombre, apellidoPaterno, apellidoMaterno);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof NombrePersonaMx) {
            NombrePersonaMx other = (NombrePersonaMx) obj;
            return Objects.equals(primerNombre, other.primerNombre)
                    && Objects.equals(segundoNombre, other.segundoNombre)
                    && Objects.equals(apellidoPaterno, other.apellidoPaterno)
                    && Objects.equals(apellidoMaterno, other.apellidoMaterno);
        } 
        return false;
    }
}
