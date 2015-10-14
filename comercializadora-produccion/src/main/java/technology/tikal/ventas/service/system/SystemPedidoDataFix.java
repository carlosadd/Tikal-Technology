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
package technology.tikal.ventas.service.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.pagination.PaginationModelFactory;
import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationDataLong;
import technology.tikal.gae.service.template.RestControllerTemplate;
import technology.tikal.ventas.dao.pedido.PartidaDao;
import technology.tikal.ventas.dao.pedido.PedidoDao;
import technology.tikal.ventas.model.pedido.ofy.PartidaOfy;
import technology.tikal.ventas.model.pedido.ofy.PedidoOfy;
import technology.tikal.ventas.model.pedido.ofy.intermediario.PartidaIntermediario;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
@RequestMapping("/pedido/{idOrigen}")
public class SystemPedidoDataFix extends RestControllerTemplate{

    private PedidoDao pedidoDao;
    private PartidaDao partidaDao;
    
    @RequestMapping(produces = "application/json;charset=UTF-8", value="/move/all/partida/{idDestino}", method = RequestMethod.GET)
    public Page<List<PartidaOfy>> moveAllPartida(@PathVariable final Long idOrigen, @PathVariable final Long idDestino,
            @Valid @ModelAttribute final PaginationDataLong pagination, final BindingResult resultPagination,
            final HttpServletRequest httpRequest) {
        if (resultPagination.hasErrors()) {
            throw new NotValidException(resultPagination);
        }
        PedidoOfy pedidoOrigen = pedidoDao.consultar(idOrigen);
        PedidoOfy pedidoDestino = pedidoDao.consultar(idDestino);
        List<PartidaOfy> partidas = partidaDao.consultarTodos(pedidoOrigen, null, pagination);
        for (PartidaOfy x: partidas) {
            if (x instanceof PartidaIntermediario) {
                PartidaIntermediario copia = new PartidaIntermediario(pedidoDestino);
                copia.update(x);
                partidaDao.guardar(pedidoDestino, copia);
                partidaDao.borrar(pedidoOrigen, x);
            }
        }
        Page<List<PartidaOfy>> r = PaginationModelFactory.getPage(
                partidas,
                "Partidas",
                httpRequest.getRequestURI(),
                null,
                pagination);
        return r;
    }
    public void setPartidaDao(PartidaDao partidaDao) {
        this.partidaDao = partidaDao;
    }
    public void setPedidoDao(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }
    
}
