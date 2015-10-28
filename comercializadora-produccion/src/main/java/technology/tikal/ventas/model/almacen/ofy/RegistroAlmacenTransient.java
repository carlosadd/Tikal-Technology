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

import technology.tikal.ventas.model.almacen.RegistroAlmacen;
import technology.tikal.ventas.model.producto.Producto;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 
 * @author Nekorp
 *
 */
@JsonTypeName("RegistroAlmacenTransient")
public class RegistroAlmacenTransient implements RegistroAlmacen {

    private Long pedidoId;
    private Long id;
    private Long cantidad;
    private Producto producto;
    private Long idProveedor;
    private Date fechaRegistro;
    private Long referenciaRegistro;
    private String descripcion;
    
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
}
