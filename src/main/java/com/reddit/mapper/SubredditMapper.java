package com.reddit.mapper;

import com.reddit.dto.SubredditDto;
import com.reddit.model.Post;
import com.reddit.model.Subreddit;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit); //subreddit to subredditDto

    default Integer mapPosts(List<Post> numberOfPosts){
        return numberOfPosts.size();
    }
    @InheritConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto); //subredditDto to subreddit
}

