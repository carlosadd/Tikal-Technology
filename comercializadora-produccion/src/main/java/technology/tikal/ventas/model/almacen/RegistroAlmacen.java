package technology.tikal.ventas.model.almacen;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenTransient;
import technology.tikal.ventas.model.producto.Producto;

@JsonSubTypes({
    @JsonSubTypes.Type(value = RegistroAlmacenTransient.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface RegistroAlmacen {

    Long getPedidoId();
    Long getId();
    Producto getProducto();
    Long getIdProveedor();
    Long getCantidad();
    Date getFechaRegistro();
}
