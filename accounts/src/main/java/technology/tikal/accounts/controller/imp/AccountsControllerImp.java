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
package technology.tikal.accounts.controller.imp;

import java.util.LinkedList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.googlecode.objectify.NotFoundException;

import technology.tikal.accounts.controller.AccountsController;
import technology.tikal.accounts.controller.OtpController;
import technology.tikal.accounts.controller.SessionController;
import technology.tikal.accounts.dao.AccountDao;
import technology.tikal.accounts.dao.filter.AccountFilter;
import technology.tikal.accounts.model.Account;
import technology.tikal.accounts.model.Password;
import technology.tikal.accounts.model.InternalAccount;
import technology.tikal.accounts.model.OtpStatusInfoFactory;
import technology.tikal.accounts.model.PersonalInfo;
import technology.tikal.accounts.model.Role;
import technology.tikal.accounts.model.Status;
import technology.tikal.accounts.model.authentication.AuthenticationRequest;
import technology.tikal.accounts.model.authentication.AuthenticationResponse;
import technology.tikal.accounts.model.session.SessionInfo;
import technology.tikal.accounts.otp.model.OtpStatus;
import technology.tikal.accounts.otp.model.OtpStatusInfo;
import technology.tikal.accounts.otp.model.OtpSyncData;
import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataString;

/**
 * 
 * @author Nekorp
 *
 */
public class AccountsControllerImp implements AccountsController {

    private AccountDao accountDao;
    private OtpController otpController;
    private SessionController sessionController;
    
    @Override
    public void createAccount(final Account account) {
        InternalAccount cuenta = new InternalAccount(account);
        try {
            this.accountDao.consultar(cuenta.getIdUser());
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"DuplicatedAccount.AccountsControllerImp.createAccount"}, 
                    new String[]{cuenta.getIdUser()}, 
                    "Duplicated Account"));
        } catch (NotFoundException e) {
            this.accountDao.guardar(cuenta);
        }
    }
    
    @Override
    public void updateAccount(String user, PersonalInfo request) {
        InternalAccount account = accountDao.consultar(user);
        account.setPersonalInfo(request);
        accountDao.guardar(account);
    }

    @Override
    public void updateAccount(final String user, final Password request) {
        InternalAccount account = accountDao.consultar(user);
        account.setPassword(request);
        accountDao.guardar(account);
    }
    
    @Override
    public void updateAccount(String user, Role request) {
        InternalAccount account = accountDao.consultar(user);
        account.setRole(request);
        accountDao.guardar(account);
    }

    @Override
    public void updateAccount(String user, Status request) {
        InternalAccount account = accountDao.consultar(user);
        account.setStatus(request);
        accountDao.guardar(account);
    }    

    @Override
    public void deleteAccount(final String user) {
        InternalAccount account = accountDao.consultar(user);
        accountDao.borrar(account);
    }

    @Override
    public List<Account> queryAccounts(final AccountFilter filter, final PaginationDataString pagination) {
        List<InternalAccount> result = accountDao.consultarTodos(filter, pagination);
        List<Account> dataAsAccount = new LinkedList<>();
        for (InternalAccount x: result) {
            x.setPassword(null);
            dataAsAccount.add(x);
        }
        return dataAsAccount;
    }
    
    @Override
    public Account getAccount(final String user) {
        Account respuesta = accountDao.consultar(user);
        respuesta.setPassword(null);
        return respuesta;
    }
    
    @Override
    public OtpSyncData registerOTP(final String user) {
        InternalAccount account = accountDao.consultar(user);
        if (account.getOtpInfo().hasOtp()) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"ExistingOtp.AccountsControllerImp.registerOTP"}, 
                    new String[]{}, 
                    "Can't register another otp"));
        } else {
            OtpSyncData syncData = otpController.buildOtp(account);
            accountDao.guardar(account);
            return syncData;
        }
    }
    
    @Override
    public OtpStatusInfo getOtpStatusInfo(String user) {
        InternalAccount account = accountDao.consultar(user);
        return OtpStatusInfoFactory.getOtpStatusInfo(account);
    }
    
    @Override
    public void deleteOTP(final String user) {
        InternalAccount account = accountDao.consultar(user);
        account.getOtpInfo().clear();
        accountDao.guardar(account);
    }

    @Override
    public void updateOTPStatus(final String user, final OtpStatus status) {
        InternalAccount account = accountDao.consultar(user);
        if (account.getOtpInfo().hasOtp()) {
            account.getOtpInfo().setEnabled(status.isEnabled());
            accountDao.guardar(account);
        } else {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"OtpNotFound.AccountsControllerImp.registerOTP"}, 
                    new String[]{}, 
                    "The account don't have an OTP"));
        }
    }
    
    @Override
    public AuthenticationResponse authenticateUser(final AuthenticationRequest request) {
        try {
            InternalAccount account = accountDao.consultar(request.getUser());
            return this.authenticateUser(request, account);
        } catch (NotFoundException e) {
            AuthenticationResponse response = new AuthenticationResponse();
            response.setAuthenticated(false);
            response.setAuthenticationMethod(null);
            return response;
        }
    }
    
    private AuthenticationResponse authenticateUser(final AuthenticationRequest request,  InternalAccount account) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAuthenticated(false);
        response.setAuthenticationMethod(null);
        if (account.getPassword().getValue().compareTo(request.getPassword()) == 0) {
            if (account.getOtpInfo().hasOtp() && account.getOtpInfo().isEnabled()) {
                if (this.otpController.isOtpValid(account, request.getOtp())) {
                    accountDao.guardar(account);//se guarda el token usado
                    response.setAuthenticated(true);//es valido con otp y password
                    response.setAuthenticationMethod(AuthenticationResponse.OTP_AUTHENTICATION_METHOD);
                }
            } else {
                response.setAuthenticated(true);//es valido sin otp, solo password
                response.setAuthenticationMethod(AuthenticationResponse.PASS_AUTHENTICATION_METHOD);
            }
        }
        return response;
    }
    
    @Override
    public SessionInfo createSession(AuthenticationRequest request) {
        try {
            InternalAccount account = accountDao.consultar(request.getUser());
            if (account.getStatus().isBlocked()) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"BlockedAccount.AccountsControllerImp.createSession"}, 
                    new String[]{},
                    "Account Blocked"));
            }
            AuthenticationResponse auth = authenticateUser(request, account);
            if (auth.isAuthenticated()) {
                return sessionController.createSession(account, request, auth);
            } else {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"BadCredentials.AccountsControllerImp.createSession"}, 
                    new String[]{},
                    "Not valid credentials"));
            }
        } catch (NotFoundException e) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"BadCredentials.AccountsControllerImp.createSession"}, 
                    new String[]{},
                    "Not valid credentials"));
        }
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setOtpController(OtpController otpController) {
        this.otpController = otpController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
}
