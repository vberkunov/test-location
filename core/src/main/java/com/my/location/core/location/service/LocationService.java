package com.my.location.core.location.service;

import com.my.location.core.coordinates.bean.CoordinatesData;


public interface LocationService {

    CoordinatesData getCoordinatesBySteetName(final String streetName);
}
