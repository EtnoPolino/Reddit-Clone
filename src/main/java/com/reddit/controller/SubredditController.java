package com.reddit.controller;

import com.reddit.dto.SubredditDto;
import com.reddit.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/subreddit")
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping()
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subredditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity getAllSubreddit(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getAll());
    }

}
