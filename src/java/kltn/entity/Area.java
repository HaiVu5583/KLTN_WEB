/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.entity;

import com.vividsolutions.jts.geom.Geometry;
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
import org.hibernate.annotations.Type;

/**
 *
 * @author Vu
 */
@Entity
@Table(name = "\"KLTN\".area")
@NamedQueries({
    @NamedQuery(name = "Area.findAll", query = "SELECT a FROM Area a")})
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "province")
    private String province;
    @Column(name = "district")
    private String district;
    @Column(name = "precinct")
    private String precinct;
    @Column(name = "street")
    private String street;
    @Column(name = "type")
    private Character type;
    @Column(name = "shortname")
    private String shortname;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "bound")
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Geometry bound;
    
    @Column(name = "splittype")
    private String splittype;
    public Area() {
    }

    public Area(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Geometry getBound() {
        return bound;
    }

    public void setBound(Geometry bound) {
        this.bound = bound;
    }

    public String getSplittype() {
        return splittype;
    }

    public void setSplittype(String splittype) {
        this.splittype = splittype;
    }
    

    public void print() {
        System.out.println("Province: " + province);
        System.out.println("District: " + district);
        System.out.println("Precinct: " + precinct);
        System.out.println("Street: " + street);
        System.out.println("Type: " + type);
        System.out.println("Display: " + shortname);
        System.out.println("-------------------------------------------");
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
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kltn.entity.Area[ id=" + id + " ]";
    }

}
