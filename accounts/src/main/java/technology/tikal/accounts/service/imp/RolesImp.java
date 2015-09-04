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
package technology.tikal.accounts.service.imp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.accounts.controller.SessionController;
import technology.tikal.accounts.model.Role;
import technology.tikal.accounts.service.Roles;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.pagination.PaginationModelFactory;
import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationDataString;
import technology.tikal.gae.service.template.RestControllerTemplate;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
@RequestMapping("/admin/roles")
public class RolesImp extends RestControllerTemplate implements Roles {

    private SessionController sessionController;
    
    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Page<List<Role>> queryRoles(@Valid @ModelAttribute PaginationDataString pagination, BindingResult paginationResult, HttpServletRequest request) {
        if (paginationResult.hasErrors()) {
            throw new NotValidException(paginationResult);
        }
        Page<List<Role>> r = PaginationModelFactory.getPage(
                sessionController.queryRoles(pagination),
                "role",
                request.getRequestURI(),
                null,
                pagination);
        return r;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
}
