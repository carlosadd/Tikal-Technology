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
package technology.tikal.taller.automotriz.model.servicio.bitacora;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Nekorp
 *
 */
public class Evento implements Comparable<Evento> {

    private Long id;
    /**
     * el tipo de evento.
     */
    private String tipo;
    /**
     * el nombre del evento.
     */
    private String etiqueta;
    /**
     * la fecha en la que fue creado el evento.
     */
    private Date fechaCreacion;
    /**
     * el responsable de crear el evento.
     */
    private String responsable;
    /**
     * la evidencia anexada al evento
     */
    private List<Evidencia> evidencia;
    /**
     * la fecha en la que sucedio el evento.
     */
    private Date fecha;
    /**
     * descripcion del evento.
     */
    private String descripcion;
    
    public Evento() {
        this.tipo = "";
        this.etiqueta = "";
        this.fechaCreacion = new Date();
        this.responsable = "";
        this.evidencia = new LinkedList<>();
        this.fecha = new Date();
        this.descripcion = "";
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

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String nombre) {
        this.etiqueta = nombre;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public List<Evidencia> getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(List<Evidencia> evidencia) {
        this.evidencia = evidencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int compareTo(Evento o) {
        return this.fechaCreacion.compareTo(o.getFechaCreacion());
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
        Evento other = (Evento) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
