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
package technology.tikal.ventas.model.pedido.ofy.intermediario;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.googlecode.objectify.annotation.Subclass;

import technology.tikal.ventas.model.address.PuntoEntrega;
import technology.tikal.ventas.model.pedido.ofy.PedidoRaizOfy;

/**
 * 
 * @author Nekorp
 *
 */
@JsonTypeName("PedidoIntermediario")
@Subclass(index=true)
public class PedidoIntermediario extends PedidoRaizOfy {

    private Long idCliente;
    @NotNull
    @Length(max=40)
    @Pattern(regexp="^[\\w\\p{IsLatin}\\p{Punct}]+( [\\w\\p{IsLatin}\\p{Punct}]+)*")
    private String nombre;
    @Valid
    private PuntoEntrega puntoEntrega;
    
    public Long getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public PuntoEntrega getPuntoEntrega() {
        return puntoEntrega;
    }
    public void setPuntoEntrega(PuntoEntrega puntoEntrega) {
        this.puntoEntrega = puntoEntrega;
    }
}
