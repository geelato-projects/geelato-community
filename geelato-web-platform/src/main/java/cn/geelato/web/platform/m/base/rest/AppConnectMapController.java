package cn.geelato.web.platform.m.base.rest;

import jakarta.servlet.http.HttpServletRequest;
import cn.geelato.lang.api.ApiPagedResult;
import cn.geelato.lang.constants.ApiErrorMsg;
import cn.geelato.core.gql.parser.PageQueryRequest;
import cn.geelato.web.platform.m.base.entity.AppConnectMap;
import cn.geelato.web.platform.m.base.service.AppConnectMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author diabl
 * @date 2024/4/16 9:55
 */
@Controller
@RequestMapping(value = "/api/app/connect")
public class AppConnectMapController extends BaseController {
    private static final Map<String, List<String>> OPERATORMAP = new LinkedHashMap<>();
    private static final Class<AppConnectMap> CLAZZ = AppConnectMap.class;

    static {
        OPERATORMAP.put("contains", Arrays.asList("appName", "connectName"));
        OPERATORMAP.put("intervals", Arrays.asList("createAt", "updateAt"));
    }

    private final Logger logger = LoggerFactory.getLogger(AppConnectMapController.class);
    @Autowired
    private AppConnectMapService appConnectMapService;

    @RequestMapping(value = "/pageQueryOf", method = RequestMethod.GET)
    @ResponseBody
    public ApiPagedResult pageQueryOf(HttpServletRequest req) {
        ApiPagedResult result = new ApiPagedResult();
        try {
            PageQueryRequest pageQueryRequest = this.getPageQueryParameters(req);
            Map<String, Object> params = this.getQueryParameters(req);
            result = appConnectMapService.pageQueryModel("page_query_platform_app_r_connect", params, pageQueryRequest);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return result;
    }
}
