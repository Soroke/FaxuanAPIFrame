package database;

import util.LoadFile;
import util.Request;

import java.util.List;

/**
 * Created by song on 2017/5/19.
 */
public class SaveRequest {
    private static List<Request> requests = new LoadFile().getRequests();
    static Connect connect = new Connect();

    public static void save() {
        for(Request request:requests) {
            //System.err.println("INSERT INTO api VALUES(" + request.getRequestType() + "," + request.getRequestUrl().replace("\"","'") + "," + request.getResponseBody().replace("\"","'") + ")");
            connect.insertSQL("INSERT INTO manager (request_type,request_url,response_body) VALUES (\"" + request.getRequestType() + "\",\"" + request.getRequestUrl().replace("\"","'") + "\",\"" + request.getResponseBody().replace("\"","'") + "\")");
        }
    }

}
