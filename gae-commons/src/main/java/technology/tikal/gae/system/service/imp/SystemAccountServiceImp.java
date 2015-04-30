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
package technology.tikal.gae.system.service.imp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.googlecode.objectify.NotFoundException;

import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.pagination.PaginationModelFactory;
import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationDataString;
import technology.tikal.gae.service.template.RestControllerTemplate;
import technology.tikal.gae.system.security.dao.SystemUserDao;
import technology.tikal.gae.system.security.model.SystemUser;
import technology.tikal.gae.system.service.SystemAccountService;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
@RequestMapping("/accounts")
public class SystemAccountServiceImp extends RestControllerTemplate implements SystemAccountService {

    private SystemUserDao systemUserDao;
    
    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSystemAccount(@Valid @RequestBody final SystemUser account, final BindingResult result, 
            final HttpServletRequest request, final HttpServletResponse response) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        if (account.getUsername() == null) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotNull.systemUser.username"}, 
                    new String[]{}, 
                    "Null username"));
        }
        if (account.getPassword() == null) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotNull.systemUser.password"}, 
                    new String[]{}, 
                    "Null password User"));
        }
        try {
            this.systemUserDao.consultar(account.getUsername());
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                        new String[]{"Duplicated.SystemAccountServiceImp.createSystemAccount"}, 
                        new String[]{account.getUsername()}, 
                        "Duplicated User"));
        } catch (NotFoundException e) {
            this.systemUserDao.guardar(account);
            response.setHeader("Location", request.getRequestURI() + "/" + account.getUsername());
        }
    }
    @Override
    @RequestMapping(value="/{user}", method = RequestMethod.POST)
    public void updateSystemAccount(@PathVariable String user, @Valid @RequestBody final SystemUser systemUser, final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        if (systemUser.getUsername() != null && user.compareTo(systemUser.getUsername()) != 0) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotSame.SystemAccountServiceImp.updateSystemAccount"}, 
                    new String[]{systemUser.getUsername(), user},
                    "Body username dont correspond to path username"));
        } else {
            SystemUser account = systemUserDao.consultar(user);
            account.update(systemUser);
            systemUserDao.guardar(account);
        }
    }
    @Override
    @RequestMapping(value="/{user}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteSystemAccount(@PathVariable final String user) {
        SystemUser account = systemUserDao.consultar(user);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username.compareTo(user) == 0) { //No se puede borrar a si mismo
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"SelfDelete.SystemAccountServiceImp.deleteSystemAccount"}, 
                    new String[]{user},
                    "Can't delete yourself"));
        } else {
            systemUserDao.borrar(account);
        }
    }
    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Page<List<SystemUser>> queryAccounts(@Valid @ModelAttribute final PaginationDataString pagination, final BindingResult paginationResult,
            final HttpServletRequest request) {
        if (paginationResult.hasErrors()) {
            throw new NotValidException(paginationResult);
        }
        List<SystemUser> result = systemUserDao.consultarTodos(null, pagination);
        for(SystemUser x : result) {
            x.setPassword(null);
        }
        Page<List<SystemUser>> r = PaginationModelFactory.getPage(
                result, "SystemUser", request.getRequestURI(), null, pagination);
        return r;
    }
    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", value="/{user}", method = RequestMethod.GET)
    public SystemUser getSystemAccount(@PathVariable final String user) {
        SystemUser respuesta = systemUserDao.consultar(user);
        respuesta.setPassword(null);
        return respuesta;
    }
    public void setSystemUserDao(SystemUserDao systemUserDao) {
        this.systemUserDao = systemUserDao;
    }
}
