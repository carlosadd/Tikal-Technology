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

import technology.tikal.accounts.controller.SessionController;
import technology.tikal.accounts.dao.rest.SessionDaoRestConfig;
import technology.tikal.accounts.service.AccountsConfig;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.service.template.RestControllerTemplate;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
@RequestMapping("/config/accounts")
public class AccountsConfigImp extends RestControllerTemplate implements AccountsConfig {

    private SessionController sessionController;
    @Override
    @RequestMapping(value="/session/rest", method = RequestMethod.POST)
    public void setSessionRestConfig(@Valid @RequestBody SessionDaoRestConfig config, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        sessionController.setRemoteSessionConfig(config);
    }
    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
}
