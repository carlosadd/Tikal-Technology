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
package technology.tikal.ventas.model.almacen.ofy;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import technology.tikal.ventas.model.almacen.RegistroAlmacen;
import technology.tikal.ventas.model.producto.Producto;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 
 * @author Nekorp
 * Al parecer se necesitan dos objetos diferentes, uno para las entradas y otro para las salidas
 * por que las validaciones no son iguales.
 */
@JsonTypeName("RegistroAlmacenTransient")
public class RegistroAlmacenTransient implements RegistroAlmacen {

    @NotNull
    private Long pedidoId;
    private Long id;
    @NotNull
    private Long cantidad;
    @NotNull
    @Valid
    private Producto producto;
    @NotNull
    private Long idProveedor;
    @NotNull
    private Date fechaRegistro;
    private Long referenciaRegistro;
    private Long referenciaEnvio;
    @Length(max=360)
    @Pattern(regexp="^[\\w\\p{IsLatin}\\p{Punct}]+( [\\w\\p{IsLatin}\\p{Punct}]+)*")
    private String descripcion;
    @Length(max=40)
    @Pattern(regexp="^[\\w\\p{IsLatin}\\p{Punct}]+( [\\w\\p{IsLatin}\\p{Punct}]+)*")
    private String tag;
    
    @Override
    public Long getPedidoId() {
        return this.pedidoId;
    }
    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }
    @Override
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public Long getCantidad() {
        return cantidad;
    }
    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
    @Override
    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    @Override
    public Long getIdProveedor() {
        return idProveedor;
    }
    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }
    @Override
    public Date getFechaRegistro() {
        return fechaRegistro;
    }
    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    public Long getReferenciaRegistro() {
        return referenciaRegistro;
    }
    public void setReferenciaRegistro(Long referenciaRegistro) {
        this.referenciaRegistro = referenciaRegistro;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public Long getReferenciaEnvio() {
        return referenciaEnvio;
    }
    public void setReferenciaEnvio(Long referenciaEnvio) {
        this.referenciaEnvio = referenciaEnvio;
    }    
}
