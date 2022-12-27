package com.my.location.api;

import com.my.location.core.coordinates.bean.CoordinatesData;
import com.my.location.core.location.service.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    private final LocationService locationService;

    public LocationController(final LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/getCoordinates/{streetName}")
    public CoordinatesData getCoordinatesByStreet(@PathVariable("streetName") String streetName) {
        return locationService.getCoordinatesBySteetName(streetName);
    }
}
