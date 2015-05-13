/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.controller;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import kltn.dao.ATMLocationDAO;
import kltn.dao.AreaDAO;
import kltn.dao.EssentialWordDAO;
import kltn.entity.Area;
import kltn.entity.AtmLocation;
import kltn.entity.EssentialWord;
import kltn.getlocation.GetAtmData;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Vu
 */
@ManagedBean(name = "startController")
@ViewScoped
public class StartController implements Serializable {

    private String status;
    private String status2;
//    private boolean renderProgress;
//    private List<AtmLocation> notStandardList = null;

    @PostConstruct
    public void init() {
//        renderProgress = false;
//        ATMLocationDAO atmDAO = new ATMLocationDAO();
//        notStandardList = atmDAO.findByStandardlizationStatus('0');
    }

    @Asynchronous
    public void crawlingData() throws IOException, GeneralSecurityException, InterruptedException {

//        status = "Đang tiến hành thu thập dữ liệu ...";
        ATMLocationDAO atmDAO = new ATMLocationDAO();
//        status2 = "Đang cập nhật dữ liệu Vietcombank ATM";
//        List<AtmLocation> vietcomAtmList = GetAtmData.getVietcombankATMLocation();
//        atmDAO.synchronizeData(vietcomAtmList, "vietcombank");
//        
//        status2 = "Đang cập nhật dữ liệu Agribank ATM";
//        List<AtmLocation> agriAtmList = GetAtmData.getAgribankATMLocation();
//        atmDAO.synchronizeData(agriAtmList, "agribank");
//        
//        status2 = "Đang cập nhật dữ liệu Viettinbank ATM";
//        List<AtmLocation> vietinAtmList = GetAtmData.getVietinbankATMLocation();
//        atmDAO.synchronizeData(vietinAtmList, "vietinbank");
//        
//        status2 = "Đang cập nhật dữ liệu ACB ATM";
//        List<AtmLocation> acbAtmList = GetAtmData.getAcbATMLocation();
//        atmDAO.synchronizeData(acbAtmList, "vietinbank");
//        
//        status2 = "Đang cập nhật dữ liệu Techcombank ATM";
//        List<AtmLocation> techcomAtmList = GetAtmData.getTechcombankATMLocation();
//        atmDAO.synchronizeData(techcomAtmList, "techcombank");
        
        status2 = "Đang cập nhật dữ liệu VIB ATM";
        List<AtmLocation> vibAtmList = GetAtmData.getVibAtmLocation();
        atmDAO.synchronizeData(vibAtmList, "vib");
        
//        status2 = "Đang cập nhật dữ liệu MB ATM";
//        List<AtmLocation> mbAtmList = GetAtmData.getMBBankLocation();
//        atmDAO.synchronizeData(mbAtmList, "mbbank");
//        
//        status2 = "Đang cập nhật dữ liệu BIDV ATM";
//        List<AtmLocation> bidvAtmList = GetAtmData.getBIDVATMLocation();
//        atmDAO.synchronizeData(bidvAtmList, "bidv");
        
        status2 = "Cập nhật thành công";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cập nhật thành công"));

    }

