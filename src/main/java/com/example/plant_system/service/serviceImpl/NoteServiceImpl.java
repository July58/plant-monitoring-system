package com.example.plant_system.service.serviceImpl;

import com.example.plant_system.dto.NoteDto;
import com.example.plant_system.dto.NoteTransformer;
import com.example.plant_system.dto.UserDto;
import com.example.plant_system.dto.UserTransformer;
import com.example.plant_system.exception.NullEntityReferenceException;
import com.example.plant_system.model.Note;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.User;
import com.example.plant_system.repository.NoteRepository;
import com.example.plant_system.service.NoteService;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);


    private final NoteRepository noteRepository;
    private final PlantService plantService;
    private final UserService userService;

    public NoteServiceImpl(NoteRepository noteRepository, PlantService plantService, UserService userService) {
        this.noteRepository = noteRepository;
        this.plantService = plantService;
        this.userService = userService;
    }


    @Override
    public NoteDto create(NoteDto noteDto, Long userId, Long planId) {
        Plant plant = plantService.readById(planId);
        User user = userService.readById(userId);
        noteDto.setNoteDate(LocalDateTime.now());
        Note note = NoteTransformer.convertToEntity(noteDto, user, plant);
        Note saved = noteRepository.save(note);
        logger.debug("Note created with id: {}", saved.getId());
        return NoteTransformer.convertToDto(saved);
    }

    @Override
    public Note readById(Long id) {
        return noteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Note with id " + id + " not found"));
    }

    @Override
    public NoteDto update(NoteDto noteDto, Long userId, Long planId) {
        Plant oldPlant = plantService.readById(planId);
        if(oldPlant==null){
            logger.error("Plant with id {} not found", planId);
            throw new EntityNotFoundException("Plant with id " + planId + " not found");
        }
        User user = userService.readById(userId);
        Note note = NoteTransformer.convertToEntity(noteDto, user, oldPlant);
        Note noteUpdate = readById(note.getId());
        noteUpdate.setTitle(note.getTitle());
        noteUpdate.setText(note.getText());
        noteUpdate.setDataTime(LocalDateTime.now());
        Note saved = noteRepository.save(noteUpdate);
        logger.debug("Note updated with id: {}", saved.getId());
        return NoteTransformer.convertToDto(saved);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting note with id: {}", id);
        Note note = readById(id);
        if (note != null) {
            noteRepository.delete(note);
            logger.debug("Note deleted with id: {}", id);
        } else {
            logger.warn("Attempted to delete null note with id: {}", id);
        }
    }


    @Override
    public List<NoteDto> findNoteByPlant(Plant plant) {
        if(plant!=null){
            return noteRepository.findNoteByPlant(plant).stream().map(NoteTransformer::convertToDto).collect(Collectors.toList());
        }
        logger.error("Plant cannot be 'null'");
        throw new NullEntityReferenceException("Plant cannot be 'null'");
    }

    @Override
    public List<NoteDto> findNoteByUser(UserDto user) {
        if(user!=null){
            return noteRepository.findNoteByUser(UserTransformer.convertToEntity(user)).stream().map(NoteTransformer::convertToDto).collect(Collectors.toList());
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }
}
