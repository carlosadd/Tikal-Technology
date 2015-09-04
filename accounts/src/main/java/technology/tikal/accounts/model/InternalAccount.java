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

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import technology.tikal.accounts.otp.model.OtpInfo;
import technology.tikal.accounts.otp.model.OtpToken;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
public class InternalAccount extends Account {
    private static final int NUMBER_OF_USED_TOKENS = 3;
    @Id
    @JsonIgnore
    private String idUser;
    @JsonIgnore
    private OtpInfo otpInfo;
    @JsonIgnore
    private SortedSet<OtpToken> usedOtpTokens;
    
    private InternalAccount(){
        this.usedOtpTokens = new TreeSet<>();
    }

    public InternalAccount(Account source){
        this();
        if (source.getUser() == null) {
            throw new NullPointerException();
        }
        this.idUser = StringUtils.lowerCase(source.getUser());
        this.setUser(source.getUser());
        this.setPassword(source.getPassword());
        this.setPersonalInfo(source.getPersonalInfo());
        this.setStatus(source.getStatus());
        this.setRole(source.getRole());
        otpInfo = new OtpInfo();
    }
    @Override
    public void setUser(String user) {
        if(user == null) {
            throw new NullPointerException("El usuario no puede ser nulo");
        }
        if(this.idUser.compareToIgnoreCase(user) != 0){
            throw new IllegalArgumentException("El usuario de esta clase no puede cambiar");
        }
        super.setUser(user);
    }
    public String getIdUser() {
        return idUser;
    }
    public OtpInfo getOtpInfo() {
        return otpInfo;
    }
    public boolean isNewOtp(OtpToken otp){
        return !this.isNotNewOtp(otp);
    }
    public boolean isNotNewOtp(OtpToken otp){
        return this.usedOtpTokens.contains(otp);
    }
    public void addOtpToken(OtpToken otp) {
        if (this.isNotNewOtp(otp)) {
            throw new IllegalArgumentException("Duplicated token");
        }
        this.usedOtpTokens.add(otp);
        if(this.usedOtpTokens.size() > InternalAccount.NUMBER_OF_USED_TOKENS){
            this.usedOtpTokens.remove(this.usedOtpTokens.first());
        }
    }
}
