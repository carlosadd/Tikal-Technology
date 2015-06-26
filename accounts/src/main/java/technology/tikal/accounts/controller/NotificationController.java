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

package technology.tikal.accounts.controller;

/**
 * usar http://freemarker.org/ para mardar emails
 * usar MIME multipart para enviar txt y html, primero txt y luego html
 * 
 * MimeBodyPart textPart = new MimeBodyPart();
 * textPart.setText(text, "utf-8");

 * MimeBodyPart htmlPart = new MimeBodyPart();
 * htmlPart.setContent(html, "text/html; charset=utf-8");

 * multiPart.addBodyPart(textPart); <-- first
 * multiPart.addBodyPart(htmlPart); <-- second
 * message.setContent(multiPart);
 * 
 * @author Nekorp
 *
 */
public interface NotificationController {

    void SendNotification();
}
