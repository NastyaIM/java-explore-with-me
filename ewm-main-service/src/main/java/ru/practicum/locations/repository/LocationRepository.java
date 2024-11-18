package ru.practicum.locations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.locations.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(value = "select l from Location as l " +
            "where (:saved is null or l.saved = :saved) " +
            "and (:name is null or (lower(l.name) like lower(concat('%',:name,'%'))))")
    Page<Location> findAll(@Param("name") String name, @Param("saved") Boolean saved,
                           Pageable pageable);

    @Query(value = "select l from Location as l " +
            "where (l.lat = :lat and l.lon = :lon) " +
            "or (((:lat - l.lat) * (:lat - l.lat)) + ((:lon - l.lon) * (:lon - l.lon)) <= (l.radius * l.radius)) " +
            "and l.saved = true")
    Location checkLocationExistence(@Param("lat") double lat, @Param("lon") double lon);

    Location findByLatAndLonAndSavedIsTrue(@Param("lat") double lat, @Param("lon") double lon);
}