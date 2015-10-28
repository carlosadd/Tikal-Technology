package technology.tikal.ventas.model.almacen.ofy;

import org.springframework.beans.BeanUtils;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.almacen.RegistroAlmacen;
import technology.tikal.ventas.model.almacen.SalidaDevolucion;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SalidaDevolucionOfy extends RegistroAlmacenOfy implements SalidaDevolucion, OfyEntity<SalidaDevolucion> {
    
    @Index
    @JsonIgnore
    private Ref<RegistroAlmacen> origen;
    
    protected SalidaDevolucionOfy() {
        super();
    }

    public SalidaDevolucionOfy(RegistroAlmacenOfy base) {
        super(base);
    }

    public SalidaDevolucionOfy(Pedido owner, Producto producto, Long idProveedor) {
        super(owner, producto, idProveedor);
    }
    
    public Long getOrigenId() {
        if (origen == null) {
            return null;
        }
        return origen.getKey().getId();
    }
    
    public RegistroAlmacen getOrigen() {
        if (origen == null) {
            return null;
        }
        return origen.get();
    }

    public void setOrigen(RegistroAlmacen origen) {
        if (origen == null) {
            this.origen = null;
        } else {
            this.origen = Ref.create(origen);
        }
    }

    @Override
    public void update(SalidaDevolucion source) {
        BeanUtils.copyProperties(source, this, "owner", "producto", "idProveedor");
    }
}
