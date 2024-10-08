package cn.geelato.web.platform.script.rest;

import cn.geelato.core.graal.GraalManager;
import cn.geelato.lang.api.ApiResult;
import cn.geelato.web.platform.annotation.ApiRestController;
import cn.geelato.web.platform.m.base.rest.BaseController;
import cn.geelato.web.platform.script.entity.Api;
import cn.geelato.web.platform.script.service.ApiService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;


@ApiRestController("/service")
public class ScriptController extends BaseController {
    private final GraalManager graalManager = GraalManager.singleInstance();

    @Resource
    private ApiService apiService;

    @RequestMapping(value = "/exec/{scriptId}", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ApiResult<?> exec(@PathVariable("scriptId") String scriptId, HttpServletRequest request) {
        String parameter = getBody(request);
        String scriptContent = getScriptContent(scriptId);
        try (
            Context context = Context.newBuilder("js")
                    .allowHostAccess(HostAccess.ALL)
                    .allowHostClassLookup(className -> true).build()) {
            Map<String, Object> graalServiceMap = graalManager.getGraalServiceMap();
            Map<String, Object> graalVariableMap = graalManager.getGraalVariableMap();
            Map<String, Object> globalGraalVariableMap = graalManager.getGlobalGraalVariableMap();
            context.getBindings("js").putMember("$gl", globalGraalVariableMap);
            for (Map.Entry entry : graalServiceMap.entrySet()) {
                context.getBindings("js").putMember(entry.getKey().toString(), entry.getValue());
            }
            for (Map.Entry entry : graalVariableMap.entrySet()) {
                context.getBindings("js").putMember(entry.getKey().toString(), entry.getValue());
            }
            Map result = context.eval("js", scriptContent).execute(parameter).as(Map.class);
            return ApiResult.success(result.get("result"));
        }
    }

    private String getScriptContent(String scriptId) {
        String scriptTemplate = scriptTemplate();
        return scriptTemplate.replace("#scriptContent#", customContent(scriptId));
    }

    private String customContent(String id) {
        Api api = apiService.getModel(Api.class, id);
        return api.getReleaseContent();
    }

    private String scriptTemplate() {
        return """
                (function(parameter){
                \t var ctx={};
                \t ctx.parameter=parameter;
                \t ctx.result=#scriptContent# ();
                \t return ctx;\t
                })""";
    }
    private String getBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = null;
        try {
            br = request.getReader();
        } catch (IOException ignored) {
        }
        String str;
        try {
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
        } catch (IOException ignored) {
        }
        return stringBuilder.toString();
    }

}
