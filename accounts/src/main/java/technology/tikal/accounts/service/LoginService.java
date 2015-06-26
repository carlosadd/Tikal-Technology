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

import org.springframework.validation.BindingResult;

import technology.tikal.accounts.model.authentication.AuthenticationRequest;
import technology.tikal.accounts.model.session.SessionInfo;

/**
 * 
 * @author Nekorp
 *
 */
public interface LoginService {

    SessionInfo login(AuthenticationRequest request, BindingResult result);
}
