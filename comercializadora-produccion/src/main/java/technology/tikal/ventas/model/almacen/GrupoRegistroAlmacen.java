package technology.tikal.ventas.model.almacen;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeName;

import technology.tikal.ventas.model.producto.LineaDeProductos;
import technology.tikal.ventas.model.producto.Producto;

@JsonTypeName("GrupoRegistroAlmacen")
public class GrupoRegistroAlmacen implements Entrada, Salida {

    private Long pedidoId;
    private Long idProveedor;
    private LineaDeProductos linea;
    private List<RegistroAlmacen> registros;
    private Long referenciaEnvio;
    private Date fechaDeCreacion;
    
    public GrupoRegistroAlmacen(Long pedidoId, Long idProveedor, LineaDeProductos linea) {
        registros = new LinkedList<>();
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
        for (RegistroAlmacen x: registros) {
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

    public RegistroAlmacen[] getRegistros() {
        RegistroAlmacen[] response = new RegistroAlmacen[registros.size()];
        registros.toArray(response);
        return response;
    }
    
    public void addRegistro(RegistroAlmacen pedimento) {
        if (!pedimento.getIdProveedor().equals(this.idProveedor)) {
            throw new IllegalArgumentException();
        }
        this.registros.add(pedimento);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrupoRegistroAlmacen other = (GrupoRegistroAlmacen) obj;
        return Objects.equals(this.pedidoId, other.pedidoId)
                && Objects.equals(this.linea.getId(), other.linea.getId())
                && Objects.equals(this.linea.getCatalogoId(), other.linea.getCatalogoId())
                && Objects.equals(this.idProveedor, other.idProveedor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.pedidoId, this.linea.getId(), this.linea.getCatalogoId(), this.idProveedor);
    }

    @Override
    public Date getFechaRegistro() {
        return null;
    }

    @Override
    public Long getReferenciaEnvio() {
        return referenciaEnvio;
    }

    public void setReferenciaEnvio(Long referenciaEnvio) {
        this.referenciaEnvio = referenciaEnvio;
    }
}
