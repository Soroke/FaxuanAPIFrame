package xuefa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import core.Http;
import core.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 17/4/26.
 */
public class UserInfo {
    /**
     * 管理员用户名
     */
    private String userAccount;

    /**
     * 请求参数
     */
    private Map<Object,Object> param = new HashMap<Object, Object>();

    /**
     * 请求返回
     */
    Request request;

    /**
     * 构造方法
     * @param userAccount
     */
    public UserInfo(String userAccount) {
        this.userAccount = userAccount;
        param.put("userAccount",this.userAccount);
        request = doRequest();
    }


    /**
     * 请求接口获取管理员用户的信息
     * @return Request对象
     */
    private Request doRequest() {
        return new Http().setUrl("/useris/service/getdetail").setParam(param).get();
    }

    /**
     * 从返回值中取得指定值
     * @param json
     * @param key
     * @return
     */
    private String getRequestValue(String json, String key){
        JSONArray ja = JSON.parseArray(json);
        for(int i=0; i<ja.size()-1; i+=2){
            if(ja.get(i).toString().equals(key)){
                return ja.get(i+1).toString();
            }

        }
        return null;
    }


    /**
     * 获取用户domaincode
     * @return String
     */
    public String getDomainCode() {
        return getRequestValue(request.getResult(),"domainCode");
    }

    /**
     * 获取用户userName
     * @return String
     */
    public String getUserName() {
        return getRequestValue(request.getResult(),"userName");
    }

    /**
     * 获取用户areaCode
     * @return String
     */
    public String getAreaCode() {
        return getRequestValue(request.getResult(),"areaCode");
    }

    /**
     * 获取用户roleId
     * @return String
     */
    public String getRoleId() {
        return getRequestValue(request.getResult(),"roleId");
    }

    /**
     * 获取用户rankId
     * @return String
     */
    public String getRankId() {
        return getRequestValue(request.getResult(),"rankId");
    }

    /**
     * 获取用户id
     * @return String
     */
    public String getId() {
        return getRequestValue(request.getResult(),"id");
    }
}
