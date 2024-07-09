package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.dto.StatusRequest;
import ru.practicum.requests.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterIdAndEventId(long requesterId, long eventId);

    List<Request> findAllByRequesterIdAndEventIdAndIdIn(long requesterId, long eventId, List<Long> ids);

    int findCountByRequesterIdAndEventIdAndStatus(long requesterId, long eventId, StatusRequest status);
    List<Request> findAllByRequesterId(long requesterId);
}
