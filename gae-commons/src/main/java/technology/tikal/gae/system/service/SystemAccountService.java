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
package technology.tikal.gae.system.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationDataString;
import technology.tikal.gae.system.security.model.SystemUser;
/**
 * 
 * @author Nekorp
 *
 */
public interface SystemAccountService {

    void createSystemAccount(SystemUser account, BindingResult result, HttpServletRequest request, HttpServletResponse response);
    void updateSystemAccount(String user, SystemUser systemUser, BindingResult result);
    void deleteSystemAccount(String user);
    Page<List<SystemUser>> queryAccounts(PaginationDataString pagination, BindingResult paginationResult,  HttpServletRequest request);
    SystemUser getSystemAccount(String user);
}
