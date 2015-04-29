/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vu
 */
@Entity
@Table(name = "\"KLTN\".atm_location")
@NamedQueries({
    @NamedQuery(name = "AtmLocation.findAll", query = "SELECT a FROM AtmLocation a")})
@XmlRootElement
public class AtmLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "fulladdress")
    private String fulladdress;
    @Column(name = "bank")
    private String bank;
    @Column(name = "opentime")
    private String opentime;
    @Column(name = "province_city")
    private String provinceCity;
    @Column(name = "district")
    private String district;
    @Column(name = "precinct")
    private String precinct;
    @Column(name = "street")
    private String street;
    @Column(name = "phone")
    private String phone;
    @Column(name = "latd")
    private String latd;
    @Column(name = "longd")
    private String longd;
    @Column(name = "nummachine")
    private Integer nummachine;
    @Column(name = "standardlization")
    private Character standardlization;
    @Column(name = "uniquecode")
    private String uniquecode;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "\"determineLocation\"")
    private String determineLocation;

    @Transient
    private double distance;
    
    public AtmLocation() {
    }
     public void copy(AtmLocation atm){
        this.bank = atm.getBank();
        this.determineLocation = atm.getDetermineLocation();
        this.district = atm.getDistrict();
        this.fulladdress = atm.getFulladdress();
        this.id = atm.getId();
        this.latd = atm.getLatd();
        this.longd = atm.getLongd();
        this.nummachine = atm.getNummachine();
        this.opentime = atm.getOpentime();
        this.phone = atm.getPhone();
        this.precinct = atm.getPrecinct();
        this.provinceCity = atm.getProvinceCity();
        this.standardlization = atm.getStandardlization();
        this.street = atm.getStreet();
        this.uniquecode = atm.getUniquecode();
        this.distance = atm.getDistance();
    }
    public AtmLocation(Integer id) {
        this.id = id;
    }

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatd() {
        return latd;
    }

    public void setLatd(String latd) {
        this.latd = latd;
    }

    public String getLongd() {
        return longd;
    }

    public void setLongd(String longd) {
        this.longd = longd;
    }

    public Integer getNummachine() {
        return nummachine;
    }

    public void setNummachine(Integer nummachine) {
        this.nummachine = nummachine;
    }

    public Character getStandardlization() {
        return standardlization;
    }

    public void setStandardlization(Character standardlization) {
        this.standardlization = standardlization;
    }

    public String getUniquecode() {
        return uniquecode;
    }

    public void setUniquecode(String uniquecode) {
        this.uniquecode = uniquecode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetermineLocation() {
        return determineLocation;
    }

    public void setDetermineLocation(String determineLocation) {
        this.determineLocation = determineLocation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    public void print(){
        System.out.println("Bank: "+bank);
        System.out.println("Address: "+fulladdress);
        System.out.println("Time Open: "+opentime);
        System.out.println("Num Machine: "+nummachine);
        System.out.println("Province: "+provinceCity);
        System.out.println("District: "+district);
        System.out.println("Precinct: "+precinct);
        System.out.println("Street: "+street);
        System.out.println("Phone: "+phone);
        System.out.println("Unique ATM Code: "+uniquecode);
        System.out.println("Determine Location: "+determineLocation);
        System.out.println("-------------------------------------");
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtmLocation)) {
            return false;
        }
        AtmLocation other = (AtmLocation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kltn.entity.AtmLocation[ id=" + id + " ]";
    }
    
}
