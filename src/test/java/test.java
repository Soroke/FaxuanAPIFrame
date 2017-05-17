import util.LoadFile;
import util.Request;

import java.util.List;

/**
 * Created by song on 17/5/18.
 */
public class test {
    public static void main(String[] ages) {
        List<Request> requests = new LoadFile().getRequests();
        for(Request request:requests) {
            System.out.println("请求类型："+ request.getRequestType() + "\t请求地址：" + request.getRequestUrl() + "\t请求返回：" + request.getResponseBody());
        }
    }
}
