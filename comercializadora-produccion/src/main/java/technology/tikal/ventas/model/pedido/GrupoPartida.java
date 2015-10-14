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
package technology.tikal.ventas.model.pedido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeName;

import technology.tikal.ventas.model.producto.LineaDeProductos;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
@JsonTypeName("GrupoPartida")
public class GrupoPartida implements Partida {

    private Long pedidoId;
    private LineaDeProductos linea;
    private List<Partida> partidas;
    private Date fechaDeCreacion;
    
    public GrupoPartida(Long pedidoId, LineaDeProductos linea) {
        partidas = new ArrayList<>();
        this.pedidoId = pedidoId;
        this.linea = linea;
    }
    
    @Override
    public Long getId() {
        return null;
    }
    
    @Override
    public Long getPedidoId() {
        return pedidoId;
    }

    @Override
    public Producto getProducto() {
        return null;
    }

    @Override
    public void setProducto(Producto producto) {
    }

    @Override
    public Long getCantidad() {
        long cantidad = 0;
        for (Partida x: partidas) {
            cantidad = cantidad + x.getCantidad();
        }
        return cantidad;
    }

    @Override
    public void setCantidad(Long cantidad) {
    }
    
    public void updateFechaCreacion(Date fecha) {
        if (fechaDeCreacion == null || fecha.before(fechaDeCreacion)) {
            this.fechaDeCreacion = fecha;
        }
    }

    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public LineaDeProductos getLinea() {
        return linea;
    }

    public Partida[] getPartidas() {
        Partida[] response = new Partida[partidas.size()];
        partidas.toArray(response);
        return response;
    }
    
    public void addPartida(Partida partida) {
        this.partidas.add(partida);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrupoPartida other = (GrupoPartida) obj;
        return Objects.equals(this.pedidoId, other.pedidoId)
                && Objects.equals(this.linea.getId(), other.linea.getId())
                && Objects.equals(this.linea.getCatalogoId(), other.getLinea().getCatalogoId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.pedidoId, this.linea.getId(), this.linea.getCatalogoId());
    }
}
