package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.CommentDto;
import ru.practicum.model.Comment;

@Component
public class CommentMapper {

    public CommentDto commentToDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setCreated(comment.getCreated());
        commentDto.setEventId(comment.getEvent().getId());
        commentDto.setCommentator(comment.getAuthor());
        commentDto.setText(comment.getText());
        return commentDto;
    }
}
