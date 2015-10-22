package technology.tikal.ventas.model.almacen.ofy;

import org.springframework.beans.BeanUtils;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.almacen.EntradaDevolucion;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class EntradaDevolucionOfy extends RegistroAlmacenOfy implements EntradaDevolucion, OfyEntity<EntradaDevolucion> {

    
    protected EntradaDevolucionOfy() {
        super();
    }

    public EntradaDevolucionOfy(RegistroAlmacenOfy base) {
        super(base);
    }

    public EntradaDevolucionOfy(Pedido owner, Producto producto, Long idProveedor) {
        super(owner, producto, idProveedor);
    }

    @Override
    public void update(EntradaDevolucion source) {
        BeanUtils.copyProperties(source, this, "owner", "producto", "idProveedor");
    }
}
