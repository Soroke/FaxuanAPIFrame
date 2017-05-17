package util;

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
    public LoadFile() {

        readFileByLines(new GetProperties().getPropertie("route"));

    }

    public void readFileByLines(String fileName) {

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
}
