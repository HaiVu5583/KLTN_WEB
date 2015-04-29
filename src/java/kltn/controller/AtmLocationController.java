/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import kltn.dao.ATMLocationDAO;
import kltn.dao.AreaDAO;
import kltn.entity.Area;
import kltn.entity.AtmLocation;
import org.primefaces.context.RequestContext;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
/**
 *
 * @author Vu
 */
@ManagedBean(name = "atmController")
@ViewScoped
public class AtmLocationController implements Serializable {

    private ATMLocationDAO atmDAO;
    private List<AtmLocation> atmList;
    private AtmLocation selectedAtm;
    private AtmLocation dialogAtm;
    private AreaDAO areaDAO;

    private List<Area> provinceList;
    private String selectedProvince;

    private List<Area> districtList;
    private String selectedDistrict;

    private List<Area> precinctList;
    private String selectedPrecinct;

    private List<String> bankList;
    private String selectedBank;

    private List<Area> provinceListDialog;
    private String selectedProvinceDialog;

    private List<Area> districtListDialog;
    private String selectedDistrictDialog;

    private List<Area> precinctListDialog;
    private String selectedPrecinctDialog;

    private String selectedBankDialog;

    private Character standardlizationStatus;
    private Character geocodingStatus;

    private String keyword;
    private String mapCenter;

    private MapModel mapModel;
    private Marker marker;
    private double latMarker;
    private double lngMarker;

    private String username;

    @PostConstruct
    public void init() {
        atmDAO = new ATMLocationDAO();
        areaDAO = new AreaDAO();
        atmList = atmDAO.listAll();
        provinceList = areaDAO.findByType('1');
        provinceListDialog = areaDAO.findByType('1');
        bankList = atmDAO.listBank();
        dialogAtm = new AtmLocation();
        selectedAtm = new AtmLocation();
        mapModel = new DefaultMapModel();
        username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user").toString();

//        districtList = new ArrayList<>();
//        precinctList = new ArrayList<>();
    }

    public void setAtmList(List<AtmLocation> atmList) {
        this.atmList = atmList;
    }

    public List<AtmLocation> getAtmList() {
        return atmList;
    }

