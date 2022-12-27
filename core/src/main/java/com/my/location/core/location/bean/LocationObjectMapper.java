package com.my.location.core.location.bean;

import com.my.location.core.coordinates.bean.CoordinatesData;
import com.my.location.core.location.persistance.LocationEntity;

public final class LocationObjectMapper {


    public static LocationData map(final LocationEntity toMap){
        return LocationData.builder()
                .id(toMap.getId())
                .streetName(toMap.getStreetName())
                .coordinates(CoordinatesData.builder()
                        .x(toMap.getCoordinates().getX())
                        .y(toMap.getCoordinates().getY())
                        .build())
                .lastModified(toMap.getLastModified())
                .build();
    }

    public static CoordinatesData mapToCoordinates(final LocationEntity toMap) {

        return CoordinatesData.builder()
                .x(toMap.getCoordinates().getX())
                .y(toMap.getCoordinates().getY())
                .build();
    }

    public static CoordinatesData mapToCoordinates(final LocationData toMap){
        return CoordinatesData.builder()
                .x(toMap.getCoordinates().getX())
                .y(toMap.getCoordinates().getY())
                .build();
    }
}