    public void standardlize() throws InterruptedException {

        ATMLocationDAO atmDAO = new ATMLocationDAO();
        List<AtmLocation> notStandardList = atmDAO.listAll();
        if (notStandardList != null) {
            AreaDAO areaDAO = new AreaDAO();
            EssentialWordDAO wordDAO = new EssentialWordDAO();
            List<Area> areaList = areaDAO.listAll();
            List<EssentialWord> rejectWord = wordDAO.findByType('5');
            List<EssentialWord> determineWord = wordDAO.findByType('1');
//            List<EssentialWord> wordList = wordDAO.listAll();
            List<EssentialWord> deleteWord = wordDAO.findByType('7');

            for (AtmLocation atm : notStandardList) {
                String s = atm.getFulladdress();
                StringBuilder exactAddress = new StringBuilder();
                s = oneSpace(s);
                List<String> splitStr = split(s, "\\,|\\-|\\(|\\)|\\.|\\;");
                List<String> exactAddressList = new ArrayList<>();
                int numDetermine = 0;
                for (int i = splitStr.size() - 1; i >= 0; i--) {
                    String element = splitStr.get(i).toLowerCase().trim();
                    if (!isNotAddress(element, rejectWord)) {

                        if (check(areaList, '1', element)[0].equals("1")) {
                            atm.setProvinceCity(check(areaList, '1', element)[1]);

                        }
                        if (check(areaList, '2', element)[0].equals("2")) {

                            atm.setDistrict(check(areaList, '2', element)[1]);

                        }

                        if (check(areaList, '3', element)[0].equals("3")) {

                            String precinct = check(areaList, '3', element)[1];

                            if (checkPrecinct(precinct, atm.getDistrict(), atm.getProvinceCity(), areaList)) {
                                atm.setPrecinct(precinct);

                            }

                        }

                        if (isExactAddress(element) && !isDetermineLocation(element, determineWord)) {
                            exactAddressList.add(element);

                        } else if (isDetermineLocation(element, determineWord)) {
                            numDetermine++;
                            atm.setDetermineLocation(element);
                        }
                    }
                }
                for (int i = exactAddressList.size() - 1; i >= 0; i--) {
                    exactAddress.append(exactAddressList.get(i));
                    if (i > 0) {
                        exactAddress.append(",");
                    }
                }
                atm.setStreet(exactAddress.toString());
                if (exactAddressList.size() == 1) {
                    atm.setStandardlization('1');
                } else if (exactAddressList.isEmpty()) {
                    if (numDetermine == 1) {
                        atm.setStandardlization('1');
                    }
                } else {
                    atm.setStandardlization('2');
                }
            }
            for (AtmLocation atm : notStandardList) {
                if (atm.getStreet() != null && !atm.getStreet().isEmpty()) {
                    atm.setStreet(cutString(atm.getStreet(), deleteWord));
                }
            }
            atmDAO.updateAll(notStandardList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Chuẩn hóa xong"));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tất cả dữ liệu đã được chuẩn hóa"));
        }
    }

    public void getLatLong() throws InterruptedException, MalformedURLException, IOException, ParseException {
        ATMLocationDAO atmDAO = new ATMLocationDAO();
        List<AtmLocation> atmList = atmDAO.findGeocodingList();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Integer.toString(atmList.size())));

        for (AtmLocation atm : atmList) {
//        AtmLocation atm = atmList.get(2);
            StringBuilder sb = new StringBuilder();
            if (atm.getStreet() != null) {
                sb.append(atm.getStreet());
                sb.append(",");
            }
            if (atm.getPrecinct() != null) {
                sb.append(atm.getPrecinct());
                sb.append(",");
            }
            if (atm.getDistrict() != null) {
                sb.append(atm.getDistrict());
                sb.append(",");
            }
            sb.append(atm.getProvinceCity());
            sb.append(", Viet Nam");

            String link = "https://maps.googleapis.com/maps/api/geocode/json?&key=AIzaSyALCgmmer3Cht-mFQiaJC9yoWdSqvfdAiM";
            link = link + "&address=" + URLEncoder.encode(sb.toString(), "UTF-8");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(link));
            status2 = link;
            URL url = new URL(link);
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            InputStream is = httpsCon.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String jsonString = writer.toString();
//            System.out.println(jsonString);
//            System.out.println("----------------------------------------");
            JSONParser parse = new JSONParser();
            Object obj = parse.parse(jsonString);
            JSONObject jsonObject = (JSONObject) obj;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(jsonObject.get("status").toString()));
            if (jsonObject.get("status").toString().toLowerCase().trim().equals("over_query_limit")) {
                System.out.println("Reach API LIMIT");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Reach API LIMIT"));
                break;
            } else if (jsonObject.get("status").toString().toLowerCase().trim().equals("invalid_request")) {
                System.out.println("INVALID REQUEST");
                System.out.println(link);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sb.toString()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("INVALID REQUEST"));
                break;
            } else {

                JSONArray resultArray = (JSONArray) jsonObject.get("results");
                //                if (resultArray.size() == 1) {
                int countMatch = 0;
                for (Object resultO : resultArray) {
                    JSONObject resultObject = (JSONObject) resultO;
                    JSONArray addressArray = (JSONArray) resultObject.get("address_components");
                    String street_number = null;
                    String route = null;
                    String precinct = null;
                    String district = null;
                    String province = null;
                    for (Object addressObj : addressArray) {
                        JSONObject addressJsonObj = (JSONObject) addressObj;
                        if (addressJsonObj.get("types") != null) {
                            if (addressJsonObj.get("types").toString().contains("street_number")) {
                                street_number = addressJsonObj.get("long_name").toString();
                            } else if (addressJsonObj.get("types").toString().contains("route")) {
                                route = addressJsonObj.get("long_name").toString();
                            } else if (addressJsonObj.get("types").toString().contains("sublocality_level_1")) {
                                precinct = addressJsonObj.get("long_name").toString();
                            } else if (addressJsonObj.get("types").toString().contains("administrative_area_level_2")) {
                                district = addressJsonObj.get("long_name").toString();
                            } else if (addressJsonObj.get("types").toString().contains("administrative_area_level_1")) {
                                province = addressJsonObj.get("long_name").toString();
                            }

                        }
                    }
                    if (street_number != null && route != null) {
                        if (atm.getFulladdress().toLowerCase().contains(street_number.toLowerCase()) && atm.getFulladdress().toLowerCase().contains(route.toLowerCase())) {
//                            countMatch++;
                            JSONObject geoJsonObj = (JSONObject) resultObject.get("geometry");
                            JSONObject locationJsonObj = (JSONObject) geoJsonObj.get("location");
                            atm.setLatd(locationJsonObj.get("lat").toString());
                            atm.setLongd(locationJsonObj.get("lng").toString());
                        }
                    }
                }
//                if (countMatch == 0) {
//                    atm.setStandardlization('3');
//                }
                //                }
                System.out.println("=============================================================================");
                atmDAO.update(atm);
                Thread.sleep(300);
            }
        }
