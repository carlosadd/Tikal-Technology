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
package technology.tikal.ventas.service.envio;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.pagination.PaginationModelFactory;
import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.gae.service.template.RestControllerTemplate;
import technology.tikal.ventas.controller.envio.EnvioController;
import technology.tikal.ventas.model.envio.Envio;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
@RequestMapping("/pedido/{pedidoId}/envio")
public class EnvioService extends RestControllerTemplate {

    private EnvioController envioController;
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void crear(@PathVariable final Long pedidoId, 
            @Valid @RequestBody final Envio request, final BindingResult result,
            final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        Envio nuevo = envioController.crear(pedidoId, request);
        httpResponse.setHeader("Location", httpRequest.getRequestURI() + "/" + nuevo.getId());
    }
    
    @RequestMapping(value="/{envioId}", method = RequestMethod.POST)
    public void actualizar(@PathVariable final Long pedidoId, @PathVariable final Long envioId,
            @Valid @RequestBody final Envio request, final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        if (Long.compare(request.getId(), envioId) != 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                new String[]{"NoValidRequest.PedimentoService.actualizar"}, 
                new String[]{request.getId() + ""}, 
                "El id del path no corresponde al de la info mandada en el body"));
        }
        envioController.actualizar(pedidoId, envioId, request);
    }
    
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Page<Envio[]> consultar(@PathVariable final Long pedidoId,
            @Valid @ModelAttribute final PaginationDataLong pagination, final BindingResult resultPagination,
            final HttpServletRequest httpRequest) {
        if (resultPagination.hasErrors()) {
            throw new NotValidException(resultPagination);
        }
        Page<Envio[]> r = PaginationModelFactory.getPage(
                envioController.consultar(pedidoId, pagination),
                "Envio",
                httpRequest.getRequestURI(),
                null,
                pagination);
        return r;
    }
    
    @RequestMapping(produces = "application/json;charset=UTF-8", value="/{envioId}", method = RequestMethod.GET)
    public Envio get(@PathVariable final Long pedidoId, @PathVariable final Long envioId) {
        return envioController.get(pedidoId, envioId);
    }
    
    @RequestMapping(value="/{envioId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void borrar(@PathVariable final Long pedidoId, @PathVariable final Long envioId) {
        envioController.borrar(pedidoId, envioId);
    }

    public void setEnvioController(EnvioController envioController) {
        this.envioController = envioController;
    }
}
