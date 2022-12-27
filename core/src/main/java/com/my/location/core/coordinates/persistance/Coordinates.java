package com.my.location.core.coordinates.persistance;


import lombok.*;


import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "x", nullable = false)
    private double x;
    @Column(name = "y", nullable = false)
    private double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y =y;
    }


}
