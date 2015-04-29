/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.dao;

import java.io.Serializable;
import java.util.List;
import kltn.entity.AtmLocation;
import kltn.hibernate.HibernateUtil;
import kltn.utils.Utils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.DisjunctionFragment;

/**
 *
 * @author Vu
 */
public class ATMLocationDAO implements Serializable {

    public ATMLocationDAO() {

    }

    public List<AtmLocation> listAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            cr.addOrder(Order.asc("id"));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    public List<String> listBank() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<String> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            cr.setProjection(Projections.distinct(Projections.property("bank")));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    public List<AtmLocation> findByFullAddressAndDistrictNotNull() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            cr.add(Restrictions.isNotNull("fulladdress"));
            cr.add(Restrictions.isNotNull("district"));
            cr.addOrder(Order.asc("id"));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    public List<AtmLocation> findByFullAddressNotnull() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            cr.add(Restrictions.isNotNull("fulladdress"));
            cr.add(Restrictions.isNull("district"));
            cr.addOrder(Order.asc("id"));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    public List<AtmLocation> findByStandardlizationStatus(char standardlization) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            cr.add(Restrictions.eq("standardlization", standardlization));
            cr.addOrder(Order.asc("id"));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    public List<AtmLocation> findByGeocodingStatus(char status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            if (status == '1') {
                cr.add(Restrictions.neOrIsNotNull("latd", ""));
            } else if (status == '0') {
                cr.add(Restrictions.eqOrIsNull("latd", ""));
            }
            cr.addOrder(Order.asc("id"));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return list;
    }

    public List<AtmLocation> filter(String keyword, String province, String district, String precinct, Character standardStatus, Character geocodingStatus, String bank) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                cr.add(Restrictions.like("fulladdress", "%" + keyword + "%").ignoreCase());
            }
            if (!province.isEmpty()) {
                cr.add(Restrictions.eq("provinceCity", province).ignoreCase());
            }
            if (!district.isEmpty()) {
                cr.add(Restrictions.eq("district", district).ignoreCase());
            }
            if (!precinct.isEmpty()) {
                cr.add(Restrictions.eq("precinct", precinct).ignoreCase());
            }
            if (standardStatus != null) {
                if (standardStatus == '0') {
                    cr.add(Restrictions.isNull("standardlization"));
                } else {
                    cr.add(Restrictions.eq("standardlization", standardStatus));
                }
            }
            if (geocodingStatus != null) {
                if (geocodingStatus == '0') {
                    cr.add(Restrictions.isNull("latd"));
                } else if (geocodingStatus == '1') {
                    cr.add(Restrictions.isNotNull("latd"));
                }
            }
            if (!bank.isEmpty()) {
                cr.add(Restrictions.eq("bank", bank).ignoreCase());
            }
            cr.addOrder(Order.asc("id"));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }

    public List<AtmLocation> filter(String province, String district, String precinct, List<String> banks) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(AtmLocation.class);
            if (!province.isEmpty()) {
                cr.add(Restrictions.eq("provinceCity", province).ignoreCase());
            }
            if (!district.isEmpty()) {
                cr.add(Restrictions.eq("district", district).ignoreCase());
            }
            if (!precinct.isEmpty()) {
                cr.add(Restrictions.eq("precinct", precinct).ignoreCase());
            }
            Disjunction dis = Restrictions.disjunction();
            for (String bank:banks)
                dis.add(Restrictions.eq("bank", bank));
            cr.add(dis);
            cr.add(Restrictions.neOrIsNotNull("latd", ""));
            cr.addOrder(Order.asc("id"));
            
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }
//    public List<Map.Entry<AtmLocation, Double>> find10NeareastATM(String lat1, String long1) {
//        Map<AtmLocation, Double> map = new HashMap();
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        List<AtmLocation> list = null;
//        List<Map.Entry<AtmLocation, Double>> listDistance = new ArrayList<>();
//
//        Criteria cr = session.createCriteria(AtmLocation.class);
//        cr.add(Restrictions.isNotNull("latd"));
//        list = cr.list();
//        for (AtmLocation atm : list) {
//            listDistance.add(new AbstractMap.SimpleEntry<>(atm, Utils.distance(lat1, long1, atm.getLatd(), atm.getLongd())));
//        }
//        for (int i = 0; i < listDistance.size() - 1; i++) {
//            for (int j = i + 1; j < listDistance.size(); j++) {
//                if (listDistance.get(i).getValue() > listDistance.get(j).getValue()) {
//                    AtmLocation tempAtm = new AtmLocation();
//                    tempAtm.copy(listDistance.get(i).getKey());
//                    Double tempDis = listDistance.get(i).getValue();
//                    listDistance.get(i).getKey().copy(listDistance.get(j).getKey());
//                    listDistance.get(i).setValue(listDistance.get(j).getValue());
//                    listDistance.get(j).getKey().copy(tempAtm);
//                    listDistance.get(j).setValue(tempDis);
//                }
//            }
//        }
//        for (int i=0; i<20; i++) {
//            Map.Entry<AtmLocation, Double> entry = listDistance.get(i);
//            System.out.println(entry.getKey().getFulladdress());
//            System.out.println(entry.getKey().getLatd());
//            System.out.println(entry.getKey().getLongd());
//            System.out.println(entry.getValue());
//            System.out.println("-------------------------");
//        }
//        return null;
//
//    }

    public List<AtmLocation> find10NeareastATM(String lat1, String long1) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Criteria cr = session.createCriteria(AtmLocation.class);
        cr.add(Restrictions.neOrIsNotNull("latd", ""));
        list = cr.list();
        for (AtmLocation atm : list) {
            atm.setDistance(Utils.distance(lat1, long1, atm.getLatd(), atm.getLongd()));
        }
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getDistance() > list.get(j).getDistance()) {
                    AtmLocation temp = new AtmLocation();
                    temp.copy(list.get(i));
                    list.get(i).copy(list.get(j));
                    list.get(j).copy(temp);
                }

            }

        }
        return list.subList(0, 19);
    }

    public List<AtmLocation> find10NeareastATM(String lat1, String long1, String bank) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AtmLocation> list = null;
        Criteria cr = session.createCriteria(AtmLocation.class);
        cr.add(Restrictions.isNotNull("latd"));
        cr.add(Restrictions.eq("bank", bank.toLowerCase()));
        list = cr.list();
        for (AtmLocation atm : list) {
            atm.setDistance(Utils.distance(lat1, long1, atm.getLatd(), atm.getLongd()));
        }
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getDistance() > list.get(j).getDistance()) {
                    AtmLocation temp = new AtmLocation();
                    temp.copy(list.get(i));
                    list.get(i).copy(list.get(j));
                    list.get(j).copy(temp);
                }

            }

        }
        return (list.size() > 20 ? list.subList(0, 19) : list);
    }

    public void insert(AtmLocation atm) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(atm);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void insertAll(List<AtmLocation> atmList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (AtmLocation atm : atmList) {
                session.save(atm);
            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void update(AtmLocation atm) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(atm);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void updateAll(List<AtmLocation> atmList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (AtmLocation atm : atmList) {
                session.update(atm);
            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void delete(AtmLocation atm) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(atm);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }
}
