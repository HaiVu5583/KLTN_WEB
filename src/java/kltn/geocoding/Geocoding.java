/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.geocoding;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import kltn.dao.ATMLocationDAO;
import kltn.dao.AreaDAO;
import kltn.entity.Area;
import kltn.entity.AtmLocation;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Vu
 */
public class Geocoding {

    private static Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }

    public static boolean checkContainNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private static Geometry getBoundary(String s) throws MalformedURLException, IOException, org.json.simple.parser.ParseException {
        String link = "https://maps.googleapis.com/maps/api/geocode/json?&key=AIzaSyALCgmmer3Cht-mFQiaJC9yoWdSqvfdAiM";
        link = link + "&address=" + URLEncoder.encode(s);
        URL url = new URL(link);
        HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
        InputStream is = httpsCon.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, "UTF-8");

        String jsonString = writer.toString();
        JSONParser parse = new JSONParser();
        Object obj = parse.parse(jsonString);
        JSONObject jsonObject = (JSONObject) obj;
        System.out.println(s);
        System.out.println(jsonObject.toJSONString());
        JSONArray resultArr = (JSONArray) jsonObject.get("results");
        Object resultObject = parse.parse(resultArr.get(0).toString());
        JSONObject resultJsonObject = (JSONObject) resultObject;

        Object geoObject = parse.parse(resultJsonObject.get("geometry").toString());
        JSONObject geoJsonObject = (JSONObject) geoObject;

        if (!geoJsonObject.containsKey("bounds")) {
            return null;
        }
        Object boundObject = parse.parse(geoJsonObject.get("bounds").toString());
        JSONObject boundJsonObject = (JSONObject) boundObject;
//        System.out.println(boundJsonObject.toJSONString());

        Object southwest = parse.parse(boundJsonObject.get("southwest").toString());
        JSONObject southwestJson = (JSONObject) southwest;
        String southwestLat = southwestJson.get("lat").toString();
        String southwestLong = southwestJson.get("lng").toString();

        Object northeast = parse.parse(boundJsonObject.get("northeast").toString());
        JSONObject northeastJson = (JSONObject) northeast;
        String northeastLat = northeastJson.get("lat").toString();
        String northeastLong = northeastJson.get("lng").toString();

        String polygon = "POLYGON((" + southwestLong.trim() + " " + northeastLat.trim() + "," + northeastLong.trim() + " " + northeastLat.trim() + "," + northeastLong.trim() + " " + southwestLat.trim() + "," + southwestLong.trim() + " " + southwestLat.trim() + "," + southwestLong.trim() + " " + northeastLat.trim() + "))";
        Geometry geo = wktToGeometry(polygon);
        return geo;
    }

    private static void prinRaw(String s) throws MalformedURLException, IOException {
        String link = "https://maps.googleapis.com/maps/api/geocode/json?&key=AIzaSyALCgmmer3Cht-mFQiaJC9yoWdSqvfdAiM";
        link = link + "&address=" + URLEncoder.encode(s);
        URL url = new URL(link);
        HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
        InputStream is = httpsCon.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, "UTF-8");

        String jsonString = writer.toString();
        System.out.println(jsonString);
    }

//    public static void main(String[] args) throws IOException, Exception {
//        AreaDAO areaDAO = new AreaDAO();
//        List<Area> listArea = areaDAO.listAll();
//        for (Area a : listArea) {
//            if (a.getType() == '1' && !a.getProvince().contains("tây")) {
//                if (getBoundary(a.getProvince() + "," + "Việt Nam") != null) {
//                    a.setBound(getBoundary(a.getProvince() + "," + "Việt Nam"));
//                }
//            } else if (a.getType() == '2') {
//                Geometry geo;
//                if(a.getSplittype()!=null)
//                    geo = getBoundary(a.getSplittype()+" "+a.getDistrict() + "," + a.getProvince() + "," + "Việt Nam");
//                else 
//                    geo = getBoundary(a.getDistrict() + "," + a.getProvince() + "," + "Việt Nam");
//                a.setBound(geo);
//            } else if (a.getType() == '3') {
//                Area district = new Area();
//                for(Area a1:listArea)
//                    if(a1.getType()=='2' && a1.getDistrict().equals(a.getDistrict()))
//                        district = a1;
//                StringBuilder sb = new StringBuilder();
//                if(a.getSplittype()!=null) {
//                    sb.append(a.getSplittype());
//                    sb.append(" ");
//                }
//                sb.append(a.getPrecinct());
//                sb.append(",");
//                if(district.getSplittype()!=null){
//                    sb.append(district.getSplittype());
//                    sb.append(" ");
//                }
//                sb.append(a.getDistrict());
//                sb.append(",");
//                sb.append(a.getProvince());
//                sb.append(",");
//                sb.append("Việt Nam");
//                Geometry geo = getBoundary(sb.toString());
//                a.setBound(geo);
//            }
//            Thread.sleep(250);
//        }
//        areaDAO.updateAll(listArea);
//        prinRaw("Đường Nguyễn Hữu Thọ, Bắc Linh Đàm, Đại Kim,Hoàng Mai, Hà Nội, Viet Nam");
//        prinRaw("đại thịnh, mê linh, hà nội, viet nam");
//    }
    public static void main(String[] args) throws IOException, InterruptedException, org.json.simple.parser.ParseException {

        ATMLocationDAO atmDAO = new ATMLocationDAO();
        List<AtmLocation> atmList = atmDAO.findByStandardlizationStatus('1');
        System.out.println(atmList.size());
        int count0 = 0;
        int count1 = 0;
        int countO = 0;
        for (AtmLocation atm : atmList) {
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

            JSONArray resultArray = (JSONArray) jsonObject.get("results");
            if (resultArray.size() == 1) {
                System.out.println("Status: "+jsonObject.get("status").toString());
                JSONObject resultObject = (JSONObject) resultArray.get(0);
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
                System.out.println(sb.toString());
                System.out.println("------------------------------");
                System.out.println(street_number + " " + route + ", " + precinct + ", " + district + ", " + province);

            } else if (resultArray.size() == 0) {
                count0++;
            } else {
                countO++;
            }
            System.out.println("=============================================================================");
            Thread.sleep(300);
        }
        System.out.println("Count 1: " + Integer.toString(count1));
        System.out.println("Count 0: " + Integer.toString(count0));
        System.out.println("Count Other: " + Integer.toString(countO));

        prinRaw("44 lý thường kiệt,Hoàn Kiếm,Hà Nội, Viet Nam");
    }
}
