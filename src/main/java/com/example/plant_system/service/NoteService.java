package com.example.plant_system.service;

import com.example.plant_system.dto.NoteDto;
import com.example.plant_system.dto.UserDto;
import com.example.plant_system.model.Note;
import com.example.plant_system.model.Plant;

import java.util.List;

public interface NoteService {

    NoteDto create(NoteDto note, Long userId, Long plantId);
    Note readById(Long id);
    NoteDto update(NoteDto note, Long userId, Long plantId);
    void delete(Long id);
    List<NoteDto> findNoteByPlant(Plant plant);
    List<NoteDto> findNoteByUser (UserDto user);
}
