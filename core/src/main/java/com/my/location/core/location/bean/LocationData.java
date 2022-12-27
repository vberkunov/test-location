package com.my.location.core.location.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.my.location.core.coordinates.bean.CoordinatesData;


import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class LocationData implements Serializable {

    private Long id;
    private String streetName;
    private CoordinatesData coordinates;
    private Date lastModified;





}
