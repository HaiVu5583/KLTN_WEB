/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.getlocation;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import static kltn.controller.StartController.checkNumber;
import kltn.entity.AtmLocation;
import kltn.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Vu
 */
public class GetAtmData {

    public static List<AtmLocation> getAgribankATMLocation() throws IOException {
        Document doc = Jsoup.connect("http://agribank.com.vn/71/1147/mang-luoi---atm-pos.aspx").timeout(10000).get();
        Element locationList = doc.select("#cphMain_Map1_ddlTinh").first();
        HashMap<Integer, String> maps = new HashMap();
        Elements locations = locationList.children();
        for (Element location : locations) {
            if (location.text().contains("Hà Nội")) {
                maps.put(Integer.parseInt(location.val()), location.text());
            }

        }
        Set<Integer> keys = maps.keySet();
        ArrayList<AtmLocation> atmList = new ArrayList<>();
        AtmLocation atm = new AtmLocation();
        for (Integer key : keys) {
            doc = Jsoup.connect("http://agribank.com.vn/tim-kiem/atm/1147/" + key.toString() + "/0/ket-qua.aspx").timeout(10000).get();
            Element div = doc.select("#ATM").first();
            Elements tables = div.select(".agri-atm-tbl");
            for (Element table : tables) {

                String district = table.select("tr th").first().text();
                char[] c = district.toCharArray();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < c.length; i++) {
                    if (!Character.toString(c[i]).equals(".")) {
                        sb.append(c[i]);
                    }
                }
                district = sb.toString();

                Elements trs = table.select("tr");
                for (Element tr : trs) {
                    if (tr.select("th").text().equals("")) {
                        atm.setBank("agribank");
                        atm.setProvinceCity("Hà Nội");
                        atm.setDistrict(district);
//                        atm.setStreet(tr.select("td.agri-atm-tbl-name").text());
                        atm.setFulladdress(tr.select("td.agri-atm-tbl-name").text());
                        atm.setUniquecode(tr.select("td.agri-atm-tbl-name").text());
                        atm.setOpentime(tr.select("td.agri-atm-tbl-xem").text());
                        String numMachine = tr.select("td.agri-atm-tbl-time").text();
                        String[] arr = numMachine.split("");
                        numMachine = arr[0].trim();
                        if (checkNumber(numMachine)) {
                            atm.setNummachine(Integer.parseInt(numMachine));
                        }
                        atmList.add(atm);
                        atm = new AtmLocation();
                    }
                }

            }
        }
        return atmList;
    }

    public static List<AtmLocation> getVietcombankATMLocation() throws IOException {
        Document doc = Jsoup.connect("https://www.vietcombank.com.vn/ATM/default.aspx").timeout(10000).get();
        String VIEWSTATE = doc.select("#__VIEWSTATE").first().val();
        Element locationList = doc.select("#ctl00_Content_CityList").first();
        Elements locations = locationList.children();
        String hanoiID = null;
        for (Element e : locations) {
            if (e.text().contains("Hà Nội")) {
                hanoiID = e.val();
            }
        }

        ArrayList<AtmLocation> atmList = new ArrayList();
        AtmLocation atm = new AtmLocation();
        doc = Jsoup.connect("https://www.vietcombank.com.vn/ATM/default.aspx").timeout(10000)
                .data("__EVENTTARGET", "")
                .data("__EVENTARGUMENT", "")
                .data("__LASTFOCUS", "")
                .data("__VIEWSTATE", VIEWSTATE)
                .data("__VIEWSTATEENCRYPTED", "")
                .data("ctl00$Content$CityList", hanoiID)
                .post();
        Elements list = doc.select("table#ctl00_Content_ATMView tr td");
        for (int i = 0; i < list.size(); i++) {
            Element e = list.get(i);
            if (i % 4 == 0) {
                atm.setUniquecode(e.text());
            } else if (i % 4 == 1) {
                atm.setNummachine(Integer.parseInt(e.text()));
            } else if (i % 4 == 2) {
                atm.setFulladdress(e.text());
            } else {
                atm.setOpentime(e.text());
                atm.setProvinceCity("Hà Nội");
                atm.setBank("vietcombank");
                atmList.add(atm);
                atm = new AtmLocation();
            }
        }
        return atmList;
    }

    public static List<AtmLocation> getVietinbankATMLocation() throws IOException {
        Document doc = Jsoup.connect("https://card.vietinbank.vn/sites/home/vi/diem-dat-atm/").timeout(20000).get();
        Element hanoiDistrictDropdown = doc.select("#ajaxmenu1").first();
//        System.out.println(hanoiDistrictList.toString());
        Elements hanoiDistrictList = hanoiDistrictDropdown.children();
        HashMap<String, String> maps = new HashMap<>();
        for (Element e : hanoiDistrictList) {
            if (!e.val().equals("")) {
                maps.put(e.text(), "https://card.vietinbank.vn" + e.val());
            }
        };

        Set<String> district = maps.keySet();
        System.out.println(district.toString());
        List<AtmLocation> atmList = new ArrayList<>();
        for (String d : district) {
            Document doc2 = Jsoup.connect(maps.get(d)).timeout(10000).get();
//            System.out.println(doc2.toString());
            Elements tds = doc2.select("td");
            AtmLocation atm = new AtmLocation();
            atm.setProvinceCity("Hà Nội");
            atm.setDistrict(d);
            atm.setBank("vietinbank");
            for (int i = 0; i < tds.size(); i++) {

                if (i % 5 == 0) {
//                    atm.setUniquecode(tds.get(i).text());
                } else if (i % 5 == 1) {
                    atm.setFulladdress(tds.get(i).text());
                    atm.setUniquecode(tds.get(i).text());
//                    System.out.println("Address: " + tds.get(i).text());
                } else if (i % 5 == 2) {
                    if (Utils.checkNumber(tds.get(i).text())) {
                        atm.setNummachine(Integer.parseInt(tds.get(i).text()));
                    }
//                    System.out.println("Num Of Machine: " + tds.get(i).text());

                } else if (i % 5 == 3) {
//                    System.out.println("Branch: " + tds.get(i).text());
                } else if (i % 5 == 4) {
                    atm.setPhone(tds.get(i).text());
                    atmList.add(atm);
                    atm = new AtmLocation();
                    atm.setProvinceCity("Hà Nội");
                    atm.setDistrict(d);
                    atm.setBank("vietinbank");
                }

            }
        }
        return atmList;
    }

    public static List<AtmLocation> getTechcombankATMLocation() throws IOException {
        String mainUrl = "https://www.techcombank.com.vn/mang-luoi-dia-diem-atm/danh-sach-chi-nhanh-phong-giao-dich-va-atm";
        Document doc = Jsoup.connect(mainUrl).timeout(10000).get();
        Element cityList = doc.getElementById("fCityId");
        Elements cities = cityList.children();
        Element hanoi = null;
        for (Element e : cities) {
            if (e.text().contains("Hà Nội")) {
                hanoi = e;
            }
        }
        String url = "https://www.techcombank.com.vn/mang-luoi-dia-diem-atm/danh-sach-chi-nhanh-phong-giao-dich-va-atm?fKeyword=&fCityId=" + hanoi.val() + "&fDistrictId=0&chkBranch=false&chkPriority=false&page=1&pageItems=50";
        Document doc2 = Jsoup.connect(url).timeout(10000).get();
        Element paging = doc2.select(".paging").first();
        Elements pageLinks = paging.select("a");
        String finalPageLink = null;
        for (Element e : pageLinks) {
            if (e.text().toLowerCase().contains("cuối")) {
                finalPageLink = e.attr("href");
            }
        }
        int finalPageNumber = Integer.parseInt(finalPageLink.split("&page=")[1].split("&")[0]);
        List<AtmLocation> atmList = new ArrayList<>();
        for (int i = 1; i <= finalPageNumber; i++) {
            url = "https://www.techcombank.com.vn/mang-luoi-dia-diem-atm/danh-sach-chi-nhanh-phong-giao-dich-va-atm?fKeyword=&fCityId=" + hanoi.val() + "&fDistrictId=0&chkBranch=false&chkPriority=false&page=" + Integer.toString(i) + "&pageItems=50";
            doc2 = Jsoup.connect(url).timeout(10000).get();
            Element atmEntries = doc2.select(".entries").first();
            Elements atmDescriptions = atmEntries.select(".description");
            Elements titleEntries = atmEntries.select(".title-entries");
            for (int j = 0; j < atmDescriptions.size(); j++) {
                Element e = atmDescriptions.get(j);
                Element f = titleEntries.get(j);
                AtmLocation atm = new AtmLocation();
                atm.setBank("techcombank");
                atm.setProvinceCity("Hà Nội");
                atm.setFulladdress(e.select(".address").first().text());
                if (!e.select(".tel").isEmpty()) {
                    atm.setPhone(e.select(".tel").first().text());
                }
                atm.setUniquecode(f.select("a").first().text());
                atm.setOpentime(e.select(".timer").first().text());
                atmList.add(atm);
            }
        }
        return atmList;
    }

    public static List<AtmLocation> getAcbATMLocation() throws IOException {
        String mainUrl = "http://acb.com.vn/wps/portal/Home/atm";
        Document doc = Jsoup.connect(mainUrl).timeout(10000).get();
        Element cityList = doc.getElementById("cityId");
        Elements cities = cityList.children();
        Element hanoi = null;
        //
        for (Element e : cities) {
            if (e.text().toLowerCase().contains("hà nội")) {
                hanoi = e;
            }
        }
        String url1 = "http://acb.com.vn/ACBMapPortlet/vn/DistrictSelectBox.jsp";
        Document doc1 = Jsoup.connect(url1).timeout(10000)
                .data("cmd", "DISTRICT")
                .data("cityId", hanoi.val())
                .get();
        Elements options = doc1.getElementsByTag("option");
        List<AtmLocation> atmList = new ArrayList();
        for (Element district : options) {
            if (!district.val().equals("")) {
//                System.out.println("Quận: " + district.text());
                String url2 = "http://acb.com.vn/ACBMapPortlet/vn/Process.jsp";
                Document doc2 = Jsoup.connect(url2)
                        .data("params[]", "Search")
                        .data("params[]", "")
                        .data("params[]", "atm")
                        .data("params[]", "")
                        .data("params[]", district.val())
                        .data("params[]", "")
                        .data("params[]", hanoi.val())
                        .data("params[]", "0")
                        .data("params[]", "http://acb.com.vn:80/ACBMapPortlet/vn/MapMobi.jsp")
                        .timeout(10000).get();
                if (!doc2.getElementsByClass("wrap-content-search-big").isEmpty()) {
                    Element table = doc2.getElementsByClass("wrap-content-search-big").first();
                    Element tbody = table.getElementsByClass("tbody").first();
                    Elements rows = tbody.getElementsByClass("row");
                    for (Element row : rows) {
                        AtmLocation atm = new AtmLocation();
                        atm.setProvinceCity("Hà Nội");
                        atm.setDistrict(district.text());
                        atm.setFulladdress(row.getElementsByClass("address").first().text());
                        atm.setOpentime(row.getElementsByClass("hours").first().text());
                        atm.setUniquecode(row.getElementsByClass("type").first().text());
                        atm.setBank("acb");
                        atm.setPhone(row.getElementsByClass("tel-fax").first().text());
                        atmList.add(atm);
                    }
                }
            }
        }
        return atmList;
    }

    public static List<AtmLocation> getVibAtmLocation() throws IOException, GeneralSecurityException {

        String mainUrl = "https://vib.com.vn/1775-truy-cap-nhanh/1777-diem-giao-dich-may-atm/1793-may-atm.aspx";
        WebClient web = new WebClient(BrowserVersion.FIREFOX_3_6);
        web.waitForBackgroundJavaScript(10000);
        web.setUseInsecureSSL(true);
        web.setThrowExceptionOnScriptError(false);
        web.setThrowExceptionOnFailingStatusCode(false);
        web.setPrintContentOnFailingStatusCode(false);
        web.setJavaScriptEnabled(true);
        web.setRedirectEnabled(true);

        HtmlPage page = web.getPage(mainUrl);

        HtmlElement mainPage = page.getElementById("page");
        HtmlElement midCol = mainPage.getElementsByAttribute("div", "class", "midCol").get(0);
        HtmlElement paging = midCol.getElementById("ctl00_ctl00_ContentPlaceHolder1_SiteCenterContent_SecondCategoryHomeTransactionATM_PagingControl_ulPaging");
        HtmlElement lastPage = paging.getElementsByTagName("li").get(paging.getElementsByTagName("li").size() - 1);
        HtmlElement lastPageLink = lastPage.getElementsByTagName("a").get(0);
        String href = lastPageLink.getAttribute("href");
        int lastPageNum = Integer.parseInt(href.split("=")[1]);
        System.out.println(lastPageNum);
        List<AtmLocation> atmList = new ArrayList();
        for (int num = 1; num < lastPageNum; num++) {
            HtmlPage page2 = web.getPage(mainUrl + "?p=" + Integer.toString(num));
            HtmlElement mainPage2 = page2.getElementById("page");
            HtmlElement midCol2 = mainPage2.getElementsByAttribute("div", "class", "midCol").get(0);
            HtmlElement atmData = (HtmlElement) midCol2.getElementsByAttribute("div", "class", "atm-title").get(0).getParentNode();
            int size = atmData.getElementsByAttribute("div", "class", "atm-title").size();
            for (int i = 0; i < size; i++) {
                String atmCode = atmData.getElementsByAttribute("div", "class", "atm-title").get(i).asText();
                String address = atmData.getElementsByAttribute("div", "class", "gallery").get(i).asText();
                AtmLocation atm = new AtmLocation();
                atm.setUniquecode(atmCode);
                atm.setFulladdress(address);
                atm.setProvinceCity("Hà Nội");
                atm.setBank("vib");
                atmList.add(atm);
            }

        }
        return atmList;
    }

    public static List<AtmLocation> getMBBankLocation() throws IOException, GeneralSecurityException {
        String mainUrl = "https://mbbank.com.vn/mangluoi/Lists/ATM/AllItems.aspx";
        WebClient web = new WebClient(BrowserVersion.FIREFOX_3_6);
        web.waitForBackgroundJavaScript(10000);
        web.setTimeout(100000);
        web.setUseInsecureSSL(true);
        web.setThrowExceptionOnScriptError(false);
        web.setThrowExceptionOnFailingStatusCode(false);
        web.setPrintContentOnFailingStatusCode(false);
        web.setJavaScriptEnabled(true);
        web.setRedirectEnabled(true);

//        StringBuilder sb = new StringBuilder();
        HtmlPage page = web.getPage(mainUrl);
        HtmlSelect cityList = page.getHtmlElementById("ctl00_m_g_4a61401d_237a_468a_b004_114aaf8a9af0_ctl01_DropDownList_Field");
        HtmlOption hanoi = cityList.getOptionByText("Hà Nội");
//        cityList.setSelectedAttribute(hanoi, true);
//        synchronized (page) {
//                page.wait(20000);
//            }
//        status2 = hanoi.getValueAttribute();
        String url = mainUrl + hanoi.getValueAttribute();
        page = web.getPage(url);
        HtmlSelect districtList = (HtmlSelect) page.getHtmlElementById("ctl00_m_g_4a61401d_237a_468a_b004_114aaf8a9af0_ctl01_DropDownList_Field2");
        List<HtmlOption> districs = districtList.getOptions();
////        List<String> list = new ArrayList<>();
//        for (HtmlOption o:districs){
//            sb.append(o.asText());
//            sb.append("__");
//            sb.append(o.getValueAttribute());
//            sb.append("\n");
//        }

//        ?FilterField1=temp1&FilterValue1=H%c3%a0+N%e1%bb%99i
//        ?FilterField1=temp1&FilterValue1=H%c3%a0+N%e1%bb%99i
        List<AtmLocation> atmList = new ArrayList<>();
        for (HtmlOption o : districs) {
            System.out.println(o.asText());
            String _url = mainUrl + o.getValueAttribute();
            HtmlPage _page = web.getPage(_url);
            HtmlDivision div = (HtmlDivision) _page.getElementById("WebPartWPQ12");
            HtmlTable table = (HtmlTable) div.getElementsByTagName("table").get(0);
            HtmlElement tBody = table.getElementsByTagName("tbody").get(0);
            DomNodeList<HtmlElement> trs = tBody.getElementsByTagName("tr");
            for (int i = 1; i < trs.size(); i++) {
                HtmlElement tr = trs.get(i);
                DomNodeList<HtmlElement> tds = tr.getElementsByTagName("td");
                AtmLocation atm = new AtmLocation();
                atm.setBank("mbbank");
                atm.setProvinceCity("Hà Nội");
                atm.setDistrict(o.asText());
                atm.setUniquecode(tds.get(2).asText());
                atm.setFulladdress(tds.get(3).asText());
                atmList.add(atm);
            }
        }
        return atmList;
    }

    public static List<AtmLocation> getBIDVATMLocation() throws IOException, InterruptedException {
        String url = "http://www.bidv.com.vn/chinhanh/ATM.aspx";
        WebClient web = new WebClient(BrowserVersion.FIREFOX_3_6);
        web.setAjaxController(new NicelyResynchronizingAjaxController());
        web.waitForBackgroundJavaScript(4000);
        web.setThrowExceptionOnScriptError(false);
        web.setThrowExceptionOnFailingStatusCode(false);
        web.setPrintContentOnFailingStatusCode(false);
        web.setJavaScriptEnabled(true);
        web.setRedirectEnabled(true);
        web.setCssEnabled(false);

        List<AtmLocation> atmList = new ArrayList<>();
        HtmlPage page = web.getPage(url);
        HtmlSelect ddTinh = page.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh");
        HtmlOption hanoi = ddTinh.getOptionByText("Hà Nội");

        ddTinh.setSelectedAttribute(hanoi, true);
        synchronized (page) {
            page.wait(10000);
        }
        HtmlSelect ddHuyen = page.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlHuyen");
        List<HtmlOption> huyen = ddHuyen.getOptions();
        String hanoiID = hanoi.getValueAttribute();
        List<String> huyenID = new ArrayList<>();
        for (HtmlOption ho : huyen) {
            huyenID.add(ho.getValueAttribute());
        }
        int size = huyen.size();
        for (int i = 1; i < size; i++) {
            HtmlPage page2 = web.getPage(url);
//        System.out.println(page2.asText());
            HtmlSelect ddTinh2 = page2.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlTinh");
            HtmlOption hanoi2 = ddTinh2.getOptionByText("Hà Nội");
//
            ddTinh2.setSelectedAttribute(hanoi2, true);
            synchronized (page2) {
                page2.wait(20000);
            }
            HtmlSelect ddHuyen2 = page2.getHtmlElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_ddlHuyen");
//            System.out.println("DDHuyen2: " + ddHuyen2.asXml());
            HtmlOption ho = ddHuyen2.getOption(i);
            System.out.println("Huyen: " + ho.asText());
            String district = ho.asText();
            System.out.println("==========================================================");
//            System.out.println("HO " + ho.asXml());
            ddHuyen2.setSelectedAttribute(ho, true);
            synchronized (page2) {
                page2.wait(20000);
            }
//            System.out.println(page2.asText());
            HtmlDivision div = (HtmlDivision) page2.getElementById("plcRoot_Layout_zoneMenu_PagePlaceholder_PagePlaceholder_Layout_zoneContent_pageplaceholder_pageplaceholder_Layout_zoneContent_DSATM_UpdatePanel1");
            HtmlDivision content = (HtmlDivision) div.getElementsByAttribute("div", "class", "tim_atm_box").get(1);
            DomNodeList<HtmlElement> atmTables = content.getElementsByTagName("table");
            for (int j = 1; j < atmTables.size(); j++) {
                HtmlElement currentElement = atmTables.get(j);
                AtmLocation atm = new AtmLocation();
                atm.setBank("bidv");
                atm.setProvinceCity("Hà Nội");
                atm.setDistrict(district);
                String address = currentElement.getElementsByAttribute("td", "class", "diadanh_sub11").get(0).asText();
                String time = currentElement.getElementsByAttribute("td", "class", "hoatdong_sub11").get(0).asText();
                String machineCode = currentElement.getElementsByAttribute("td", "class", "hoatdong_sub11").get(1).asText();
                atm.setUniquecode(machineCode);
                atm.setFulladdress(address);
                atm.setOpentime(time);
                atmList.add(atm);
            }
        }
        return atmList;
    }
}
