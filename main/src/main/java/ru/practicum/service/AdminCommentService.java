package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.CommentNotFoundException;
import ru.practicum.repository.CommentRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCommentService {

    private final CommentRepository commentRepository;

    public void deleteCommentById(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId.toString()));
        commentRepository.deleteById(commentId);
    }

}
