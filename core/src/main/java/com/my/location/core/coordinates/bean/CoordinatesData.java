package com.my.location.core.coordinates.bean;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesData implements Serializable {

    private double x;
    private double y;
}
