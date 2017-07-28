package xuefa;

import core.Http;
import core.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 2017/6/21.
 */
public class DomainInfo {

    /**
     * 单位code
     */
    private String domainCode;

    /**
     * 请求参数
     */
    private Map<Object,Object> param = new HashMap<Object,Object>();

    /**
     * 请求返回
     */
    Request request;

    /**
     * 构造方法
     * @param domainCode
     */
    public DomainInfo(String domainCode) {
        this.domainCode = domainCode;
        param.put("domainCode",this.domainCode);
        request = doRequest();
    }

    /**
     * 请求接口获取单位的行业信息
     * @return Request对象
     */
    private Request doRequest() {
        return new Http().setUrl("/bss/service/getIndustryCodes").setParam(param).get();
    }


}
