package technology.tikal.gae.system.security.dao;

import org.springframework.security.core.userdetails.UserDetailsService;

import technology.tikal.gae.dao.template.EntityDAO;
import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.gae.system.security.model.UserSession;

public interface UserSessionDao extends UserDetailsService, EntityDAO<UserSession, String, FiltroBusqueda, PaginationData<String>> {

}
