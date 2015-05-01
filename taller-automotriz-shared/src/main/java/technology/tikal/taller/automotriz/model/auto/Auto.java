/**
 *   Copyright 2013-2015 Tikal-Technology
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
package technology.tikal.taller.automotriz.model.auto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
/**
 * Se usara para identificar como unico a aun auto, para el historia.
 * para el tema de datos del auto en un servicio en especifico se usara el termino datos auto.
 * @author Nekorp
 */
public class Auto {
    
    @Size(min=1, max=17)
    @NotNull
    @Pattern(regexp="[\\p{Alnum}\\-]*")
    private String numeroSerie;
    @Size(min=1)
    @NotNull
    private String marca;
    @Size(min=1)
    @NotNull
    private String tipo;
    @Size(min=1)
    @NotNull
    private String version;
    @Size(min=1)
    @NotNull
    private String modelo;
    @Size(min=1)
    @NotNull
    private String color;
    @Size(min=1, max=10)
    @NotNull
    private String placas;
    @Valid
    private Equipamiento equipamiento;
    
    public Auto() {
        this.numeroSerie = "";
        this.marca = "";
        this.tipo = "";
        this.version = "";
        this.modelo = "";
        this.color = "";
        this.placas = "";
        this.equipamiento = new Equipamiento();
    }
    
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public Equipamiento getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(Equipamiento equipamiento) {
        this.equipamiento = equipamiento;
    }

}
