package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.dto.UpdateCommentDto;
import ru.practicum.service.UserCommentService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
public class UserCommentController {

    private final UserCommentService userCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addUserComment(@PathVariable Long userId,
                                     @PathVariable Long eventId,
                                     @RequestBody @Valid NewCommentDto newCommentDto) {
        CommentDto commentDto = userCommentService.addNewComment(userId, eventId, newCommentDto);
        log.info("Написан новый комментарий");
        return commentDto;
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto redactUserComment(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @PathVariable Long commentId,
                                        @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        CommentDto commentDto = userCommentService.redactUserComment(userId, eventId, commentId, updateCommentDto);
        log.info("Комментарий {} отредактирован", commentId);
        return commentDto;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId,
                       @PathVariable Long eventId,
                       @PathVariable Long commentId) {
        userCommentService.deleteUserComment(userId, eventId, commentId);
        log.info("Комментарий {} удален пользователем", commentId);
    }
}
