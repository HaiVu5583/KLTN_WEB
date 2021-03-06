/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.dao;

import java.io.Serializable;
import java.util.List;

import kltn.hibernate.HibernateUtil;
import kltn.entity.Area;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Vu
 */
public class AreaDAO implements Serializable {

    public void AreaDAO() {

    }

    public List<Area> listAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Area> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Area.class);
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }

    public List<Area> findByBoudaryNull() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Area> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Area.class);
            cr.add(Restrictions.isNotNull("bound"));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }

    public List<Area> findByType(Character type) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Area> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Area.class);
            cr.add(Restrictions.eq("type", type));
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }

    public List<Area> findDistrictByProvince(String province) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Area> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Area.class);
            cr.add(Restrictions.eq("type", '2'));
            cr.add(Restrictions.eq("province", province).ignoreCase());
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }

    public List<Area> findPrecinctByProvinceAndDistrict(String province, String district) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Area> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Area.class);
            cr.add(Restrictions.eq("type", '3'));
            cr.add(Restrictions.eq("province", province).ignoreCase());
            cr.add(Restrictions.eq("district", district).ignoreCase());
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }
    public void update(Area area) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(area);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void updateAll(List<Area> areaList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Area area : areaList) {
                session.update(area);
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
}
