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
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.customers.controller.CustomersController;
import technology.tikal.customers.dao.filter.CustomerFilterSmall;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.pagination.PaginationModelFactory;
import technology.tikal.gae.pagination.model.PaginationDataDualLongString;
import technology.tikal.gae.service.template.RestControllerTemplate;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
public class WarmUpService extends RestControllerTemplate {

    private CustomersController customersController;
    private final Log logger = LogFactory.getLog(getClass());
    
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public void warmup(@Valid @ModelAttribute final CustomerFilterSmall filter, final BindingResult resultFilter,
            @Valid @ModelAttribute final PaginationDataDualLongString pagination, final BindingResult resultPagination,
            final HttpServletRequest request) {
        logger.info("Inicio de WarmUp");
        if (resultFilter.hasErrors()) {
            throw new NotValidException(resultFilter);
        }
        if (resultPagination.hasErrors()) {
            throw new NotValidException(resultPagination);
        }
        pagination.setMaxResults(1);
        PaginationModelFactory.getPage(
            customersController.queryCustomers(filter, pagination),
            "Customer",
            request.getRequestURI(),
            filter,
            pagination);
        logger.info("WarmUp completado");
    }

    public void setCustomersController(CustomersController customersController) {
        this.customersController = customersController;
    }
}
