package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.PaginationMaker;
import ru.practicum.dto.CommentDto;
import ru.practicum.exception.CommentNotFoundException;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.EventNotPublishedException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    public CommentDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId.toString()));
        return commentMapper.commentToDto(comment);
    }

    public List<CommentDto> getAll(Long eventId, Integer from, Integer size) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId.toString()));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventNotPublishedException("Cannot add comment to not yet published event.");
        }
        List<Comment> comments = commentRepository.findByEventId(eventId, PaginationMaker.makePageRequest(from, size))
                .getContent();
        return comments.stream().map(commentMapper::commentToDto).collect(Collectors.toList());
    }
}
