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
package technology.tikal.ventas.controller.envio.imp;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.ventas.controller.almacen.SalidaController;
import technology.tikal.ventas.controller.envio.EnvioController;
import technology.tikal.ventas.controller.pedido.PedidoController;
import technology.tikal.ventas.dao.almacen.RegistroAlmacenFilter;
import technology.tikal.ventas.dao.envio.EnvioDao;
import technology.tikal.ventas.model.almacen.Salida;
import technology.tikal.ventas.model.envio.Envio;
import technology.tikal.ventas.model.envio.ofy.EnvioOfy;
import technology.tikal.ventas.model.pedido.Pedido;

/**
 * 
 * @author Nekorp
 *
 */
public class EnvioControllerImp implements EnvioController {

    private EnvioDao envioDao;
    private PedidoController pedidoController;
    private SalidaController salidaController;
    
    @Override
    public Envio crear(Long pedidoId, Envio request) {
        Pedido pedido = pedidoController.get(pedidoId);
        EnvioOfy nuevo = EnvioFactory.build(pedido, request);
        nuevo.update(request);
        envioDao.guardar(pedido, nuevo);
        return nuevo;
    }

    @Override
    public void actualizar(Long pedidoId, Long envioId, Envio request) {
        Pedido pedido = pedidoController.get(pedidoId);
        EnvioOfy original = envioDao.consultar(pedido, envioId);
        original.update(request);
        envioDao.guardar(pedido, original);
    }

    @Override
    public Envio[] consultar(Long pedidoId, PaginationDataLong pagination) {
        Pedido pedido = pedidoController.get(pedidoId);
        List<EnvioOfy> consulta = envioDao.consultarTodos(pedido, null, pagination);
        Envio[] response = new Envio[consulta.size()];
        consulta.toArray(response);
        return response;
    }

    @Override
    public Envio get(Long pedidoId, Long envioId) {
        Pedido pedido = pedidoController.get(pedidoId);
        return envioDao.consultar(pedido, envioId);
    }

    @Override
    public void borrar(Long pedidoId, Long envioId) {
        Pedido pedido = pedidoController.get(pedidoId);
        PaginationDataLong pagination= new PaginationDataLong();
        pagination.setMaxResults(1);
        RegistroAlmacenFilter filter = new RegistroAlmacenFilter();
        filter.setReferenciaEnvio(envioId);
        Salida[] search = salidaController.consultar(pedidoId, filter, pagination);
        if (search.length > 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"EnvioConAsignaciones.EnvioControllerImp.borrar"}, 
                    new String[]{pedidoId + ""}, 
                    "Hay salidas en el inventario con este envio"));
        }
        EnvioOfy target = envioDao.consultar(pedido, envioId);
        envioDao.borrar(pedido, target);
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

    public void setEnvioDao(EnvioDao envioDao) {
        this.envioDao = envioDao;
    }

    public void setSalidaController(SalidaController salidaController) {
        this.salidaController = salidaController;
    }
}
