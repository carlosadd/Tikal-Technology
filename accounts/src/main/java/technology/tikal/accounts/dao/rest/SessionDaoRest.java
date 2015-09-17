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
package technology.tikal.accounts.dao.rest;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.fasterxml.jackson.databind.ObjectMapper;

import technology.tikal.accounts.dao.SessionDao;
import technology.tikal.accounts.model.config.SessionDaoRestConfigEntry;
import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.gae.system.security.model.UserSession;

/**
 * 
 * @author Nekorp
 *
 */
public class SessionDaoRest implements SessionDao {
    
    private SessionDaoRestConfigEntry config;
    private ObjectMapper mapper;
    private int maxRetry = 3;

    public SessionDaoRest() {
        mapper = new ObjectMapper();
    }
    
    @Override
    public List<UserSession> consultarTodos(FiltroBusqueda filtro,
            PaginationData<String> pagination) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void guardar(UserSession objeto) {
        this.guardar(objeto, 1);
    }
    
    private void guardar(UserSession objeto, int intento) {
        //basic auth string
        String basicAuthString = config.getUser() + ":" + config.getPassword(); 
        basicAuthString = new String(Base64.encodeBase64(basicAuthString.getBytes()));
        basicAuthString = "Basic " + basicAuthString;
        
        //mensaje remoto
        try {
            //config
            URL url = new URL(config.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(config.getMethod());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", basicAuthString);
            connection.setInstanceFollowRedirects(false);
            //write
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(mapper.writeValueAsString(objeto));
            //close
            writer.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                return;
            } else {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"SessionCreationRefused.SessionDaoRest.guardar"}, 
                    new String[]{connection.getResponseCode() + ""}, 
                    ""));
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            if (intento <= maxRetry) {
                this.guardar(objeto, intento + 1);
            } else {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"SessionCreationError.SessionDaoRest.guardar"}, 
                    new String[]{e.getMessage()}, 
                    ""));
            }
        }
    }

    @Override
    public UserSession consultar(String id, Class<?>... group) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void borrar(UserSession objeto) {
        throw new UnsupportedOperationException();
    }

    public void setConfig(SessionDaoRestConfigEntry config) {
        this.config = config;
    }    
}
