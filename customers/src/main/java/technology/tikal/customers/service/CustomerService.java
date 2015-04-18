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
import technology.tikal.customers.dao.filter.CustomerFilterSmall;
import technology.tikal.customers.model.Customer;
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
@RequestMapping("/customer")
public class CustomerService extends RestControllerTemplate {
    
    private CustomersController customersController;
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomer(@Valid @RequestBody final Customer data, final BindingResult result, 
            final HttpServletRequest request, final HttpServletResponse response) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        Customer created;
        if (data.getId() != null) {
            //Si viene un id en los datos se intentara crear el customer con ese id
            //esto puede fallar por que el id ya exista o por que no se cuente con los permisos para hacerlo
            created = customersController.createCustomerWithForcedId(data.getId(), data);
        } else {
            created = customersController.createCustomer(data);
        }
        response.setHeader("Location", request.getRequestURI() + "/" + created.getId());
    }
    
    @RequestMapping(value="/{customerId}", method = RequestMethod.POST)
    public void updateCustomer(@PathVariable final Long customerId, 
            @Valid @RequestBody final Customer data, final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        customersController.updateCustomer(customerId, data);
    }
    
    @RequestMapping(value="/{customerId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCustomer(@PathVariable final Long customerId) {
        customersController.deactivateCustomer(customerId);
    }
    
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Page<Customer[]> queryCustomers(
            @Valid @ModelAttribute final CustomerFilterSmall filter, final BindingResult resultFilter,
            @Valid @ModelAttribute final PaginationDataDualLongString pagination, final BindingResult resultPagination,
            final HttpServletRequest request) {
        if (resultFilter.hasErrors()) {
            throw new NotValidException(resultFilter);
        }
        if (resultPagination.hasErrors()) {
            throw new NotValidException(resultPagination);
        }
        Page<Customer[]> r = PaginationModelFactory.getPage(
                customersController.queryCustomers(filter, pagination),
                "Customer",
                request.getRequestURI(),
                filter,
                pagination);
        return r;
    }
    
    @RequestMapping(produces = "application/json;charset=UTF-8", value="/{customerId}", method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable final Long customerId) {
        return customersController.getCustomer(customerId);
    }

    public void setCustomersController(CustomersController customersController) {
        this.customersController = customersController;
    }
}
