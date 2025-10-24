package com.movie.api.controller;

import com.movie.api.dto.OscarUpdateResponse;
import com.movie.api.dto.ScreenwritersResponse;
import com.movie.api.model.Person;
import com.movie.api.service.OscarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
public class OscarController {
    
    @Autowired
    private OscarService oscarService;
    
    @GetMapping("/oscar/screenwriters/get-loosers")
    public ResponseEntity<ScreenwritersResponse> getScreenwritersWithNoOscarWins() {
        try {
            List<Person> screenwriters = oscarService.getScreenwritersWithNoOscarWins();
            ScreenwritersResponse response = new ScreenwritersResponse(screenwriters);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PatchMapping("/oscar/movies/honor-by-length/{minLength}/oscars-to-add")
    public ResponseEntity<OscarUpdateResponse> addOscarsToMoviesByLength(
            @PathVariable int minLength,
            @RequestParam("oscars-to-add") int oscarsToAdd) {
        try {
            int updatedMoviesCount = oscarService.addOscarsToMoviesByLength(minLength, oscarsToAdd);
            String message = "Successfully added Oscars to " + updatedMoviesCount + " qualifying movies";
            OscarUpdateResponse response = new OscarUpdateResponse(updatedMoviesCount, message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}