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
    @Valid
    private Password password;
    @Valid
    private PersonalInfo personalInfo;
    @Valid
    private Role role;
    @Valid
    private Status status;
    
    public Account() {
        this.personalInfo = new PersonalInfo();
        this.password = new Password();
        this.role = new Role();
        this.status = new Status();
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public Password getPassword() {
        return password;
    }
    public void setPassword(final Password password) {
        this.password = password;
    }
    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }
    public void setPersonalInfo(final PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
