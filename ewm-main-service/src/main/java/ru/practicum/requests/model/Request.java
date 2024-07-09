package ru.practicum.requests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.events.model.Event;
import ru.practicum.requests.dto.StatusRequest;
import ru.practicum.users.model.User;
import ru.practicum.utils.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created")
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Enumerated(EnumType.STRING)
    private StatusRequest status;
}