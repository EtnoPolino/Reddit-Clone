package com.reddit.service;

import com.reddit.dto.CommentDto;
import com.reddit.exceptions.PostNotFoundException;
import com.reddit.mapper.CommentMapper;
import com.reddit.model.Comment;
import com.reddit.model.NotificationEmail;
import com.reddit.model.Post;
import com.reddit.model.User;
import com.reddit.repository.CommentRepository;
import com.reddit.repository.PostRepository;
import com.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CommentService {

    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    public void save(CommentDto commentDto){
        Post post = postRepository.findById(commentDto.getPostId())
                                  .orElseThrow(()-> new PostNotFoundException(commentDto.getPostId().toString()));

        User currentUser = authService.getCurrentUser();
        Comment commentToSave = commentMapper.map(commentDto, post, currentUser);
        commentRepository.save(commentToSave);

        //envoyer un email/notif chaque fois que quelqu'un répond au post
        String message = mailContentBuilder.build(currentUser.getUsername() + " posted a comment on your post. "+POST_URL); //normalement l'URL du post
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        User currentUser = authService.getCurrentUser();
        mailService.sendMail(new NotificationEmail(currentUser.getUsername() + " commented on your post", user.getEmail(), message));
    }

    public List<CommentDto> getAllCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(()-> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                         .stream()
                         .map(commentMapper::mapToDto)
                         .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsByUser(String username) {
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(()-> new UsernameNotFoundException(username));

        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
