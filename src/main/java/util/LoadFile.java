package util;

import database.Connect;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取fiddler文件并截取内容
 * Created by song on 17/5/17.
 */
public class LoadFile {

    List<Request> requests = new ArrayList<Request>();
    private Logger log = Logger.getLogger(this.getClass());


    public LoadFile() {

        readFileByLines(new GetProperties("fiddler").getPropertie("route"));
        save();
    }

    public void readFileByLines(String fileName) {
        log.info("开始读取文件");
        String responseBody = "";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            Request request = null;
            while ((tempString = reader.readLine()) != null) {
                if(line == 1) {
                    request  = new Request();
                    String [] contents = tempString.split("\\s+");
                    request.setRequestType(contents[0]);
                    String [] urls = contents[1].split("\\(");
                    request.setRequestUrl(urls[0]);
                    line++;
                    continue;
                } else if (line == 10000) {
                    if(tempString.equals("------------------------------------------------------------------")) {
                        request.setResponseBody(responseBody);
                        requests.add(request);
                        responseBody = "";
                        request = null;
                        line = 0;
                        continue;
                    } else if(!tempString.equals("")) {
                        responseBody = responseBody + tempString + "\n";
                    }
                    if (tempString.split(":")[0].equals("Content-Length")) {
                        responseBody = "";
                    }
                    continue;
                }
                if(tempString.split(":")[0].equals("Content-Length")) {
                    line = 10000;
                    continue;
                }

                line++;
            }
            reader.close();
            log.info("文件读取完毕，开始插入数据库");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public List<Request> getRequests() {
        return requests;
    }

    Connect connect = new Connect();
    public void save() {
        for(Request request:requests) {
            //System.err.println("INSERT INTO api VALUES(" + request.getRequestType() + "," + request.getRequestUrl().replace("\"","'") + "," + request.getResponseBody().replace("\"","'") + ")");
            connect.insertSQL("INSERT INTO request_manager (request_type,request_url,response_body) VALUES (\"" + request.getRequestType() + "\",\"" + request.getRequestUrl().replace("\"","'") + "\",\"" + request.getResponseBody().replace("\"","'") + "\")");
        }
        log.info("数据库插入完毕");
        connect.disConnect();
    }
}
