package technology.tikal.ventas.model.almacen;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import technology.tikal.ventas.model.almacen.ofy.intermediario.EntradaIntermediario;

@JsonSubTypes({
    @JsonSubTypes.Type(value = EntradaIntermediario.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface Entrada extends RegistroAlmacen {

    
}
