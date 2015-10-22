package technology.tikal.ventas.model.almacen.ofy;

import org.springframework.beans.BeanUtils;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.almacen.Entrada;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class EntradaOfy extends RegistroAlmacenOfy implements Entrada, OfyEntity<Entrada> {

    
    protected EntradaOfy() {
        super();
    }

    public EntradaOfy(RegistroAlmacenOfy base) {
        super(base);
    }

    public EntradaOfy(Pedido owner, Producto producto, Long idProveedor) {
        super(owner, producto, idProveedor);
    }

    @Override
    public void update(Entrada source) {
        BeanUtils.copyProperties(source, this, "owner", "producto", "idProveedor");
    }
}
