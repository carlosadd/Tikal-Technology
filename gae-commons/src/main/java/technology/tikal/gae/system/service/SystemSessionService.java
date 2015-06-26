package technology.tikal.gae.system.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationDataString;
import technology.tikal.gae.system.security.model.UserSession;

/**
 * Interfaz para que sistemas externos creen sessiones.
 * @author Nekorp
 *
 */
public interface SystemSessionService {

    void createSession(UserSession session, BindingResult result, HttpServletRequest request, HttpServletResponse response);
    void deleteSession(String username);
    Page<List<UserSession>> querySession(PaginationDataString pagination, BindingResult paginationResult,  HttpServletRequest request);
}
