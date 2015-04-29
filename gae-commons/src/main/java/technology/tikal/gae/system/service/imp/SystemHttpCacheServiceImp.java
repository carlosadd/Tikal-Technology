package technology.tikal.gae.system.service.imp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import technology.tikal.gae.http.cache.HttpCacheQueryService;
import technology.tikal.gae.service.template.RestControllerTemplate;
import technology.tikal.gae.system.service.SystemHttpCacheService;

@RestController
@RequestMapping("/cache")
public class SystemHttpCacheServiceImp extends RestControllerTemplate implements SystemHttpCacheService {

    private HttpCacheQueryService httpCacheQueryService;
    @Override
    @RequestMapping(method = RequestMethod.DELETE)
    public void evictAll() {
        httpCacheQueryService.updateAllUriEtagEntry();
    }
    public void setHttpCacheQueryService(HttpCacheQueryService httpCacheQueryService) {
        this.httpCacheQueryService = httpCacheQueryService;
    }

}
