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
package technology.tikal.ventas.dao.almacen;

import java.util.Date;

import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.ventas.model.almacen.RegistroAlmacen;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
public class RegistroAlmacenFilter implements FiltroBusqueda {

    private Producto producto;
    private Long idProveedor;
    private Date fechaInicio;
    private Date fechaFinal;
    private RegistroAlmacen origen;
    private Long referenciaEnvio;

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public RegistroAlmacen getOrigen() {
        return origen;
    }

    public void setOrigen(RegistroAlmacen origen) {
        this.origen = origen;
    }

    public Long getReferenciaEnvio() {
        return referenciaEnvio;
    }

    public void setReferenciaEnvio(Long referenciaEnvio) {
        this.referenciaEnvio = referenciaEnvio;
    }
}
