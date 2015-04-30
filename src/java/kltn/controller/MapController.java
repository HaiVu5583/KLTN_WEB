/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import kltn.dao.ATMLocationDAO;
import kltn.dao.AreaDAO;
import kltn.entity.Area;
import kltn.entity.AtmLocation;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 * @author Vu
 */
@ManagedBean(name = "mapController")
@ViewScoped
public class MapController implements Serializable {

    private ATMLocationDAO atmDAO;
    private AreaDAO areaDAO;

    private MapModel mapModel;
    private Marker marker;
    private List<String> markerData;
    private AtmLocation selectedAtm;
    private boolean phoneRender;
    private boolean timeRender;
    private boolean nummachineRender;

    private List<Area> provinceList;
    private String selectedProvince;

    private List<Area> districtList;
    private String selectedDistrict;

    private List<Area> precinctList;
    private String selectedPrecinct;

//    List<String> selectedBanks;
    List<String> bankList;
    String selectedBank;

    @PostConstruct
    public void init() {
        mapModel = new DefaultMapModel();
        atmDAO = new ATMLocationDAO();
        areaDAO = new AreaDAO();

        provinceList = areaDAO.findByType('1');

        bankList = atmDAO.listBank();
//      selectedBanks = atmDAO.listBank();

        List<AtmLocation> atmList = atmDAO.findByGeocodingStatus('1');
        for (AtmLocation atm : atmList) {
            LatLng coord = new LatLng(Double.parseDouble(atm.getLatd()), Double.parseDouble(atm.getLongd()));
            mapModel.addOverlay(new Marker(coord, "Thông tin ATM", atm));
        }
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Clicked"));

        marker = (Marker) event.getOverlay();
        selectedAtm = (AtmLocation) marker.getData();
        phoneRender = false;
        timeRender = false;
        nummachineRender = false;
        if (selectedAtm.getPhone() != null && !selectedAtm.getPhone().isEmpty()) {
            phoneRender = true;
        }
        if (selectedAtm.getOpentime() != null && !selectedAtm.getOpentime().isEmpty()) {
            timeRender = true;
        }
        if (selectedAtm.getNummachine() != null) {
            nummachineRender = true;
        }
//        markerData.clear();
//        AtmLocation currentAtm = (AtmLocation)marker.getData();
//        markerData.add("Địa chỉ: "+currentAtm.getFulladdress());
//        markerData.add("Ngân hàng: "+currentAtm.getBank());
    }

    public void onProvinceChange() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Province Changed"));
        districtList = areaDAO.findDistrictByProvince(selectedProvince);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Integer.toString(districtList.size())));
    }

    public void onDistrictChange() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("District Changed"));
        precinctList = areaDAO.findPrecinctByProvinceAndDistrict(selectedProvince, selectedDistrict);
    }

    public void search() {
        StringBuilder sb = new StringBuilder();
        if (selectedProvince == null) {
            sb.append("Province NULL");
        } else {
            if (selectedProvince.isEmpty()) {
                sb.append("Province EMPTY");
            } else {
                sb.append(selectedProvince);
            }
        }
        if (selectedDistrict == null) {
            sb.append("District NULL");
        } else {
            if (selectedDistrict.isEmpty()) {
                sb.append("District EMPTY");
            } else {
                sb.append(selectedDistrict);
            }
        }
        if (selectedPrecinct == null) {
            sb.append("Precinct NULL");
        } else {
            if (selectedPrecinct.isEmpty()) {
                sb.append("Precinct EMPTY");
            } else {
                sb.append(selectedPrecinct);
            }
        }
        List<AtmLocation> atmList = atmDAO.filter(selectedProvince, selectedDistrict, selectedPrecinct, selectedBank);
        List<Marker> markers = mapModel.getMarkers();
        markers.clear();
        for (AtmLocation atm : atmList) {
            mapModel.addOverlay(new Marker(new LatLng(Double.parseDouble(atm.getLatd()), Double.parseDouble(atm.getLongd())), "Thông tin ATM", atm));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sb.toString()));
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public List<Area> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Area> provinceList) {
        this.provinceList = provinceList;
    }

    public String getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(String selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public List<Area> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<Area> districtList) {
        this.districtList = districtList;
    }

    public String getSelectedDistrict() {
        return selectedDistrict;
    }

    public void setSelectedDistrict(String selectedDistrict) {
        this.selectedDistrict = selectedDistrict;
    }

    public List<Area> getPrecinctList() {
        return precinctList;
    }

    public void setPrecinctList(List<Area> precinctList) {
        this.precinctList = precinctList;
    }

    public String getSelectedPrecinct() {
        return selectedPrecinct;
    }

    public void setSelectedPrecinct(String selectedPrecinct) {
        this.selectedPrecinct = selectedPrecinct;
    }

//    public List<String> getSelectedBanks() {
//        return selectedBanks;
//    }
//
//    public void setSelectedBanks(List<String> selectedBanks) {
//        this.selectedBanks = selectedBanks;
//    }

    public String getSelectedBank() {
        return selectedBank;
    }

    public void setSelectedBank(String selectedBank) {
        this.selectedBank = selectedBank;
    }

    
    
    public List<String> getBankList() {
        return bankList;
    }

    public void setBankList(List<String> bankList) {
        this.bankList = bankList;
    }

    public List<String> getMarkerData() {
        return markerData;
    }

    public void setMarkerData(List<String> markerData) {
        this.markerData = markerData;
    }

    public AtmLocation getSelectedAtm() {
        return selectedAtm;
    }

    public void setSelectedAtm(AtmLocation selectedAtm) {
        this.selectedAtm = selectedAtm;
    }

    public boolean isPhoneRender() {
        return phoneRender;
    }

    public void setPhoneRender(boolean phoneRender) {
        this.phoneRender = phoneRender;
    }

    public boolean isTimeRender() {
        return timeRender;
    }

    public void setTimeRender(boolean timeRender) {
        this.timeRender = timeRender;
    }

    public boolean isNummachineRender() {
        return nummachineRender;
    }

    public void setNummachineRender(boolean nummachineRender) {
        this.nummachineRender = nummachineRender;
    }

}
