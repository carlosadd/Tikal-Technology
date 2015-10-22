package technology.tikal.ventas.model.almacen.ofy;

import org.springframework.beans.BeanUtils;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.almacen.SalidaDevolucion;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class SalidaDevolucionOfy extends RegistroAlmacenOfy implements SalidaDevolucion, OfyEntity<SalidaDevolucion> {

    
    protected SalidaDevolucionOfy() {
        super();
    }

    public SalidaDevolucionOfy(RegistroAlmacenOfy base) {
        super(base);
    }

    public SalidaDevolucionOfy(Pedido owner, Producto producto, Long idProveedor) {
        super(owner, producto, idProveedor);
    }

    @Override
    public void update(SalidaDevolucion source) {
        BeanUtils.copyProperties(source, this, "owner", "producto", "idProveedor");
    }
}
