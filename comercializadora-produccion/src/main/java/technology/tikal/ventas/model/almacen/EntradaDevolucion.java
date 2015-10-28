package technology.tikal.ventas.model.almacen;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import technology.tikal.ventas.model.almacen.ofy.intermediario.EntradaDevolucionIntermediario;

@JsonSubTypes({
    @JsonSubTypes.Type(value = EntradaDevolucionIntermediario.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface EntradaDevolucion extends RegistroAlmacen {

    RegistroAlmacen getOrigen();
}
