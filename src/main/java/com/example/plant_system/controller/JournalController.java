package com.example.plant_system.controller;


import com.example.plant_system.config.security.userDetails.CustomUserDetails;
import com.example.plant_system.dto.NoteDto;
import com.example.plant_system.model.Plant;
import com.example.plant_system.payload.response.MessageResponse;
import com.example.plant_system.service.NoteService;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    @Autowired
    PlantService plantService;

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @PostMapping("/{plantId}/create-note")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNote(@Valid @RequestBody NoteDto noteDto, @PathVariable Long plantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_email = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        noteService.create(noteDto, userService.getUserIdByEmail(user_email), plantId);
        return ResponseEntity.ok(new MessageResponse("Note created"));
    }

    @GetMapping("/{plantId}")
    public List<NoteDto> getAllNotesByPlant(@PathVariable("plantId") Long plantId){
        Plant plant = plantService.readById(plantId);
        if(plant!=null){
            List<NoteDto> notes = noteService.findNoteByPlant(plant);
            notes.sort(Comparator.comparing(NoteDto::getNoteDate).reversed());
            return notes;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/{plantId}/{noteId}")
    public List<NoteDto> deleteNoteById(@PathVariable(name = "noteId") Long noteId, @PathVariable Long plantId) {
        noteService.delete(noteId);
        return getAllNotesByPlant(plantId);
    }

    @PutMapping("/{plantId}/edit-note")
    public ResponseEntity<?> updateNote(@Valid @RequestBody NoteDto noteDto, @PathVariable Long plantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_email = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        noteService.update(noteDto, userService.getUserIdByEmail(user_email), plantId);
        return ResponseEntity.ok(new MessageResponse("Note is updated successfully!"));
    }
}
