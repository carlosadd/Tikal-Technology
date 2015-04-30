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
package technology.tikal.taller.automotriz.model.index.servicio;

import java.util.Date;

import technology.tikal.taller.automotriz.model.cobranza.DatosCobranza;
import technology.tikal.taller.automotriz.model.servicio.moneda.Moneda;

/**
 * @author Nekorp
 */
public class ServicioIndex {

    private Long id;
    private String status;
    private String descripcion;
    private Date fechaInicio;
    private Long idCliente;
    private String idAuto;
    private DatosCobranza cobranza;
    private Moneda costoTotal;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Date getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public Long getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
    public String getIdAuto() {
        return idAuto;
    }
    public void setIdAuto(String idAuto) {
        this.idAuto = idAuto;
    }
    public DatosCobranza getCobranza() {
        return cobranza;
    }
    public void setCobranza(DatosCobranza cobranza) {
        this.cobranza = cobranza;
    }
    public Moneda getCostoTotal() {
        return costoTotal;
    }
    public void setCostoTotal(Moneda costoTotal) {
        this.costoTotal = costoTotal;
    }
    
}
