package com.reddit.service;

import com.reddit.dto.VoteDto;
import com.reddit.exceptions.SpringRedditException;
import com.reddit.model.Post;
import com.reddit.model.Vote;
import com.reddit.model.VoteType;
import com.reddit.repository.PostRepository;
import com.reddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private static VoteType voteType;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post postToVote = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post Not Found with ID "+voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(postToVote, authService.getCurrentUser()); //retrieve the recent Vote submitted by the currently logged-in user for the given post

        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
            throw new SpringRedditException("You have already "+voteDto.getVoteType()+" for this post");
        }

        if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
            postToVote.setVoteCount(postToVote.getVoteCount() + 1);
        }else {
            postToVote.setVoteCount(postToVote.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteDto, postToVote));
        postRepository.save(postToVote);
    }

    private Vote mapToVote(VoteDto voteDto, Post postToVote) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(postToVote)
                .user(authService.getCurrentUser())
                .build();
    }

}
