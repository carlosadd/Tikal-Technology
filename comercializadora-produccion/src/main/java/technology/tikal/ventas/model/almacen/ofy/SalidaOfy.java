package technology.tikal.ventas.model.almacen.ofy;

import org.springframework.beans.BeanUtils;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.almacen.Salida;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SalidaOfy extends RegistroAlmacenOfy implements Salida, OfyEntity<Salida> {

    @Index
    private Long referenciaEnvio;
    
    protected SalidaOfy() {
        super();
    }

    public SalidaOfy(RegistroAlmacenOfy base) {
        super(base);
    }

    public SalidaOfy(Pedido owner, Producto producto, Long idProveedor) {
        super(owner, producto, idProveedor);
    }

    public Long getReferenciaEnvio() {
        return referenciaEnvio;
    }

    public void setReferenciaEnvio(Long referenciaEnvio) {
        this.referenciaEnvio = referenciaEnvio;
    }

    @Override
    public void update(Salida source) {
        BeanUtils.copyProperties(source, this, "owner", "producto", "idProveedor");
    }
}
