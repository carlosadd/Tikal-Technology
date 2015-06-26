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
import technology.tikal.accounts.dao.rest.SessionDaoRestConfig;
import technology.tikal.accounts.dao.rest.SessionDaoRestConfigEntry;
import technology.tikal.accounts.model.SessionInfoFactory;
import technology.tikal.accounts.model.authentication.AuthenticationRequest;
import technology.tikal.accounts.model.authentication.AuthenticationResponse;
import technology.tikal.accounts.model.session.SessionInfo;
import technology.tikal.gae.security.model.InternalGrantedAuthority;
import technology.tikal.gae.system.security.model.UserSession;

/**
 * 
 * @author Nekorp
 *
 */
public class SessionControllerImp implements SessionController {
    
    private SecureRandom random;
    private String auth = "ROLE_USER";
    private String configId;
    private ConfigDao<SessionDaoRestConfig> configDao;
    
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
    public SessionInfo createSession(AuthenticationRequest request, AuthenticationResponse auth) {
        //calculo de token
        String token = new BigInteger(130, random).toString(32);
        //se crea la session con el usuario y el token
        UserSession session = new UserSession(request.getUser(), token);
        //se coloca el metodo de autenticacion
        session.setAuthenticationMethod(auth.getAuthenticationMethod());
        //TODO tener un mecanismo para configurar los permisos de cada usuario por sistema
        InternalGrantedAuthority authority = new InternalGrantedAuthority(this.auth);
        List<InternalGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        //se le colocan los permisos
        session.setAuthorities(authorities);
        //se envia a cada uno de los sistemas configurados.
        for (SessionDao x: loadConfig()) {
            x.guardar(session);
        }
        return SessionInfoFactory.build(session);
    }
    
    @Override
    public void setRemoteSessionConfig(SessionDaoRestConfig config) {
        config.setId(configId);
        this.configDao.guardar(config);
    }
    
    public void setConfigDao(ConfigDao<SessionDaoRestConfig> configDao) {
        this.configDao = configDao;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }    
}
