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
package technology.tikal.accounts.controller;

import java.util.List;

import technology.tikal.accounts.dao.filter.AccountFilter;
import technology.tikal.accounts.model.Account;
import technology.tikal.accounts.model.Password;
import technology.tikal.accounts.model.PersonalInfo;
import technology.tikal.accounts.model.Role;
import technology.tikal.accounts.model.Status;
import technology.tikal.accounts.model.authentication.AuthenticationRequest;
import technology.tikal.accounts.model.authentication.AuthenticationResponse;
import technology.tikal.accounts.model.session.SessionInfo;
import technology.tikal.accounts.otp.model.OtpStatus;
import technology.tikal.accounts.otp.model.OtpStatusInfo;
import technology.tikal.accounts.otp.model.OtpSyncData;
import technology.tikal.gae.pagination.model.PaginationDataString;

/**
 * 
 * @author Nekorp
 *
 */
public interface AccountsController {

    void createAccount(Account account);
    void updateAccount(String user, PersonalInfo request);
    void updateAccount(String user, Password request);
    void updateAccount(String user, Role request);
    void updateAccount(String user, Status request);
    void deleteAccount(String user);
    List<Account> queryAccounts(AccountFilter filter, PaginationDataString pagination);
    Account getAccount(String user);
    OtpSyncData registerOTP(String user);
    void deleteOTP(String user);
    void updateOTPStatus(String user, OtpStatus status);
    AuthenticationResponse authenticateUser(AuthenticationRequest request);
    SessionInfo createSession(AuthenticationRequest request);
    OtpStatusInfo getOtpStatusInfo(String user);
}
