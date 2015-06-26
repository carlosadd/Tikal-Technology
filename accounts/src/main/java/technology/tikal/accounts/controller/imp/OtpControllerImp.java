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

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import technology.tikal.accounts.controller.OtpController;
import technology.tikal.accounts.model.InternalAccount;
import technology.tikal.accounts.otp.model.OtpSyncData;
import technology.tikal.accounts.otp.model.OtpToken;

/**
 * 
 * @author Nekorp
 *
 */
public class OtpControllerImp implements OtpController {

    /**
     * la instancia que provee el otp
     * en este caso Tikal.
     */
    private String issuer; 
    @Override
    public OtpSyncData buildOtp(InternalAccount account) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        account.getOtpInfo().setSecret(key.getKey());
        account.getOtpInfo().setEnabled(true);
        account.getOtpInfo().getScratchCodes().clear();
        account.getOtpInfo().getScratchCodes().addAll(key.getScratchCodes());
        
        String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, account.getIdUser(), key);
        OtpSyncData response = new OtpSyncData();
        response.setQrUrl(url);
        response.setSecret(key.getKey());
        return response;
    }
    @Override
    public boolean isOtpValid(InternalAccount account, Integer otp) {
        if (otp == null) {
            return false;
        }
        OtpToken otpToken = new OtpToken(otp);
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        if (account.isNewOtp(otpToken) && gAuth.authorize(account.getOtpInfo().getSecret(), otpToken.getOtp())) {
            account.addOtpToken(otpToken);
            return true;
        }
        return false;
    }
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
