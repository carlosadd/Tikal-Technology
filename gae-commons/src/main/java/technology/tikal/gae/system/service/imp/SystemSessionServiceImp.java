package technology.tikal.gae.system.service.imp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.pagination.PaginationModelFactory;
import technology.tikal.gae.pagination.model.Page;
import technology.tikal.gae.pagination.model.PaginationDataString;
import technology.tikal.gae.service.template.RestControllerTemplate;
import technology.tikal.gae.system.security.dao.UserSessionDao;
import technology.tikal.gae.system.security.model.UserSession;
import technology.tikal.gae.system.service.SystemSessionService;

@RestController
@RequestMapping("/session")
public class SystemSessionServiceImp extends RestControllerTemplate implements SystemSessionService {

    private UserSessionDao userSessionDao;
    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSession(@Valid @RequestBody final UserSession session, final BindingResult result, final HttpServletRequest request, final HttpServletResponse response) {
        if (result.hasErrors()) {
            throw new NotValidException(result);
        }
        this.userSessionDao.guardar(session);
        response.setHeader("Location", request.getRequestURI() + "/" + session.getUsername());
    }

    @Override
    @RequestMapping(value="/{username}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteSession(@PathVariable String username) {
        UserSession account = userSessionDao.consultar(username);
        userSessionDao.borrar(account);
    }

    @Override
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Page<List<UserSession>> querySession(@Valid @ModelAttribute PaginationDataString pagination, BindingResult paginationResult, HttpServletRequest request) {
        if (paginationResult.hasErrors()) {
            throw new NotValidException(paginationResult);
        }
        List<UserSession> result = userSessionDao.consultarTodos(null, pagination);
        Page<List<UserSession>> r = PaginationModelFactory.getPage(
                result, "UserSession", request.getRequestURI(), null, pagination);
        return r;
    }

    public void setUserSessionDao(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }    
}
