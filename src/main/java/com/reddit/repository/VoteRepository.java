package com.reddit.repository;

import com.reddit.model.Post;
import com.reddit.model.User;
import com.reddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    //We are retrieving the recent Vote submitted by the currently logged-in user for the given Post.
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

    // We find the user by post and user info, then voteID in descending order and get the top one..
}
