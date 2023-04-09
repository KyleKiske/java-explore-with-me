package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.notDto.UpdateEventRequest;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.State;

@Component
public class EventMapper {
    private final UserMapper userMapper = new UserMapper();

    public EventShortDto eventToShortDto(Event event) {
        if (event == null) {
            return null;
        }
        EventShortDto eventShortDto = new EventShortDto();

        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(event.getCategory());
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setId(event.getId());
        eventShortDto.setInitiator(userMapper.userToShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(0L);

        return eventShortDto;
    }

    public EventFullDto eventToFullDto(Event event) {
        if (event == null) {
            return null;
        }
        EventFullDto eventFullDto = new EventFullDto();

        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(event.getCategory());
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setId(event.getId());
        eventFullDto.setInitiator(userMapper.userToShortDto(event.getInitiator()));
        eventFullDto.setLocation(new Location(event.getLocationLat(),event.getLocationLon()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(0L);

        return eventFullDto;
    }

    public Event updateEventAdminToEvent(Event event, UpdateEventRequest eventAdminRequest) {
        if (eventAdminRequest.getAnnotation() != null && !eventAdminRequest.getAnnotation().isEmpty()) {
            event.setAnnotation(eventAdminRequest.getAnnotation());
        }
        if (eventAdminRequest.getDescription() != null && !eventAdminRequest.getDescription().isEmpty()) {
            event.setDescription(eventAdminRequest.getDescription());
        }
        if (eventAdminRequest.getEventDate() != null) {
            event.setEventDate(eventAdminRequest.getEventDate());
        }
        if (eventAdminRequest.getLocation() != null) {
            event.setLocationLon(eventAdminRequest.getLocation().getLon());
            event.setLocationLat(eventAdminRequest.getLocation().getLat());
        }
        if (eventAdminRequest.getPaid() != null) {
            event.setPaid(eventAdminRequest.getPaid());
        }
        if (eventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(eventAdminRequest.getParticipantLimit());
        }
        if (eventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(eventAdminRequest.getRequestModeration());
        }
        if (eventAdminRequest.getTitle() != null && !eventAdminRequest.getTitle().isEmpty()) {
            event.setTitle(eventAdminRequest.getTitle());
        }
        return event;
    }

    public Event newEventToEvent(NewEventDto newEventDto) {
        if (newEventDto == null) {
            return null;
        }
        Event event = new Event();

        event.setAnnotation(newEventDto.getAnnotation());
        event.setConfirmedRequests(0);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLocationLon(newEventDto.getLocation().getLon());
        event.setLocationLat(newEventDto.getLocation().getLat());
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        } else {
            event.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        } else {
            event.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        } else {
            event.setPaid(true);
        }
        event.setState(State.PENDING);
        event.setTitle(newEventDto.getTitle());
        return event;
    }

}
