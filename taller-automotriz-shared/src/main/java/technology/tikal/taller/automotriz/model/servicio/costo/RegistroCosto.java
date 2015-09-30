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
package technology.tikal.taller.automotriz.model.servicio.costo;

import java.util.Date;

import technology.tikal.taller.automotriz.model.servicio.moneda.Moneda;

/**
 * 
 * @author Nekorp
 *
 */
public class RegistroCosto {
    private Long id;
    private String tipo;
    private String subtipo;
    private String concepto;
    private Integer cantidad;
    private Moneda precioUnitario;
    private boolean precioUnitarioConIVA;
    private Moneda precioCliente;
    private boolean subtotalConIVA;
    private Date fechaCreacion;
    private String grupo;
    private boolean cotizado;
    private boolean autorizado;
    private Moneda precioCotizado;
    
    public RegistroCosto() {
        tipo = "";
        subtipo = "";
        concepto = "";
        cantidad = 0;
        precioUnitario = new Moneda();
        precioCliente = new Moneda();
        fechaCreacion = new Date();
        grupo = "";
        cotizado = false;
        autorizado = false;
        precioCotizado = new Moneda();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSubtipo() {
        return subtipo;
    }

    public void setSubtipo(String subtipo) {
        this.subtipo = subtipo;
    }
    
    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Moneda getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Moneda precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Moneda getPrecioCliente() {
        return precioCliente;
    }

    public void setPrecioCliente(Moneda precioCliente) {
        this.precioCliente = precioCliente;
    }    

    public boolean isPrecioUnitarioConIVA() {
        return precioUnitarioConIVA;
    }

    public void setPrecioUnitarioConIVA(boolean precioUnitarioConIVA) {
        this.precioUnitarioConIVA = precioUnitarioConIVA;
    }

    public boolean isSubtotalConIVA() {
        return subtotalConIVA;
    }

    public void setSubtotalConIVA(boolean subtotalConIVA) {
        this.subtotalConIVA = subtotalConIVA;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public boolean isCotizado() {
        return cotizado;
    }

    public void setCotizado(boolean cotizado) {
        this.cotizado = cotizado;
    }    

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }

    public Moneda getPrecioCotizado() {
        return precioCotizado;
    }

    public void setPrecioCotizado(Moneda precioCotizado) {
        this.precioCotizado = precioCotizado;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegistroCosto other = (RegistroCosto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}
