package com.reddit.service;

import com.reddit.dto.SubredditDto;
import com.reddit.model.Subreddit;
import com.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit subreddit = mapSubredditDto(subredditDto);
        Subreddit save = subredditRepository.save(subreddit);
        subredditDto.setId(save.getId());

        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll(){
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder()
                .subredditName(subreddit.getName())
                .id(subreddit.getId())
                .description(subreddit.getDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder()
                .name(subredditDto.getSubredditName())
                .description(subredditDto.getDescription())
                .build();
    }
}
