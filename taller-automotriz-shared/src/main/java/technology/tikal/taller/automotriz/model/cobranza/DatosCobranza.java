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
package technology.tikal.taller.automotriz.model.cobranza;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Nekorp
 */
public class DatosCobranza {
    private boolean facturado;
    private String numeroFactura;
    private Date inicio;
    List<PagoCobranza> pagos;
    
    public DatosCobranza() {
        facturado = false;
        numeroFactura = "";
        inicio = new Date();
        pagos = new LinkedList<>();
    }

    public boolean isFacturado() {
        return facturado;
    }

    public void setFacturado(boolean facturado) {
        this.facturado = facturado;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }
    
    public List<PagoCobranza> getPagos() {
        LinkedList<PagoCobranza> r = new LinkedList<>();
        for (PagoCobranza x: this.pagos) {
            r.add(x);
        }
        return r;
    }

    public void setPagos(List<PagoCobranza> param) {
        this.pagos = new LinkedList<>();
        for (PagoCobranza x: param) {
            this.pagos.add(x);
        }
    }
}
