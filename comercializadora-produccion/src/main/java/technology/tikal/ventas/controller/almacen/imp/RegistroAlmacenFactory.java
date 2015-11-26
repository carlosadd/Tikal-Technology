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
package technology.tikal.ventas.controller.almacen.imp;

import technology.tikal.ventas.model.almacen.RegistroAlmacen;
import technology.tikal.ventas.model.almacen.ofy.EntradaDevolucionOfy;
import technology.tikal.ventas.model.almacen.ofy.EntradaOfy;
import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenOfy;
import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenTransient;
import technology.tikal.ventas.model.almacen.ofy.SalidaDevolucionOfy;
import technology.tikal.ventas.model.almacen.ofy.SalidaOfy;
import technology.tikal.ventas.model.almacen.ofy.intermediario.EntradaDevolucionIntermediario;
import technology.tikal.ventas.model.almacen.ofy.intermediario.EntradaIntermediario;
import technology.tikal.ventas.model.almacen.ofy.intermediario.SalidaDevolucionIntermediario;
import technology.tikal.ventas.model.almacen.ofy.intermediario.SalidaIntermediario;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

/**
 * 
 * @author Nekorp
 *
 */
public class RegistroAlmacenFactory {

    public static <T extends RegistroAlmacen> T build(Pedido owner, RegistroAlmacenTransient request, Producto producto, Class<T> type) {
        if (type == EntradaOfy.class) {
            EntradaIntermediario entrada = new EntradaIntermediario(owner, producto, request.getIdProveedor());
            entrada.setCantidad(request.getCantidad());
            entrada.setFechaRegistro(request.getFechaRegistro());
            entrada.setDescripcion(request.getDescripcion());
            entrada.setTag(request.getTag());
            return (T) entrada;
        }
        if (type == SalidaOfy.class) {
            SalidaIntermediario entrada = new SalidaIntermediario(owner, producto);
            entrada.setCantidad(request.getCantidad());
            entrada.setFechaRegistro(request.getFechaRegistro());
            entrada.setDescripcion(request.getDescripcion());
            entrada.setTag(request.getTag());
            entrada.setReferenciaEnvio(request.getReferenciaEnvio());
            return (T) entrada;
        }
        throw new IllegalArgumentException();
    }
    
    public static <T extends RegistroAlmacen> T buildDevolucion(Pedido owner, RegistroAlmacenTransient request, Producto producto, Class<T> type, RegistroAlmacen referencia) {
        if (type == EntradaDevolucionOfy.class) {
            EntradaDevolucionIntermediario entrada = new EntradaDevolucionIntermediario(owner, producto, request.getIdProveedor());
            entrada.setCantidad(request.getCantidad());
            entrada.setFechaRegistro(request.getFechaRegistro());
            entrada.setDescripcion(request.getDescripcion());
            entrada.setTag(request.getTag());
            entrada.setOrigen(referencia);
            return (T) entrada;
        }
        if (type == SalidaDevolucionOfy.class) {
            SalidaDevolucionIntermediario entrada = new SalidaDevolucionIntermediario(owner, producto, request.getIdProveedor());
            entrada.setCantidad(request.getCantidad());
            entrada.setFechaRegistro(request.getFechaRegistro());
            entrada.setDescripcion(request.getDescripcion());
            entrada.setTag(request.getTag());
            entrada.setOrigen(referencia);
            return (T) entrada;
        }
        throw new IllegalArgumentException();
    }

    public static RegistroAlmacenOfy clone(RegistroAlmacenOfy source) {
        if (source instanceof EntradaIntermediario) {
            EntradaIntermediario castedSource = (EntradaIntermediario) source;
            EntradaIntermediario response = new EntradaIntermediario(castedSource);
            response.update(castedSource);
            return response;
        }
        if (source instanceof SalidaIntermediario) {
            SalidaIntermediario castedSource = (SalidaIntermediario) source;
            SalidaIntermediario response = new SalidaIntermediario(castedSource);
            response.update(castedSource);
            return response;
        }
        if (source instanceof EntradaDevolucionIntermediario) {
            EntradaDevolucionIntermediario castedSource = (EntradaDevolucionIntermediario) source;
            EntradaDevolucionIntermediario response = new EntradaDevolucionIntermediario(castedSource);
            response.update(castedSource);
            return response;
        }
        if (source instanceof SalidaDevolucionIntermediario) {
            SalidaDevolucionIntermediario castedSource = (SalidaDevolucionIntermediario) source;
            SalidaDevolucionIntermediario response = new SalidaDevolucionIntermediario(castedSource);
            response.update(castedSource);
            return response;
        }
        throw new IllegalArgumentException();
    }
}
