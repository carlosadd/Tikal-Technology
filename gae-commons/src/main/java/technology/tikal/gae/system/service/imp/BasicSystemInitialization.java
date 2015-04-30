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
package technology.tikal.gae.system.service.imp;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.gae.security.model.InternalGrantedAuthority;
import technology.tikal.gae.service.template.RestControllerTemplate;
import technology.tikal.gae.system.security.dao.SystemUserDao;
import technology.tikal.gae.system.security.model.SystemUser;
import technology.tikal.gae.system.service.SystemInitialization;

/**
 * 
 * @author Nekorp
 *
 */
@RestController
@RequestMapping("/init")
public class BasicSystemInitialization extends RestControllerTemplate implements SystemInitialization, InitializingBean {
    private static final String DEFAULT_USER = "usuario1";
    private static final String DEFAULT_PASS = "usuario1";
    private static final String DEFAULT_ROLE = "ROLE_SYSTEM";
    private final Log logger = LogFactory.getLog(getClass());
    
    private SystemUserDao systemUserDao;
    
    @Override
    @RequestMapping(method = RequestMethod.GET)
    public void initSystem(HttpServletResponse response) {
        SystemUser user = new SystemUser(DEFAULT_USER);
        user.setPassword(DEFAULT_PASS);
        user.getAuthorities().add(new InternalGrantedAuthority(DEFAULT_ROLE));
        user.setActivo(true);
        systemUserDao.guardar(user);
    }

    public void setSystemUserDao(SystemUserDao systemUserDao) {
        this.systemUserDao = systemUserDao;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.logger.isWarnEnabled()) {
            this.logger.warn("Initialization Service available, please remove this bean if you have completed the app setup!!!");
        }
    }
}
