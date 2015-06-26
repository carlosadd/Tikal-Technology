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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.googlecode.objectify.NotFoundException;

import technology.tikal.accounts.controller.AccountsController;
import technology.tikal.accounts.model.AccountUpdateData;
import technology.tikal.accounts.otp.model.OtpConfig;
import technology.tikal.accounts.otp.model.OtpSyncData;
import technology.tikal.accounts.service.MyAccount;

@RestController
@RequestMapping("/myAccount")
/**
 * de momento esta idea esta congelada.
 * @author Nekorp
 */
public class MyAccountImp implements MyAccount {
    
    private AccountsController accountsController;
    
    @Override
    @RequestMapping(method = RequestMethod.POST)
    public void updateAccount(@Valid @RequestBody final AccountUpdateData request, final HttpServletResponse response) {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            accountsController.updateAccount(user, request);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    @RequestMapping(value="/otp", method = RequestMethod.GET)
    public OtpSyncData registerOTP(final HttpServletResponse response) {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            OtpSyncData otpSyncData = accountsController.registerOTP(user); 
            response.setHeader("Content-Type","application/json;charset=UTF-8");
            return otpSyncData;
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return null;
    }

    @Override
    @RequestMapping(value="/otp", method = RequestMethod.DELETE)
    public void deleteOTP(final HttpServletResponse response) {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            accountsController.deleteOTP(user);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    @RequestMapping(value="/otp", method = RequestMethod.POST)
    public void updateOTPConfig(@RequestBody final OtpConfig config, final HttpServletResponse response) {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            accountsController.updateOTPConfig(user, config);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    public void setAccountsController(AccountsController accountsController) {
        this.accountsController = accountsController;
    }
}
