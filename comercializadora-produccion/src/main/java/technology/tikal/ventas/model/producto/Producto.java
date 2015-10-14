/**
 *   Copyright 2015 Tikal-Technology
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */
package technology.tikal.ventas.model.producto;

import technology.tikal.ventas.model.producto.ofy.intermediario.ProductoConTalla;
import technology.tikal.ventas.model.producto.ofy.intermediario.ProductoIntermediario;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 
 * @author Nekorp
 *
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = ProductoIntermediario.class),
    @JsonSubTypes.Type(value = ProductoReference.class),
    @JsonSubTypes.Type(value = ProductoConTalla.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface Producto {
    Long getCatalogoId();
    Long getId();
}
