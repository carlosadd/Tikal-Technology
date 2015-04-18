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
package technology.tikal.customers.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.customers.controller.CustomersController;
import technology.tikal.customers.dao.filter.ContactRelationshipFilter;
import technology.tikal.customers.model.contact.ContactRelationship;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.pagination.PaginationModelFactory;
import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationDataDualLongString;
import technology.tikal.gae.service.template.RestControllerTemplate;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
@RequestMapping("/customer/{customerId}/contact/{contactId}/relationship")
public class ContactRelationshipService extends RestControllerTemplate {
    
    private CustomersController customersController;
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createRelation(@PathVariable final Long customerId, @PathVariable final Long contactId, 
            @Valid @RequestBody final ContactRelationship data, final BindingResult result,
            final HttpServletRequest request, final HttpServletResponse response) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        ContactRelationship created = customersController.createRelation(customerId, contactId, data);
        response.setHeader("Location", request.getRequestURI() + "/" + created.getRelationship());
    }
    
    @RequestMapping(value="/{relationId}", method = RequestMethod.POST)
    public void updateRelation(@PathVariable final Long customerId, @PathVariable final Long contactId, @PathVariable final Long relationId, 
            @Valid @RequestBody final ContactRelationship data, final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        customersController.updateRelation(customerId, contactId, relationId, data);
    }
    
    @RequestMapping(value="/{relationId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteRelation(@PathVariable final Long customerId, @PathVariable final Long contactId, @PathVariable final Long relationId) {
        customersController.deleteRelation(customerId, contactId, relationId);
    }
    
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Page<List<ContactRelationship>> queryRelations(@PathVariable final Long customerId, @PathVariable final Long contactId,
            @Valid @ModelAttribute final ContactRelationshipFilter filter, final BindingResult resultFilter,
            @Valid @ModelAttribute final PaginationDataDualLongString pagination, final BindingResult resultPagination,
            final HttpServletRequest request) {
        if (resultFilter.hasErrors()) {
            throw new NotValidException(resultFilter);
        }
        if (resultPagination.hasErrors()) {
            throw new NotValidException(resultPagination);
        }
        Page<List<ContactRelationship>> r = PaginationModelFactory.getPage(
                customersController.queryRelation(customerId, contactId, filter, pagination),
                "ContactRelationship",
                request.getRequestURI(),
                filter,
                pagination);
        return r;
    }
    
    @RequestMapping(produces = "application/json;charset=UTF-8", value="/{relationId}", method = RequestMethod.GET)
    public ContactRelationship getRelation(@PathVariable final Long customerId, @PathVariable final Long contactId, @PathVariable final Long relationId) {
        return customersController.getRelation(customerId, contactId, relationId);
    }

    public void setCustomersController(CustomersController customersController) {
        this.customersController = customersController;
    }
}