//        atmDAO.updateAll(atmList);
    }

    public static void prinTest(AtmLocation atm) throws InterruptedException, org.json.simple.parser.ParseException, MalformedURLException, IOException {
        StringBuilder sb = new StringBuilder();
        if (atm.getStreet() != null) {
            sb.append(atm.getStreet());
            sb.append(",");
        }
        if (atm.getPrecinct() != null) {
            sb.append(atm.getPrecinct());
            sb.append(",");
        }
        if (atm.getDistrict() != null) {
            sb.append(atm.getDistrict());
            sb.append(",");
        }
        sb.append(atm.getProvinceCity());
        sb.append(", Viet Nam");

        String link = "https://maps.googleapis.com/maps/api/geocode/json?&key=AIzaSyALCgmmer3Cht-mFQiaJC9yoWdSqvfdAiM";
        link = link + "&address=" + URLEncoder.encode(sb.toString());
        URL url = new URL(link);
        HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
        InputStream is = httpsCon.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, "UTF-8");

        String jsonString = writer.toString();

        System.out.println(jsonString);
        System.out.println("----------------------------------------");
        JSONParser parse = new JSONParser();
        Object obj = parse.parse(jsonString);
        JSONObject jsonObject = (JSONObject) obj;
        if (!jsonObject.get("status").toString().toLowerCase().trim().equals("over_query_limit")) {

            JSONArray resultArray = (JSONArray) jsonObject.get("results");
            //                if (resultArray.size() == 1) {
            int countMatch = 0;
            for (Object resultO : resultArray) {
                JSONObject resultObject = (JSONObject) resultO;
                JSONArray addressArray = (JSONArray) resultObject.get("address_components");
                String street_number = null;
                String route = null;
                String precinct = null;
                String district = null;
                String province = null;
                for (Object addressObj : addressArray) {
                    JSONObject addressJsonObj = (JSONObject) addressObj;
                    if (addressJsonObj.get("types") != null) {
                        if (addressJsonObj.get("types").toString().contains("street_number")) {
                            street_number = addressJsonObj.get("long_name").toString();
                        } else if (addressJsonObj.get("types").toString().contains("route")) {
                            route = addressJsonObj.get("long_name").toString();
                        } else if (addressJsonObj.get("types").toString().contains("sublocality_level_1")) {
                            precinct = addressJsonObj.get("long_name").toString();
                        } else if (addressJsonObj.get("types").toString().contains("administrative_area_level_2")) {
                            district = addressJsonObj.get("long_name").toString();
                        } else if (addressJsonObj.get("types").toString().contains("administrative_area_level_1")) {
                            province = addressJsonObj.get("long_name").toString();
                        }

                    }
                }
                if (street_number != null && route != null) {
                    if (atm.getFulladdress().toLowerCase().contains(street_number.toLowerCase()) && atm.getFulladdress().toLowerCase().contains(route.toLowerCase())) {
                        countMatch++;
//                            JSONArray geoArr = (JSONArray) resultObject.get("geometry");
                        JSONObject geoJsonObj = (JSONObject) resultObject.get("geometry");
//                            JSONArray locationArr = (JSONArray) geoJsonObj.get("location");
                        JSONObject locationJsonObj = (JSONObject) geoJsonObj.get("location");
                        atm.setLatd(locationJsonObj.get("lat").toString());
                        atm.setLongd(locationJsonObj.get("lng").toString());
                    }
                }
            }
            if (countMatch == 0) {
                atm.setStandardlization('3');
            }
            //                }
            System.out.println("=============================================================================");
            Thread.sleep(300);
        } else {
            System.out.println("Reach API LIMIT");

        }
        atm.print();
    }

