package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.dto.UpdateCommentDto;
import ru.practicum.exception.*;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentDto addNewComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User commentator = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId.toString()));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventNotPublishedException("Cannot add comment to not yet published event.");
        }
        Comment comment = new Comment();
        comment.setEvent(event);
        comment.setAuthor(commentator);
        comment.setCreated(LocalDateTime.now());
        comment.setText(newCommentDto.getText());
        comment = commentRepository.save(comment);
        return commentMapper.commentToDto(comment);
    }

    public CommentDto redactUserComment(Long userId, Long eventId, Long commentId, UpdateCommentDto updateCommentDto) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId.toString());
        }
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId.toString());
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId.toString()));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new CommentNotFoundException(commentId.toString());
        }
        if (LocalDateTime.now().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new TimeRestrictionException("Cannot redact comment after 2 hours passed since publication");
        }
        if (updateCommentDto.getText() != null && !updateCommentDto.getText().isEmpty()) {
            comment.setText(updateCommentDto.getText());
        }
        commentRepository.save(comment);
        return commentMapper.commentToDto(comment);
    }

    public void deleteUserComment(Long userId, Long eventId, Long commentId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId.toString());
        }
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId.toString());
        }
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new CommentNotFoundException(commentId.toString());
        }
    }
}
