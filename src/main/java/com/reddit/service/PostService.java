package com.reddit.service;

import com.reddit.dto.PostRequest;
import com.reddit.dto.PostResponse;
import com.reddit.exceptions.PostNotFoundException;
import com.reddit.exceptions.SubredditNotFoundException;
import com.reddit.mapper.PostMapper;
import com.reddit.model.Post;
import com.reddit.model.Subreddit;
import com.reddit.model.User;
import com.reddit.repository.PostRepository;
import com.reddit.repository.SubredditRepository;
import com.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    /* Pour poster il faut un subreddit et les details user */
    @Transactional
    public void save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                                                 .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));

        User currentUser = authService.getCurrentUser();
        Post postTosave = postMapper.mapPostToPostRequest(postRequest, subreddit, currentUser);

        postRepository.save(postTosave);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                                  .orElseThrow(()-> new PostNotFoundException(id.toString()));

        return postMapper.mapPostResponseToDto(post);
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){
        return postRepository.findAll()
                             .stream()
                             .map(postMapper::mapPostResponseToDto)
                             .collect(toList());
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId){
        Subreddit subreddit = subredditRepository.findById(subredditId)
                                                 .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));

        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream()
                .map(postMapper::mapPostResponseToDto)
                .collect(toList());
    }

    public List<PostResponse> getPostsByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));

        return postRepository.findByUser(user)
                             .stream()
                             .map(postMapper::mapPostResponseToDto)
                             .collect(toList());
    }

}
