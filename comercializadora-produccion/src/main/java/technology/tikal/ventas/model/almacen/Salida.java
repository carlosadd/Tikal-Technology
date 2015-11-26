package technology.tikal.ventas.model.almacen;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import technology.tikal.ventas.model.almacen.ofy.intermediario.SalidaIntermediario;

@JsonSubTypes({
    @JsonSubTypes.Type(value = SalidaIntermediario.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface Salida extends RegistroAlmacen {

    Long getReferenciaEnvio();
}
