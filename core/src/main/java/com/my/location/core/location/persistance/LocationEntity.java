package com.my.location.core.location.persistance;

import lombok.*;
import com.my.location.core.coordinates.persistance.Coordinates;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "location")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street_name", nullable = false)
    private String streetName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    private Coordinates coordinates;

    @Column(name = "last_modified")
    private Date lastModified;


    public LocationEntity(String streetName, Coordinates coordinates, Date lastModified) {
        this.streetName = streetName;
        this.coordinates = coordinates;
        this.lastModified = lastModified;
    }
}
