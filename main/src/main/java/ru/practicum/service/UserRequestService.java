package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.RequestValidationException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRequestService {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        List<Request> requestList = requestRepository.findAllByRequesterId(userId);
        return requestList.stream().map(requestMapper::requestToDto).collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto addUserRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId.toString()));
        if (event.getInitiator().getId().equals(requester.getId())) {
            throw new RequestValidationException("Event Initiator cannot request to attend own event.");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RequestValidationException("Event is not published.");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new RequestValidationException("Participants limit has already been reached.");
        }
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)){
            throw new RequestValidationException("Request from this user for this event already exists");
        }
        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        if (event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        request.setCreated(LocalDateTime.now());
        request = requestRepository.save(request);
        return requestMapper.requestToDto(request);
    }

    @Transactional
    public ParticipationRequestDto cancelUserRequest(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new UserNotFoundException(requestId.toString()));
        if (request.getStatus().equals(RequestStatus.CANCELED)) {
            throw new RequestValidationException("Request was already canceled.");
        }
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);
        return requestMapper.requestToDto(request);
    }
}
