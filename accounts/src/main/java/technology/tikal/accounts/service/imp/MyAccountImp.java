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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.accounts.controller.AccountsController;
import technology.tikal.accounts.model.Account;
import technology.tikal.accounts.model.Password;
import technology.tikal.accounts.model.PersonalInfo;
import technology.tikal.accounts.otp.model.OtpStatus;
import technology.tikal.accounts.otp.model.OtpStatusInfo;
import technology.tikal.accounts.otp.model.OtpSyncData;
import technology.tikal.accounts.service.MyAccount;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.service.template.RestControllerTemplate;

@RestController
@RequestMapping("/my/account")
/**
 * de momento esta idea esta congelada.
 * @author Nekorp
 */
public class MyAccountImp extends RestControllerTemplate implements MyAccount {
    
    private AccountsController accountsController;
    
    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Account getAccount() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountsController.getAccount(user);
    }
    
    @Override
    @RequestMapping(value="/password", method = RequestMethod.POST)
    public void updateAccountPassword(@Valid @RequestBody final Password request, final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        accountsController.updateAccount(user, request);
    }
    
    @Override
    @RequestMapping(value="/personalInfo", method = RequestMethod.POST)
    public void updateAccountPersonalInfo(@Valid @RequestBody final PersonalInfo request, final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        accountsController.updateAccount(user, request);
    }

    @Override
    @RequestMapping(value="/otp/status", method = RequestMethod.POST)
    public void updateOTPStatus(@RequestBody OtpStatus status) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        accountsController.updateOTPStatus(user, status);
    }

    @Override
    @RequestMapping(value="/otp/status", method = RequestMethod.GET)
    public OtpStatusInfo getOtpStatusInfo() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountsController.getOtpStatusInfo(user);
    }
    
    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", value="/otp", method = RequestMethod.GET)
    public OtpSyncData registerOTP() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        OtpSyncData otpSyncData = accountsController.registerOTP(user);
        return otpSyncData;
    }
    
    @Override
    @RequestMapping(value="/otp", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteOTP() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        accountsController.deleteOTP(user);
    }

    public void setAccountsController(AccountsController accountsController) {
        this.accountsController = accountsController;
    }
}
