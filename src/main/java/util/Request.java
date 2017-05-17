package util;


/**
 * 请求对象
 * Created by song on 17/5/17.
 */
public class Request {
    private String requestType;
    private String requestUrl;
    private String responseBody;

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestUrl (String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl () {
        return this.requestUrl;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return this.responseBody;
    }
}