    public void onProvinceChange() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Province Changed"));
        districtList = areaDAO.findDistrictByProvince(selectedProvince);
    }

    public void onDistrictChange() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("District Changed"));
        precinctList = areaDAO.findPrecinctByProvinceAndDistrict(selectedProvince, selectedDistrict);
    }

    public void onProvinceDialogChange() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Province Dialog Changed"));
        districtListDialog = areaDAO.findDistrictByProvince(selectedProvinceDialog);
    }

    public void onDistrictDialogChange() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("District Dialog Changed"));
        precinctListDialog = areaDAO.findPrecinctByProvinceAndDistrict(selectedProvinceDialog, selectedDistrictDialog);
    }

    public void search() {
        StringBuilder sb = new StringBuilder();
//        sb.append("Keyword: ");
//        sb.append(keyword);
//        sb.append("\n");
//        sb.append("Province: ");
//        sb.append(selectedProvince);
//        sb.append("\n");
//        sb.append("District: ");
//        sb.append(selectedDistrict);
//        sb.append("\n");
//        sb.append("Precinct: ");
//        sb.append(selectedPrecinct);
//        sb.append("\n");
//        sb.append("Standard Status: ");
//        sb.append(standardlizationStatus);
//        sb.append("\n");
//        sb.append("Geocoding Status: ");
//        sb.append(geocodingStatus);
//        sb.append("\n");

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
        if (selectedBank == null) {
            sb.append("Bank NULL");
        } else {
            if (selectedBank.isEmpty()) {
                sb.append("Bank Empty");
            } else {
                sb.append(selectedBank);
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sb.toString()));
        atmList = atmDAO.filter(keyword, selectedProvince, selectedDistrict, selectedPrecinct, standardlizationStatus, geocodingStatus, selectedBank);
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Integer.toString(atmList.size())));
    }

    public void createEditDialog() {
        if (selectedAtm != null) {
            dialogAtm.setFulladdress(selectedAtm.getFulladdress());
            dialogAtm.setBank(selectedAtm.getBank());
            dialogAtm.setDetermineLocation(selectedAtm.getDetermineLocation());
            dialogAtm.setDistrict(selectedAtm.getDistrict());
            dialogAtm.setId(selectedAtm.getId());
            dialogAtm.setLatd(selectedAtm.getLatd());
            dialogAtm.setLongd(selectedAtm.getLongd());
            dialogAtm.setNummachine(selectedAtm.getNummachine());
            dialogAtm.setOpentime(selectedAtm.getOpentime());
            dialogAtm.setPhone(selectedAtm.getPhone());
            dialogAtm.setPrecinct(selectedAtm.getPrecinct());
            dialogAtm.setProvinceCity(selectedAtm.getProvinceCity());
            dialogAtm.setStandardlization(selectedAtm.getStandardlization());
            dialogAtm.setStreet(selectedAtm.getStreet());
            dialogAtm.setUniquecode(selectedAtm.getUniquecode());
            selectedProvinceDialog = dialogAtm.getProvinceCity().toLowerCase().trim();

            districtListDialog = areaDAO.findDistrictByProvince(selectedProvinceDialog);
            selectedDistrictDialog = dialogAtm.getDistrict().toLowerCase().trim();

            precinctListDialog = areaDAO.findPrecinctByProvinceAndDistrict(selectedProvinceDialog, selectedDistrictDialog);
            selectedPrecinctDialog = dialogAtm.getPrecinct().toLowerCase().trim();

            if (dialogAtm.getLatd() != null && !dialogAtm.getLatd().trim().equals("")) {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Integer.toString(dialogAtm.getLatd().trim().length())));
                mapCenter = dialogAtm.getLatd() + ", " + dialogAtm.getLongd();
                List<Marker> list = mapModel.getMarkers();
                list.clear();
                list.add(new Marker(new LatLng(Double.parseDouble(dialogAtm.getLatd()), Double.parseDouble(dialogAtm.getLongd()))));
            } else {
                mapCenter = "21.0382604,105.7824129";
            }
//            if (dialogAtm.getLatd() == null) {
//                mapCenter = "21.0382604,105.7824129";
//            } else {
//                mapCenter = dialogAtm.getLatd() + ", " + dialogAtm.getLongd();
//                List<Marker> list = mapModel.getMarkers();
//                list.clear();
//                list.add(new Marker(new LatLng(Double.parseDouble(dialogAtm.getLatd()), Double.parseDouble(dialogAtm.getLongd()))));
//
//            }
            RequestContext.getCurrentInstance().execute("PF('dialog').show();");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Bạn chưa chọn dữ liệu"));
        }
    }

    public void addMarker() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Double.toString(latMarker) + "   " + Double.toString(lngMarker)));
        RequestContext.getCurrentInstance().execute("PF('gmapConfirm').hide();");
        List<Marker> list = mapModel.getMarkers();
        list.clear();
        list.add(new Marker(new LatLng(latMarker, lngMarker)));

//        mapModel.addOverlay(new Marker(new LatLng(latMarker, lngMarker)));
//        marker.setLatlng(new LatLng(latMarker, lngMarker));
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Integer.toString(mapModel.getMarkers().size())));
        dialogAtm.setLatd(Double.toString(latMarker));
        dialogAtm.setLongd(Double.toString(lngMarker));
        mapCenter = Double.toString(latMarker) + ", " + Double.toString(lngMarker);
    }

    public void showDeleteConfirm() {
        if (selectedAtm == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Bạn chưa chọn dữ liệu"));
        } else {
            RequestContext.getCurrentInstance().execute("PF('deleteConfirm').show()");
        }
    }

    public void clearSelection() {
        selectedAtm = null;
    }

    public void delete() {
        RequestContext.getCurrentInstance().execute("PF('deleteConfirm').hide()");
    }

    public void update() {
        dialogAtm.setProvinceCity(selectedProvinceDialog);
        dialogAtm.setDistrict(selectedDistrictDialog);
        dialogAtm.setPrecinct(selectedPrecinctDialog);
        atmDAO.update(dialogAtm);
        atmList = atmDAO.listAll();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Câp nhật thành công"));
        RequestContext.getCurrentInstance().execute("PF('dialog').hide()");
    }

    public void showMap() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("XXX"));
