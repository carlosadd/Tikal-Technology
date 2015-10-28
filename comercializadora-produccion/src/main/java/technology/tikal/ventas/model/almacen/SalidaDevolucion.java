package technology.tikal.ventas.model.almacen;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import technology.tikal.ventas.model.almacen.ofy.intermediario.SalidaDevolucionIntermediario;

@JsonSubTypes({
    @JsonSubTypes.Type(value = SalidaDevolucionIntermediario.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface SalidaDevolucion extends RegistroAlmacen {

    RegistroAlmacen getOrigen();
}
