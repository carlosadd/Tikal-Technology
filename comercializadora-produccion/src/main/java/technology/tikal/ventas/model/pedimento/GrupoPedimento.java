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
package technology.tikal.ventas.model.pedimento;

import java.util.Date;
import java.util.LinkedList;
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
@JsonTypeName("GrupoPedimento")
public class GrupoPedimento implements Pedimento {

    private Long pedidoId;
    private Long idProveedor;
    private LineaDeProductos linea;
    private List<Pedimento> pedimentos;
    private Date fechaDeCreacion;
    
    public GrupoPedimento(Long pedidoId, Long idProveedor, LineaDeProductos linea) {
        pedimentos = new LinkedList<>();
        this.pedidoId = pedidoId;
        this.idProveedor = idProveedor;
        this.linea = linea;
    }
    
    @Override
    public Long getPedidoId() {
        return pedidoId;
    }
    @Override
    public Long getId() {
        return null;
    }
    @Override
    public Producto getProducto() {
        return null;
    }
    @Override
    public Long getIdProveedor() {
        return idProveedor;
    }
    @Override
    public Long getCantidad() {
        long cantidad = 0;
        for (Pedimento x: pedimentos) {
            cantidad = cantidad + x.getCantidad();
        }
        return cantidad;
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

    public Pedimento[] getPedimentos() {
        Pedimento[] response = new Pedimento[pedimentos.size()];
        pedimentos.toArray(response);
        return response;
    }
    
    public void addPedimento(Pedimento pedimento) {
        if (!pedimento.getIdProveedor().equals(this.idProveedor)) {
            throw new IllegalArgumentException();
        }
        this.pedimentos.add(pedimento);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrupoPedimento other = (GrupoPedimento) obj;
        return Objects.equals(this.pedidoId, other.pedidoId)
                && Objects.equals(this.linea.getId(), other.linea.getId())
                && Objects.equals(this.linea.getCatalogoId(), other.linea.getCatalogoId())
                && Objects.equals(this.idProveedor, other.idProveedor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.pedidoId, this.linea.getId(), this.linea.getCatalogoId(), this.idProveedor);
    }
}
