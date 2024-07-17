package ru.practicum.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.dto.State;
import ru.practicum.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(long userId, Pageable pageable);

    Page<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(List<Long> users, List<State> states,
                                                                                   List<Long> categories, LocalDateTime start, LocalDateTime end,
                                                                                   Pageable pageable);

    Page<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsBefore(List<Long> users, List<State> states,
                                                                                    List<Long> categories, LocalDateTime end,
                                                                                    Pageable pageable);

    Page<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfter(List<Long> users, List<State> states,
                                                                                   List<Long> categories, LocalDateTime start,
                                                                                   Pageable pageable);

    Page<Event> findAllByInitiatorIdInAndStateInAndCategoryIdIn(List<Long> users, List<State> states,
                                                                List<Long> categories, Pageable pageable);

    @Query(value = "select e from Event as e " +
            "where (:text is null or ((lower(e.annotation) like lower(concat('%',:text,'%'))) " +
            "or (lower(e.description) like lower(concat('%',:text,'%'))))) " +
            "and e.category.id in (:categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and e.eventDate > :start " +
            "and (:end is null or e.eventDate < :end) " +
            "and e.state = 'PUBLISHED' " +
            "and (:onlyAvailable is null or e.requestModeration = false or e.participantLimit = 0 " +
            "or e.participantLimit > e.confirmedRequests)")
    Page<Event> search(@Param("text") String text, @Param("categories") List<Long> categories, @Param("paid") Boolean paid,
                       @Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                       @Param("onlyAvailable") boolean onlyAvailable, Pageable pageable);

    @Query(value = "select e from Event as e " +
            "left join Location as l on e.location.id = l.id " +
            "where e.state = 'PUBLISHED' " +
            "and (l.lat = :lat and l.lon = :lon)")
    Page<Event> findByLocation(@Param("lat") Double lat, @Param("lon") Double lon, Pageable pageable);
}