//    public void standardize() {
//        ATMLocationDAO atmDAO = new ATMLocationDAO();
//        AreaDAO areaDAO = new AreaDAO();
//        EssentialWordDAO wordDAO = new EssentialWordDAO();
//
//        List<AtmLocation> atmList = atmDAO.listAll();
//        List<Area> areaList = areaDAO.listAll();
//        List<EssentialWord> rejectWord = wordDAO.findByType('5');
//        List<EssentialWord> determineWord = wordDAO.findByType('1');
////        List<EssentialWord> wordList = wordDAO.listAll();
//        List<EssentialWord> deleteWord = wordDAO.findByType('7');
//
//        for (AtmLocation atm : atmList) {
//            String s = atm.getFulladdress();
//            StringBuilder exactAddress = new StringBuilder();
//            s = oneSpace(s);
//            List<String> splitStr = split(s, "\\,|\\-|\\(|\\)|\\.|\\;");
//            List<String> exactAddressList = new ArrayList<>();
//            int numDetermine = 0;
//            for (int i = splitStr.size() - 1; i >= 0; i--) {
//                String element = splitStr.get(i).toLowerCase().trim();
//                if (!isNotAddress(element, rejectWord)) {
//
//                    if (check(areaList, '1', element)[0].equals("1")) {
//                        atm.setProvinceCity(check(areaList, '1', element)[1]);
//
//                    }
//                    if (check(areaList, '2', element)[0].equals("2")) {
//
//                        atm.setDistrict(check(areaList, '2', element)[1]);
//
//                    }
//
//                    if (check(areaList, '3', element)[0].equals("3")) {
//
//                        String precinct = check(areaList, '3', element)[1];
//
//                        if (checkPrecinct(precinct, atm.getDistrict(), atm.getProvinceCity(), areaList)) {
//                            atm.setPrecinct(precinct);
//
//                        }
//
//                    }
//
//                    if (isExactAddress(element) && !isDetermineLocation(element, determineWord)) {
//                        exactAddressList.add(element);
//
//                    } else if (isDetermineLocation(element, determineWord)) {
//                        numDetermine++;
//                        atm.setDetermineLocation(element);
//                    }
//                }
//            }
//            for (int i = exactAddressList.size() - 1; i >= 0; i--) {
//                exactAddress.append(exactAddressList.get(i));
//                if (i > 0) {
//                    exactAddress.append(",");
//                }
//            }
//            atm.setStreet(exactAddress.toString());
//            if (exactAddressList.size() == 1) {
//                atm.setStandardlization('1');
//            } else if (exactAddressList.isEmpty()) {
//                if (numDetermine == 1) {
//                    atm.setStandardlization('1');
//                }
//            } else {
//                atm.setStandardlization('2');
//            }
//        }
//        for (AtmLocation atm : atmList) {
//            if (atm.getStreet() != null && !atm.getStreet().isEmpty()) {
//                atm.setStreet(cutString(atm.getStreet(), deleteWord));
//            }
//        }
//        atmDAO.updateAll(atmList);
//    }

    public static String[] check(List<Area> areaList, Character type, String str) {
        String[] result = {"0", ""};
        for (Area a : areaList) {
            String shortName = a.getShortname();
            String[] names = shortName.split(",");

//            System.out.println(names.);
            for (String s : names) {
                if (str.toLowerCase().contains(s.trim())) {
                    if (a.getType() == type && type == '1' && !checkRegex(str.toLowerCase(), "((.*)(\\d+)(.*))")) {
                        result[0] = "1";
                        result[1] = a.getProvince().toLowerCase().trim();
                    } else if (a.getType() == type && type == '2' && !checkRegex(str.toLowerCase(), "((.*)(\\d+)(.*))")) {
                        result[0] = "2";
                        result[1] = a.getDistrict().toLowerCase().trim();
                    } else if (a.getType() == type && type == '3' && !checkRegex(str.toLowerCase(), "((.*)(\\d+)(.*))")) {
                        result[0] = "3";
                        result[1] = a.getPrecinct().toLowerCase().trim();
                    } else if (a.getType() == type && type == '4') {
                        result[0] = "4";
                        result[1] = a.getStreet();
                    }
                }
            }
        }
        return result;
    }

    public static boolean check(List<Area> areaList, String str) {
        for (Area a : areaList) {
            String shortName = a.getShortname();
            String[] names = shortName.split(",");
            for (String s : names) {
                if (str.toLowerCase().contains(s.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkRegex(String s, String regex) {
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(s.toLowerCase());
        return m.find();
    }

    public static int findNumberPosition(String s) {
        String[] arr = s.split(" ");
        System.out.println(arr.length);
        if (arr.length == 1) {
            return 0;
        } else {
            for (int i = 0; i < arr.length; i++) {
                try {
                    int z = Integer.parseInt(arr[i]);
                    return i;
                } catch (NumberFormatException nfe) {

                }
            }
            return 0;
        }
    }

    public static boolean checkNumber(String s) {
        try {
            int z = Integer.parseInt(s);
        } catch (NumberFormatException nbe) {
            return false;
        }
        return true;
    }

    public static boolean checkUpper(String s) {
        if (s.length() >= 1) {
            return Character.isUpperCase(s.charAt(0));
        }
        return false;
    }

    public static List split(String s, String pattern) {
        List<String> list = new ArrayList();
        String[] arr = s.split(pattern);
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].trim().isEmpty()) {
                list.add(arr[i].trim());
            }
        }
        return list;
    }

    public static int checkType(String s) {
        String[] arr = s.split(" ");
//        System.out.println(arr.length);
        if (arr.length == 1) {
            return 0;
        } else {
            if (s.toLowerCase().contains("thành phố") || s.toLowerCase().contains("tỉnh")) {
                return 1;
            } else if (s.toLowerCase().contains("quận") || s.toLowerCase().contains("huyện")) {
                return 2;
            } else if (s.toLowerCase().contains("xã") || s.toLowerCase().contains("thị trấn")) {
                return 3;
            } else if (arr[0].toLowerCase().trim().equals("tp") || arr[0].toLowerCase().trim().equals("tỉnh") || arr[0].toLowerCase().trim().equals("thành phố")) {
                return 1;
            } else if (arr[0].toLowerCase().contains("q.") || arr[0].toLowerCase().trim().equals("q")
                    || arr[0].toLowerCase().trim().equals("quận") || arr[0].toLowerCase().contains("h.")
                    || arr[0].toLowerCase().trim().equals("h") || arr[0].toLowerCase().trim().equals("huyện")) {
                return 2;
            } else if (arr[0].toLowerCase().contains("p.") || arr[0].toLowerCase().trim().equals("p")
                    || arr[0].toLowerCase().trim().equals("phường") || arr[0].toLowerCase().contains("tx.")
                    || arr[0].toLowerCase().trim().equals("tx") || arr[0].toLowerCase().trim().equals("thị xã")
                    || arr[0].toLowerCase().contains("tt.") || arr[0].toLowerCase().trim().equals("tt")
                    || arr[0].toLowerCase().trim().equals("thị trấn") || arr[0].toLowerCase().contains("x.")
                    || arr[0].toLowerCase().trim().equals("x") || arr[0].toLowerCase().trim().equals("xã")) {
                return 3;
            }
            return 0;
        }
    }

    public static String cutString(String s, List<EssentialWord> worldList) {
        String[] arr = s.split(" ");
        StringBuilder sb = new StringBuilder();
        List<String> sub1 = new ArrayList<>();
        List<String> sub2 = new ArrayList<>();
        List<String> sub = new ArrayList<>();
        for (EssentialWord ew : worldList) {
            sub.add(ew.getDisplay().toLowerCase().trim());
        }
        for (String _sub : sub) {
            if (_sub.length() == 1) {
                sub1.add(_sub);
            } else {
                sub2.add(_sub);
            }
        }
        for (String s1 : arr) {
            int i = 0;
            for (String _sub1 : sub1) {
                if (!s1.toLowerCase().equals(_sub1)) {
                    i++;
                }
            }
            if (i == sub1.size()) {
                sb.append(s1);
                sb.append(" ");
            }
        }
        s = sb.toString().trim();
//        System.out.println(s);
        for (String _sub2 : sub2) {
            if (s.contains(_sub2)) {
                s = s.substring(0, s.indexOf(_sub2)) + s.substring(s.indexOf(_sub2) + _sub2.length(), s.length());
            }
        }
        return oneSpace(s.trim());

    }

    public static String cutString2(String s, String[] sub) {
        for (String _sub : sub) {
            if (s.contains(_sub)) {
                s = s.substring(0, s.indexOf(_sub)) + s.substring(s.indexOf(_sub) + _sub.length(), s.length());
            }
        }
        return oneSpace(s);

    }

    public static String oneSpace(String s) {
        List<String> ls = split(s, " ");
        StringBuilder sb = new StringBuilder();
        for (String s1 : ls) {
            sb.append(s1);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public static boolean isNotAddress(String s, List<EssentialWord> list) {
        boolean check1 = false;
        for (EssentialWord ew : list) {
            String[] arr = ew.getDisplay().split(",");
            for (String str : arr) {
                if (s.toLowerCase().contains(str.trim())) {
                    check1 = true;
                }
            }
        }
        String[] arrBySpace = s.split(" ");
        if (check1 == true && checkNumber(arrBySpace[arrBySpace.length - 1]) == true) {
            return true;
        } else if (check1 == true && !isExactAddress(s)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDetermineLocation(String s, List<EssentialWord> list) {
        for (EssentialWord ew : list) {
            String[] arr = ew.getDisplay().split(",");
            String[] spaceSplit = s.split(" ");
            for (String str : arr) {
                if (str.split(" ").length == 1) {
                    for (String sp : spaceSplit) {
                        if (sp.toLowerCase().trim().equals(str.trim())) {
                            return true;
                        }
                    }
                } else {
                    if (s.toLowerCase().contains(str.trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isExactAddress(String s) {
        return checkRegex(s, "(.)*(\\d+)(.)*");
    }

    public static boolean checkPrecinct(String precinct, String district, String province, List<Area> areaList) {
        for (Area a : areaList) {
            if (a.getType() == '3' && a.getPrecinct().toLowerCase().trim().equals(precinct.toLowerCase().trim())) {
                if (district != null) {
                    if (a.getDistrict().toLowerCase().trim().equals(district.toLowerCase().trim()) && a.getProvince().toLowerCase().trim().equals(province.toLowerCase().trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String replaceShortWriting(String s, List<EssentialWord> worldList) {
        List<String> list = split(s, " |//,");
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            System.out.println("sub: " + str);
            int i = 0;
            for (EssentialWord ew : worldList) {
                List<String> sub = split(ew.getDisplay(), ",");
                for (String _sub : sub) {
                    if (_sub.toLowerCase().trim().equals(str.toLowerCase().trim())) {
                        sb.append(ew.getName());
                        System.out.println(ew.getName() + "-_-");
                        sb.append(" ");
                        i++;
                    }
                }
            }
            if (i == 0) {
                sb.append(str);
                sb.append(" ");
            }
        }
        return sb.toString().trim();

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public boolean isRenderProgress() {
//        return renderProgress;
//    }
//
//    public void setRenderProgress(boolean renderProgress) {
//        this.renderProgress = renderProgress;
//    }
    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }
}
