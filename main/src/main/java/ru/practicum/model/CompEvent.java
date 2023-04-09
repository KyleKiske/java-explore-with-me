package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "events_compilations", schema = "public")
@Getter
@Setter
public class CompEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "compilation_id")
    private Long compilationId;
}