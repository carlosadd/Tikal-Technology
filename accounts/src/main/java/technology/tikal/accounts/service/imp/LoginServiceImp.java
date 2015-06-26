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

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.accounts.controller.AccountsController;
import technology.tikal.accounts.model.authentication.AuthenticationRequest;
import technology.tikal.accounts.model.session.SessionInfo;
import technology.tikal.accounts.service.LoginService;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.service.template.RestControllerTemplate;

@RestController
@RequestMapping("/login")
/**
 * @author Nekorp
 *
 */
public class LoginServiceImp extends RestControllerTemplate implements LoginService {

    private AccountsController accountsController;
    
    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public SessionInfo login(@Valid @RequestBody AuthenticationRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        return accountsController.createSession(request);
    }

    public void setAccountsController(AccountsController accountsController) {
        this.accountsController = accountsController;
    }    
}
