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
package technology.tikal.taller.automotriz.model.servicio.metadata;

import java.util.Date;

import technology.tikal.taller.automotriz.model.servicio.moneda.Moneda;
/**
 * @author Nekorp
 */
public class ServicioMetadata {

    private Date fechaInicio;
    private String status;
    private Moneda costoTotal;
    public ServicioMetadata() {
        fechaInicio = new Date();
        status = ServicioStatusConstants.activo;
        costoTotal = new Moneda();
    }
    public Date getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Moneda getCostoTotal() {
        return costoTotal;
    }
    public void setCostoTotal(Moneda costoTotal) {
        this.costoTotal = costoTotal;
    }
}
