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
package technology.tikal.gae.system.security.dao;

import org.springframework.security.core.userdetails.UserDetailsService;

import technology.tikal.gae.dao.template.EntityDAO;
import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.gae.system.security.model.SystemUser;

/**
 * 
 * @author Nekorp
 *
 */
public interface SystemUserDao extends UserDetailsService, EntityDAO<SystemUser, String, FiltroBusqueda, PaginationData<String>> {

    
}