//        new FacesMessage(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString())
    }

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getSessionMap().remove("user");
//        NavigationHandler nh = FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
//        nh.handleNavigation(FacesContext.getCurrentInstance(), null, "login");
        ec.redirect(ec.getRequestContextPath()+"/faces/login.xhtml");
    }

    public AtmLocation getSelectedAtm() {
        return selectedAtm;
    }

    public void setSelectedAtm(AtmLocation selectedAtm) {
        this.selectedAtm = selectedAtm;
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

    public Character getStandardlizationStatus() {
        return standardlizationStatus;
    }

    public void setStandardlizationStatus(Character standardlizationStatus) {
        this.standardlizationStatus = standardlizationStatus;
    }

    public Character getGeocodingStatus() {
        return geocodingStatus;
    }

    public void setGeocodingStatus(Character geocodingStatus) {
        this.geocodingStatus = geocodingStatus;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<String> getBankList() {
        return bankList;
    }

    public void setBankList(List<String> bankList) {
        this.bankList = bankList;
    }

    public String getSelectedBank() {
        return selectedBank;
    }

    public void setSelectedBank(String selectedBank) {
        this.selectedBank = selectedBank;
    }

    public AtmLocation getDialogAtm() {
        return dialogAtm;
    }

    public void setDialogAtm(AtmLocation dialogAtm) {
        this.dialogAtm = dialogAtm;
    }

    public String getMapCenter() {
        return mapCenter;
    }

    public void setMapCenter(String mapCenter) {
        this.mapCenter = mapCenter;
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

    public double getLatMarker() {
        return latMarker;
    }

    public void setLatMarker(double latMarker) {
        this.latMarker = latMarker;
    }

    public double getLngMarker() {
        return lngMarker;
    }

    public void setLngMarker(double lngMarker) {
        this.lngMarker = lngMarker;
    }

    public String getSelectedProvinceDialog() {
        return selectedProvinceDialog;
    }

    public void setSelectedProvinceDialog(String selectedProvinceDialog) {
        this.selectedProvinceDialog = selectedProvinceDialog;
    }

    public String getSelectedDistrictDialog() {
        return selectedDistrictDialog;
    }

    public void setSelectedDistrictDialog(String selectedDistrictDialog) {
        this.selectedDistrictDialog = selectedDistrictDialog;
    }

    public String getSelectedPrecinctDialog() {
        return selectedPrecinctDialog;
    }

    public void setSelectedPrecinctDialog(String selectedPrecinctDialog) {
        this.selectedPrecinctDialog = selectedPrecinctDialog;
    }

    public String getSelectedBankDialog() {
        return selectedBankDialog;
    }

    public void setSelectedBankDialog(String selectedBankDialog) {
        this.selectedBankDialog = selectedBankDialog;
    }

    public List<Area> getProvinceListDialog() {
        return provinceListDialog;
    }

    public void setProvinceListDialog(List<Area> provinceListDialog) {
        this.provinceListDialog = provinceListDialog;
    }

    public List<Area> getDistrictListDialog() {
        return districtListDialog;
    }

    public void setDistrictListDialog(List<Area> districtListDialog) {
        this.districtListDialog = districtListDialog;
    }

    public List<Area> getPrecinctListDialog() {
        return precinctListDialog;
    }

    public void setPrecinctListDialog(List<Area> precinctListDialog) {
        this.precinctListDialog = precinctListDialog;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
