/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.controller;

/**
 *
 * @author Vu
 */
import java.io.Serializable;  
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
  
import org.primefaces.event.map.OverlaySelectEvent;  
import org.primefaces.model.map.DefaultMapModel;  
import org.primefaces.model.map.LatLng;  
import org.primefaces.model.map.MapModel;  
import org.primefaces.model.map.Marker;  

@ManagedBean(name="mapBean")
@ViewScoped
public class MapBean implements Serializable {  
      
    private MapModel advancedModel;  
  
    private Marker marker;  
  
    public MapBean() {  
        advancedModel = new DefaultMapModel();  
          
        //Shared coordinates  
        LatLng coord1 = new LatLng(36.879466, 30.667648);  
        LatLng coord2 = new LatLng(36.883707, 30.689216);  
        LatLng coord3 = new LatLng(36.879703, 30.706707);  
        LatLng coord4 = new LatLng(36.885233, 30.702323);  
          
        //Icons and Data  
        advancedModel.addOverlay(new Marker(coord1, "Konyaalti", "konyaalti.png", "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));  
        advancedModel.addOverlay(new Marker(coord2, "Ataturk Parki", "ataturkparki.png"));  
        advancedModel.addOverlay(new Marker(coord4, "Kaleici", "kaleici.png", "http://maps.google.com/mapfiles/ms/micons/pink-dot.png"));  
        advancedModel.addOverlay(new Marker(coord3, "Karaalioglu Parki", "karaalioglu.png", "http://maps.google.com/mapfiles/ms/micons/yellow-dot.png"));  
    }  
  
    public MapModel getAdvancedModel() {  
        return advancedModel;  
    }  
      
    public void onMarkerSelect(OverlaySelectEvent event) {  
        marker = (Marker) event.getOverlay();  
    }  
      
    public Marker getMarker() {  
        return marker;  
    }  
}  