package ru.practicum.compilations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.events.model.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "events_compilations",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private Set<Event> events;
    @Column(name = "pinned")
    private boolean pinned;
    @Column(name = "title", nullable = false)
    private String title;
}
