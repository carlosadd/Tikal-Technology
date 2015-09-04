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

import technology.tikal.accounts.controller.AccountsController;
import technology.tikal.accounts.dao.filter.AccountFilter;
import technology.tikal.accounts.model.Account;
import technology.tikal.accounts.model.PersonalInfo;
import technology.tikal.accounts.model.Role;
import technology.tikal.accounts.model.Status;
import technology.tikal.accounts.otp.model.OtpStatus;
import technology.tikal.accounts.otp.model.OtpStatusInfo;
import technology.tikal.accounts.service.Accounts;
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
@RequestMapping("/admin/accounts")
public class AccountsImp extends RestControllerTemplate implements Accounts {

    private AccountsController accountsController;
    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@Valid @RequestBody final Account account, final BindingResult result, final HttpServletRequest request,
            final HttpServletResponse response) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        accountsController.createAccount(account);
        response.setHeader("Location", request.getRequestURI() + "/" + account.getUser());
    }
    
    @Override
    @RequestMapping(value="/{user}/personalInfo", method = RequestMethod.POST)
    public void updateAccountPersonalInfo(@PathVariable final String user, @Valid @RequestBody final PersonalInfo request,
            final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        accountsController.updateAccount(user, request);
    }
    
    @Override
    @RequestMapping(value="/{user}/role", method = RequestMethod.POST)
    public void updateAccountRole(@PathVariable final String user, @Valid @RequestBody final Role request,
            final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        accountsController.updateAccount(user, request);
    }

    @Override
    @RequestMapping(value="/{user}/status", method = RequestMethod.POST)
    public void updateAccountStatus(@PathVariable final String user, @Valid @RequestBody final Status request,
            final BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        accountsController.updateAccount(user, request);
    }    

    
    @Override
    @RequestMapping(value="/{user}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAccount(@PathVariable final String user, final HttpServletResponse response) {
        accountsController.deleteAccount(user);
    }

    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Page<List<Account>> queryAccounts(@Valid @ModelAttribute final AccountFilter filter, final BindingResult filterResult,
        @Valid @ModelAttribute final PaginationDataString pagination, final BindingResult paginationResult, 
        final HttpServletRequest request) {
        if (filterResult.hasErrors()) {
            throw new NotValidException(filterResult);
        }
        if (paginationResult.hasErrors()) {
            throw new NotValidException(paginationResult);
        }
        Page<List<Account>> r = PaginationModelFactory.getPage(
                accountsController.queryAccounts(filter, pagination),
                "account",
                request.getRequestURI(),
                filter,
                pagination);
        return r;
    }

    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", value="/{user}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable final String user) {
        return accountsController.getAccount(user);
    }
    
    @Override
    @RequestMapping(value="/{user}/otp/status", method = RequestMethod.POST)
    public void updateOTPStatus(@PathVariable final String user, @RequestBody OtpStatus status) {
        accountsController.updateOTPStatus(user, status);
    }

    @Override
    @RequestMapping(value="/{user}/otp/status", method = RequestMethod.GET)
    public OtpStatusInfo getOtpStatusInfo(String user) {
        return accountsController.getOtpStatusInfo(user);
    }
    
    /*@Override
    @RequestMapping(value="/{user}/otp", method = RequestMethod.POST)
    public void updateOTPConfig(@PathVariable final String user, @RequestBody final OtpConfig config,
            final HttpServletResponse response) {
        accountsController.updateOTPConfig(user, config);
    }

    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", value="/{user}/otp", method = RequestMethod.GET)
    public OtpSyncData registerOTP(@PathVariable String user, HttpServletResponse response) {
        OtpSyncData otpSyncData = accountsController.registerOTP(user); 
        return otpSyncData;
    }*/
    
    public void setAccountsController(final AccountsController accountsController) {
        this.accountsController = accountsController;
    }
}
