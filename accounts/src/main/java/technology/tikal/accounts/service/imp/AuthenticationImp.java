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

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.accounts.controller.AccountsController;
import technology.tikal.accounts.model.authentication.AuthenticationRequest;
import technology.tikal.accounts.model.authentication.AuthenticationResponse;
import technology.tikal.accounts.service.Authentication;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.service.template.RestControllerTemplate;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
@RequestMapping("/authenticate")
public class AuthenticationImp extends RestControllerTemplate implements Authentication {

    private AccountsController accountsController;
    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public AuthenticationResponse authenticate(@Valid @RequestBody final AuthenticationRequest request, final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        AuthenticationResponse authResp = accountsController.authenticateUser(request);
        return authResp;
    }
    public void setAccountsController(AccountsController accountsController) {
        this.accountsController = accountsController;
    }
}
