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
package technology.tikal.accounts.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import technology.tikal.accounts.dao.filter.AccountFilter;
import technology.tikal.accounts.model.Account;
import technology.tikal.accounts.model.AccountUpdateData;
import technology.tikal.accounts.otp.model.OtpConfig;
import technology.tikal.accounts.otp.model.OtpSyncData;
import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationDataString;
/**
 * 
 * @author Nekorp
 *
 */
public interface Accounts {

    void createAccount(Account account, BindingResult result, HttpServletRequest request, HttpServletResponse response);
    void updateAccount(String user, AccountUpdateData request, BindingResult result, HttpServletResponse response);
    void deleteAccount(String user, HttpServletResponse response);
    Page<List<Account>> queryAccounts(AccountFilter filter, BindingResult filterResult, 
            PaginationDataString pagination, BindingResult paginationResult, 
            HttpServletRequest request);
    Account getAccount(String user);
    OtpSyncData registerOTP(String user, HttpServletResponse response);
    void updateOTPConfig(String user, OtpConfig config, HttpServletResponse response);
}
