package ru.practicum.locations.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lat", nullable = false)
    private Double lat;
    @Column(name = "lon", nullable = false)
    private Double lon;
    @Column(name = "radius")
    private Double radius;
    @Column(name = "name")
    private String name;
    @Column(name = "saved")
    private Boolean saved;
}
