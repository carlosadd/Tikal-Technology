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
package technology.tikal.accounts.model;

import technology.tikal.accounts.model.session.SessionInfo;
import technology.tikal.gae.system.security.model.UserSession;
/**
 * 
 * @author Nekorp
 *
 */
public class SessionInfoFactory {

    public static SessionInfo build(InternalAccount account, UserSession session) {
        SessionInfo result = new SessionInfo();
        result.setUser(session.getUsername());
        result.setToken(session.getToken());
        result.setName(account.getPersonalInfo().getName());
        result.setRole(account.getRole().getValue());
        return result;
    }
}
