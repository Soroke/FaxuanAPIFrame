package faxuan.index;

import core.Http;
import net.faxuan.util.JsonHelper;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import core.Request;

import java.util.*;

/**
 * Created by song on 2017/6/21.
 */
public class Home {
    private Logger log = Logger.getLogger(this.getClass());

    @Test
    public void test() {
        Request request = new Http().setUrl("/bss/service/getIndustryCodes").setParam("domainCode","2002001001001002001").get();
        String result = request.getResult();
        String[] industryCodes = result.split("\n")[1].split(";");
        String codes = "";
        for(String industryCode:industryCodes) {
            if(industryCode.length() == 9) {
                codes += industryCode.replaceAll("\r","");
            } else {
                codes += (industryCode + ",");
            }
        }
        System.err.println(codes);
    }

    @Test
    public void addIndustry() {
        Map<Object,Object> params = new HashMap<Object,Object>();
        for(int i=0;i<200;i++) {
            params.clear();
            params.put("industryName","行业" + (i + 1));
            params.put("operator","srk");
            new Http().setUrl("/bss/service/industryService!doaddIndustry.do")
                    .setParam(params)
                    .post();
        }

    }


    /**
     * 添加单位
     */
    @Test
    public void addDomain() {
        /**
         * 需要修改的参数
         * userAccount：管理员用户名
         * areaCode：区域编码
         * domainCode：单位编码
         * count：需要添加的单位数量
         */
        String userAccount = "lhh";
        String areaCode = "110000";
        String domainCode = "101000000000000";
        int count = 14;


        /**
         * 接口参数
           */
        Map<Object,Object> params = new HashMap<Object,Object>();

        /**
         * 获取用户的sid
         */
        Request request = new Http()
                .setUrl("/useris/service/getusersid")
                .setParam("userAccount",userAccount)
                .get();
        String sid = request.getResult().split("\n")[1].split("\r")[0];


        /**
         * 循环添加
         */
        for(int i=0;i<count;i++) {
            params.clear();
            params.put("areaCode",areaCode);
            params.put("address","");
            params.put("domainName","添加单位之" + (i + 1));
            params.put("domainType",3);
            params.put("id","");
            params.put("industryCodes","");
            params.put("linkMan","");
            params.put("operator",userAccount);
            params.put("parentCode",domainCode);
            params.put("peopleCount",0);
            params.put("phone","");
            params.put("sid",sid);
            params.put("status",0);
            /**
             * 生成随机数
             */
            int max=99999;
            int min=10000;
            Random random = new Random();
            int s = random.nextInt(max)%(max-min+1) + min;
            String unitId = "";
            if(i<9) {
                unitId = s + "612" + (i+1);
            }else if(i>8 && i<99) {
                unitId = s + "61" + (i+1);
            } else if(i>98 && i<999) {
                unitId = s + "6" + (i+1);
            } else {
                unitId = s +""+ (i+1);
            }
            params.put("unitId",unitId);
            params.put("usePeriod",0);

            new Http()
                    .setUrl("/bss/service/domainService!doAdd.do")
                    .setParam(params)
                    .post();
        }

    }

    @Test
    public void userInfor() {
        Request request = new Http()
                .setUrl("/useris/service/getusersid")
                .setParam("userAccount","lihonghua")
                .get();
        Assert.assertEquals(request.getStatusCode(),200);
        System.err.println(request.getResult().split("\n")[1]);
    }

    @Test
    public void paper() {
        Map<Object,Object> params = new HashMap<Object,Object>();
        params.put("domainCode","101002000000000");
        params.put("dc",new Date().getTime());
        params.put("page","1");
        params.put("rows","100");
        params.put("paperType","");
        params.put("paperName","");
        Request request = new Http()
                .setUrl("/ess/service/paper/paperAo!doGetList.do")
                .setParam(params)
                .post();
        Assert.assertEquals(request.getStatusCode(),200);

        String result = request.getResult();
System.out.println(JsonHelper.getValue(result,"rows").toString());
        int index = Integer.valueOf(JsonHelper.getValue(result,"total").toString());
        List<Paper> papers = new ArrayList<Paper>();
        if(index != 0) {
            String[] rows = JsonHelper.getValue(result,"rows").toString().split("},\\{");
            for(int i=1;i<index-1;i++) {
                rows[i] = "{" + rows[i] + "}";
            }
            rows[0] = (rows[0] + "}").replaceAll("\\[","");
            rows[rows.length -1] = ("{" + rows[rows.length - 1]).replaceAll("]","");

            for(String row:rows) {
                Paper paper = new Paper();
                paper.setPaperId(JsonHelper.getValue(row,"id").toString());
                paper.setPaperName(JsonHelper.getValue(row,"paperName").toString());
                papers.add(paper);
            }

            for (Paper paper:papers) {
                System.err.println(paper);
            }
        }
    }

    class Paper{
        private String paperId;
        private String paperName;

        public void setPaperId (String id) {
            this.paperId = id;
        }
        public String getPaperId() {
            return this.paperId;
        }

        public void setPaperName(String paperName) {
            this.paperName = paperName;
        }

        public String getPaperName() {
            return paperName;
        }

        public String toString() {
            return "试卷的ID：" + paperId + "，试卷的名称："  + paperName;
        }
    }
}
