/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.util.List;
import kltn.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Vu
 */
public class Utils {

    public static Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }

//    public static Double distance(String lat1, String long1, String lat2, String long2) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        List<Double> distance = null;
//        Transaction tx = null;
//        try {
//
//            tx = session.beginTransaction();
//            SQLQuery query = session.createSQLQuery("SELECT ST_Distance_Sphere ("
//                    + "	ST_GeomFromText('POINT(" + lat1 + " " + long1 + ")'),"
//                    + "	ST_GeomFromText('POINT(" + lat2 + " " + long2 + ")')"
//                    + ") As distance;");
//            distance = query.list();
//            tx.commit();
//        } catch (HibernateException he) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//        } finally {
//            session.close();
//        }
//        return distance.get(0);
//    }

    public static double deg2Rad(double deg) {
        return deg * Math.PI / 180;
    }

    public static double distance(double lat1, double long1, double lat2, double long2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2Rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2Rad(long2 - long1);
        double a  = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(deg2Rad(lat1)) * Math.cos(deg2Rad(lat2))
                       * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d;
    }
}
