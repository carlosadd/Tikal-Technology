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
package technology.tikal.ventas.service.pedido;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.gae.service.template.RestControllerTemplate;
import technology.tikal.ventas.controller.pedido.PedidoController;
import technology.tikal.ventas.model.pedido.Pedido;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
@RequestMapping("/pedido")
public class PedidoService extends RestControllerTemplate {

    private PedidoController pedidoController;
    
    @RequestMapping(produces = "application/json;charset=UTF-8", value="/{pedidoId}", method = RequestMethod.GET)
    public Pedido get(@PathVariable final Long pedidoId) {
        return pedidoController.get(pedidoId);
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }
}
