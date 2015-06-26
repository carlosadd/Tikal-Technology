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
import technology.tikal.accounts.model.authentication.AuthenticationResponse;

/**
 * 
 * @author Nekorp
 *
 */
public interface Authentication {

    /**
     * dilema*
     * opcion 1 regresa token de session, esta app tiene que ir a la app de authority/session/access a decir que inicie session
     * opcion 2 solo regresa un si o un no, esta app no podria ser usada por un cliente, tendria que ser usada por otra app
     * por que el cliente no seria confiable para pasar el mensaje de si o no.
     * -------
     * suena a que es mas segura la opcion 2 por que en la opcion 1 cualquiera le podria pedir al de session que inicie una session
     * y si para iniciar session pide las credenciales y a su vez estas se validan aqui no cualquiera puede solicitar un inicio de
     * session
     * 
     * Esto estaba pensado cuando estaba separado el authentication del authorization
     * @param request
     * @param response
     * @return
     */
    AuthenticationResponse authenticate(AuthenticationRequest request, BindingResult result);
}
