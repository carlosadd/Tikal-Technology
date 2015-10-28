package technology.tikal.ventas.model.almacen.ofy;

import org.springframework.beans.BeanUtils;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.almacen.EntradaDevolucion;
import technology.tikal.ventas.model.almacen.RegistroAlmacen;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class EntradaDevolucionOfy extends RegistroAlmacenOfy implements EntradaDevolucion, OfyEntity<EntradaDevolucion> {

    @Index
    @JsonIgnore
    private Ref<RegistroAlmacen> origen;
    
    protected EntradaDevolucionOfy() {
        super();
    }

    public EntradaDevolucionOfy(RegistroAlmacenOfy base) {
        super(base);
    }

    public EntradaDevolucionOfy(Pedido owner, Producto producto, Long idProveedor) {
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
    public void update(EntradaDevolucion source) {
        BeanUtils.copyProperties(source, this, "owner", "producto", "idProveedor");
    }
}
