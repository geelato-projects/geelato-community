package cn.geelato.web.platform.m.base.rest;


import cn.geelato.core.constants.MediaTypes;
import cn.geelato.core.meta.MetaManager;
import cn.geelato.core.meta.model.entity.EntityMeta;
import cn.geelato.core.orm.DaoException;
import cn.geelato.lang.api.ApiMetaResult;
import cn.geelato.lang.api.ApiMultiPagedResult;
import cn.geelato.lang.api.ApiPagedResult;
import cn.geelato.lang.api.ApiResult;
import cn.geelato.utils.StringUtils;
import cn.geelato.web.platform.annotation.ApiRestController;
import cn.geelato.web.platform.boot.DynamicDatasourceHolder;
import cn.geelato.web.platform.utils.GqlResolveException;
import cn.geelato.web.platform.utils.GqlUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;


/**
 * @author itechgee@126.com
 */
@ApiRestController("/meta")
@Slf4j
public class MetaController extends BaseController {

    private final MetaManager metaManager = MetaManager.singleInstance();


    @RequestMapping(value = {"/list", "list/*"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiPagedResult list(@RequestParam(value = "withMeta", defaultValue = "true") boolean withMeta, HttpServletRequest request) {
        String gql = getGql(request, "query");
        return ruleService.queryForMapList(gql, withMeta);
    }

    /**
     * 多列表查询，一次查询返回多个列表
     */
    @RequestMapping(value = {"/multiList", "multiList/*"}, method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiMultiPagedResult multiList(@RequestParam(value = "withMeta", defaultValue = "true") boolean withMeta, HttpServletRequest request) {
        String gql = getGql(request, null);
        return ruleService.queryForMultiMapList(gql, withMeta);
    }

    /**
     * @param biz     业务代码
     * @param request HttpServletRequest
     * @return SaveResult
     */
    @RequestMapping(value = {"/save/{biz}"}, method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiMetaResult save(@PathVariable("biz") String biz, HttpServletRequest request) throws DaoException {
        String gql = getGql(request, "save");
        ApiMetaResult result = new ApiMetaResult();
        result.setData(ruleService.save(biz, gql));
        return result;
    }

    @RequestMapping(value = {"/batchSave"}, method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiMetaResult batchSave(HttpServletRequest request) throws DaoException {
        String gql = getGql(request, "batchSave");
        ApiMetaResult result = new ApiMetaResult();
        result.setData(ruleService.batchSave(gql, true));
        return result;
    }

    @RequestMapping(value = {"/multiSave"}, method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiMetaResult multiSave(HttpServletRequest request) {
        String gql = getGql(request, "multiSave");
        ApiMetaResult result = new ApiMetaResult();
        result.setData(ruleService.multiSave(gql));
        return result;
    }

    @RequestMapping(value = {"/delete/{biz}/{id}"}, method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiMetaResult delete(@PathVariable("biz") String biz, @PathVariable("id") String id) {
        ApiMetaResult result = new ApiMetaResult();
        result.setData(ruleService.delete(biz, id));
        return result;
    }

    @RequestMapping(value = {"/delete2/{biz}"}, method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiMetaResult delete(@PathVariable("biz") String biz, HttpServletRequest request) {
        String gql = getGql(request, "delete");
        ApiMetaResult result = new ApiMetaResult();
        result.setData(ruleService.deleteByGql(biz, gql));
        return result;
    }

    /**
     * 获取数据定义信息，即元数据信息
     *
     * @param entityOrQueryKey 实体名称或查询键
     */
    @RequestMapping(value = {"/defined/{entityOrQueryKey}"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiMetaResult defined(@PathVariable("entityOrQueryKey") String entityOrQueryKey) {
        ApiMetaResult result = new ApiMetaResult();
        if (metaManager.containsEntity(entityOrQueryKey)) {
            result.setMeta(metaManager.getByEntityName(entityOrQueryKey).getAllSimpleFieldMetas());
        }
        return result;
    }


    /**
     * 获取实体名称列表
     */
    @RequestMapping(value = {"/entityNames"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiResult entityNames(@RequestParam String appCode) {
        return ApiResult.success(metaManager.getAllEntityNames());
    }

    /**
     * 获取指定应用下的精简版实体元数据信息列表
     *
     * @param appCode 应用编码
     */
    @RequestMapping(value = {"/entityLiteMetas"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiResult queryLiteEntities(@RequestParam String appCode) {
        return ApiResult.success(metaManager.getAllEntityLiteMetas());
    }


    /**
     * 获取通用树数据（platform_tree_node）
     *
     * @param biz     业务代码
     * @param request HttpServletRequest
     * @return ApiResult
     */
    @RequestMapping(value = {"/tree/{biz}"}, method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    @ResponseBody
    public ApiResult treeNodeList(@PathVariable("biz") String biz, @RequestParam String entity, @RequestParam Long treeId, HttpServletRequest request) {
        return ruleService.queryForTreeNodeList(entity, treeId);
    }


    private String getGql(HttpServletRequest request, String type) {
        String gql = GqlUtil.resolveGql(request);
        if (StringUtils.isEmpty(gql)) {
            throw new GqlResolveException();
        }
        if (type != null) {
            EntityMeta entityMeta = ruleService.resolveEntity(gql, type);
            DynamicDatasourceHolder.setDataSourceKey(entityMeta.getTableMeta().getConnectId());
        }
        return gql;
    }

    /**
     * 唯一性校验
     */
    @RequestMapping(value = {"/uniqueness"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.APPLICATION_JSON_UTF_8)
    public ApiResult uniqueness(HttpServletRequest request) {
        String gql = getGql(request, null);
        if (Strings.isNotBlank(gql)) {
            JSONObject jo = JSON.parseObject(gql);
            String key = jo.keySet().iterator().next();
            JSONObject value = jo.getJSONObject(key);
            if (!value.containsKey("@fs")) {
                jo.getJSONObject(key).put("@fs", "id");
            }
            if (!value.containsKey("@p")) {
                jo.getJSONObject(key).put("@p", "1,10");
            }
            gql = JSON.toJSONString(jo);
        }
        ApiPagedResult page = ruleService.queryForMapList(gql, false);
        if (page.isSuccess()) {
            return ApiResult.success(page.getTotal() == 0);
        } else {
            return ApiResult.fail(page.getMsg());
        }
    }

}
