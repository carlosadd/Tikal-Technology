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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.NotFoundException;

import technology.tikal.accounts.controller.SessionController;
import technology.tikal.accounts.dao.ConfigDao;
import technology.tikal.accounts.dao.SessionDao;
import technology.tikal.accounts.dao.rest.SessionDaoRest;
import technology.tikal.accounts.model.InternalAccount;
import technology.tikal.accounts.model.Role;
import technology.tikal.accounts.model.SessionInfoFactory;
import technology.tikal.accounts.model.authentication.AuthenticationRequest;
import technology.tikal.accounts.model.authentication.AuthenticationResponse;
import technology.tikal.accounts.model.config.RoleAuthorityMapEntry;
import technology.tikal.accounts.model.config.SessionDaoRestConfig;
import technology.tikal.accounts.model.config.SessionDaoRestConfigEntry;
import technology.tikal.accounts.model.session.SessionInfo;
import technology.tikal.gae.pagination.model.PaginationDataString;
import technology.tikal.gae.security.model.InternalGrantedAuthority;
import technology.tikal.gae.system.security.dao.UserSessionDao;
import technology.tikal.gae.system.security.model.UserSession;

/**
 * 
 * @author Nekorp
 *
 */
public class SessionControllerImp implements SessionController {
    
    private SecureRandom random;
    private String configId;
    private ConfigDao<SessionDaoRestConfig> configDao;
    private ConfigDao<RoleAuthorityMapEntry> authorityConfigDao;
    private UserSessionDao userSessionDao;
    
    public SessionControllerImp() {
        this.random = new SecureRandom();
    }
    
    public List<SessionDao> loadConfig() {
        List<SessionDao> sessionDao = new ArrayList<>();
        try {
            SessionDaoRestConfig config = this.configDao.consultar(configId);
            for (SessionDaoRestConfigEntry x: config.getConfigInfo()) {
                SessionDaoRest dao = new SessionDaoRest();
                dao.setConfig(x);
                sessionDao.add(dao);
            }
        } catch (NotFoundException e) {
            //no hay configuracion en la db
        }
        return sessionDao;
    }

    @Override
    public SessionInfo createSession(InternalAccount account, AuthenticationRequest request, AuthenticationResponse auth) {
        //calculo de token
        String token = new BigInteger(130, random).toString(32);
        //se crea la session con el usuario y el token
        UserSession session = new UserSession(account.getUser(), token);
        //se coloca el metodo de autenticacion
        session.setAuthenticationMethod(auth.getAuthenticationMethod());
        //TODO tener un mecanismo para configurar los permisos de cada usuario por sistema
        List<InternalGrantedAuthority> authorities = new ArrayList<>();
        for (String x: getAuthorityByRole(account.getRole())) {
            InternalGrantedAuthority authority = new InternalGrantedAuthority(x);
            authorities.add(authority);
        }
        //se le colocan los permisos
        session.setAuthorities(authorities);
        //se envia a cada uno de los sistemas configurados.
        for (SessionDao x: loadConfig()) {
            x.guardar(session);
        }
        //se guarda una copia local
        userSessionDao.guardar(session);
        return SessionInfoFactory.build(account, session);
    }
    
    private List<String> getAuthorityByRole(Role role) {
        try {
            RoleAuthorityMapEntry config = authorityConfigDao.consultar(role.getValue());
            return config.getAuthority();
        } catch(NotFoundException e) {
            return new ArrayList<String>();
        }
    }
    
    @Override
    public void setRemoteSessionConfig(SessionDaoRestConfig config) {
        config.setId(configId);
        this.configDao.guardar(config);
    }
    
    @Override
    public void setAuthorityConfig(RoleAuthorityMapEntry config) {
        authorityConfigDao.guardar(config);
    }
    
    @Override
    public List<Role> queryRoles(PaginationDataString pagination) {
        List<RoleAuthorityMapEntry> datos = authorityConfigDao.consultarTodos(null, pagination);
        List<Role> response = new ArrayList<>();
        for (RoleAuthorityMapEntry x: datos) {
            Role nuevo = new Role();
            nuevo.setValue(x.getRole());
            response.add(nuevo);
        }
        return response;
    }
    
    public void setConfigDao(ConfigDao<SessionDaoRestConfig> configDao) {
        this.configDao = configDao;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public void setUserSessionDao(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }

    public void setAuthorityConfigDao(
            ConfigDao<RoleAuthorityMapEntry> authorityConfigDao) {
        this.authorityConfigDao = authorityConfigDao;
    }
}
