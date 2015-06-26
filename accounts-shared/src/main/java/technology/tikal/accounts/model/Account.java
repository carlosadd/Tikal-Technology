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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

/**
 * El 15 maximo de account es para tener mayor eficiencia con el memcache.
 * @author Nekorp
 */
public class Account {

    @NotNull
    @Length(min=8, max=26)
    @Pattern(regexp="\\w*")
    private String user;
    
    @NotNull
    @Length(min=8, max=26)
    @Pattern(regexp="[\\w\\!\\#$\\%\\&\\(\\)\\*\\+\\-\\.\\:\\;\\<\\=\\>\\?\\[\\]\\_\\{\\|\\}]*")
    private String password;
    @Valid
    private PersonalInfo personalInfo;
    
    public Account() {
        this.personalInfo = new PersonalInfo();
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(final String password) {
        this.password = password;
    }
    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }
    public void setPersonalInfo(final PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }
}
