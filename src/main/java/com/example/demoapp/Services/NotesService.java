package com.example.demoapp.Services;

import com.example.demoapp.Data.Note;
import com.example.demoapp.Data.db.CategoriesEntity;
import com.example.demoapp.Data.db.NotesEntity;
import com.example.demoapp.Data.db.SharednotesEntity;
import com.example.demoapp.Data.db.UsersEntity;
import com.example.demoapp.ModelAttributeClasses.MyInteger;
import com.example.demoapp.repositories.NotesRepository;

import com.example.demoapp.repositories.db.CategoriesEntityRepository;
import com.example.demoapp.repositories.db.NotesEntityRepository;
import com.example.demoapp.repositories.db.SharednotesEntityRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotesService {
    @Autowired
    NotesEntityRepository notesEntityRepository;

    @Autowired
    NotesRepository notesRepository;

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    private HttpSession session;

    @Autowired
    SharednotesEntityRepository sharednotesEntityRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    CategoriesEntityRepository categoriesEntityRepository;


    public boolean addNewNote(Note newNote){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);
        newNote.setCreationDate(formattedDate);
        if(newNote.getNoteLink() == null){
            newNote.setNoteLink("");
        }
        NotesEntity notesEntity = convertToNotesEntity(newNote);

        if(notesEntityRepository.findByUserIdAndAndTitle(notesEntity.getUserId(),notesEntity.getTitle()) != null){
            return false;
        }else
        {
            notesRepository.getNotesList().add(notesEntity);
            return true;
        }
    }

    public List<NotesEntity> FilterByCategory(List<NotesEntity> notes, Cookie cookie) {
        if (!cookie.getValue().equals("All")) {
            String selectedCategory = cookie.getValue();
            notes = notes.stream().filter(note -> note.getCategoriesByCategoryId().getCategoryName().equals(selectedCategory)).collect(Collectors.toList());
        }
        return notes;
    }



    public List<NotesEntity> FilterByDate(List<NotesEntity> notes, Cookie cookie1, Cookie cookie2) {
        String endDateCookie = null;
        if(cookie2 != null)endDateCookie = cookie1.getValue();

        String startDateCookie = null;
        if(cookie2 != null)startDateCookie = cookie2.getValue();
        if (endDateCookie != null && !endDateCookie.equals("none") ) {
            LocalDate selectedDate = LocalDate.parse(endDateCookie, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            notes = notes.stream().filter(note -> {
                LocalDate noteDate = LocalDate.parse(note.getCreationDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                return !noteDate.isAfter(selectedDate);
            }).collect(Collectors.toList());
        }
        if (startDateCookie != null && !startDateCookie.equals("none")) {
            LocalDate selectedDate = LocalDate.parse(startDateCookie, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            notes = notes.stream().filter(note -> {
                LocalDate noteDate = LocalDate.parse(note.getCreationDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                return !noteDate.isBefore(selectedDate);
            }).collect(Collectors.toList());
        }
        return notes;
    }


    public List<NotesEntity> SortOrder(List<NotesEntity> notes, Cookie cookie) {
        if (cookie.getValue().equals("ASC_Name")) {
            Collections.sort(notes, Comparator.comparing(NotesEntity::getTitle));
        } else if (cookie.getValue().equals("DESC_Name")) {
            Collections.sort(notes, Comparator.comparing(NotesEntity::getTitle).reversed());
        } else if (cookie.getValue().equals("ASC_Category")) {
            Collections.sort(notes, Comparator.comparing(notesEntity -> notesEntity.getCategoriesByCategoryId().getCategoryName()));
        } else if (cookie.getValue().equals("DESC_Category")) {
            Comparator<NotesEntity> comparator = Comparator.comparing(notesEntity -> notesEntity.getCategoriesByCategoryId().getCategoryName());
            comparator = comparator.reversed();
            Collections.sort(notes, comparator);
        } else if (cookie.getValue().equals("ASC_CreationDate")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            Collections.sort(notes, Comparator.comparing(
                    note -> LocalDate.parse(note.getCreationDate(), formatter)));
        } else if (cookie.getValue().equals("DESC_CreationDate")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            Collections.sort(notes, Comparator.comparing(
                    note -> LocalDate.parse(note.getCreationDate(), formatter), Comparator.reverseOrder()));
        }

        return notes;
    }


    public List<NotesEntity> updateNoteCategoryNames(List<NotesEntity> notesList, List<CategoriesEntity> categoryList) {
        for (NotesEntity note : notesList) {
            int categoryId = note.getCategoryId();
            for (CategoriesEntity category : categoryList) {
                if (category.getCategoryId() == categoryId && !category.getCategoryName().equals(note.getCategoriesByCategoryId().getCategoryName())) {
                    note.getCategoriesByCategoryId().setCategoryName(category.getCategoryName());
                    break;
                }
            }
        }
        return notesList;
    }
    public List<NotesEntity> getAllNotes() {

        List<NotesEntity> notesRepo = notesRepository.getNotesList();
        List<NotesEntity> notesDB = notesEntityRepository.findAll();
        List<NotesEntity> allnotes = new ArrayList<>();
        allnotes.addAll(notesRepo);
        allnotes.addAll(notesDB);
        UsersEntity usersEntity = (UsersEntity) session.getAttribute("UsersEntity");
        allnotes = filterNotesByUserId(allnotes, usersEntity.getUserId());
        int i = cookiePosFinder(request.getCookies(), "sortOrder");
        int k = cookiePosFinder(request.getCookies(), "category");
        int z = cookiePosFinder(request.getCookies(), "endDate");
        int x = cookiePosFinder(request.getCookies(), "startDate");
        if( k != -1){
            Cookie cookie = request.getCookies()[k];
            allnotes = FilterByCategory(allnotes,cookie);
        }
        if (z != -1 || x != -1) {
            Cookie cookie1 = null;
            if(z != -1) cookie1 = request.getCookies()[z];
            Cookie cookie2 = null;
            if(x != -1) cookie2 = request.getCookies()[x];
            allnotes = FilterByDate(allnotes,cookie1,cookie2);


        }
        if (i != -1) {
            Cookie cookie = request.getCookies()[i];
            allnotes = SortOrder(allnotes,cookie);
        }
        return allnotes;
    }


    public List<NotesEntity> filterNotesByUserId(List<NotesEntity> notes, int userId) {
        List<NotesEntity> filteredNotes = new ArrayList<>();
        for (NotesEntity note : notes) {
            if (note.getUsersByUserId().getUserId() == userId) {
                filteredNotes.add(note);
            }
        }
        return filteredNotes;
    }

    public int getNextNoteId() {
        List<NotesEntity> notes = notesEntityRepository.findAll();
        List<NotesEntity> notesv2 = notesRepository.getNotesList();
        notes.addAll(notesv2);
        int maxNoteId = 0;
        for (NotesEntity note : notes) {
            int noteId = note.getNoteId();
            if (noteId > maxNoteId) {
                maxNoteId = noteId;
            }
        }
        return maxNoteId + 1;
    }

    public NotesEntity convertToNotesEntity(Note note) {
        UsersEntity usersEntity = (UsersEntity) session.getAttribute("UsersEntity");
        int z = getNextNoteId();

        NotesEntity notesEntity = new NotesEntity();
        notesEntity.setNoteId(z);
        notesEntity.setTitle(note.getTitle());
        notesEntity.setContent(note.getContent());
        notesEntity.setNoteLink(note.getNoteLink());
        notesEntity.setCreationDate(note.getCreationDate());
        notesEntity.setUserId(usersEntity.getUserId());
        notesEntity.setCategoryId(categoriesService.findCategoryIdByName(note.getCategoryName()));
        notesEntity.setCategoriesByCategoryId(categoriesService.findCategoryById(notesEntity.getCategoryId()));
        notesEntity.setUsersByUserId(usersEntity);
        return notesEntity;
    }

    public NotesEntity getNoteByLink(String noteLink) {

        List<NotesEntity> notesRepo = notesRepository.getNotesList();
        List<NotesEntity> notesDB = notesEntityRepository.findAll();
        List<NotesEntity> allnotes = new ArrayList<>();
        allnotes.addAll(notesRepo);
        allnotes.addAll(notesDB);


        if (noteLink.isEmpty()) {
            return null;
        }
        for (NotesEntity note : allnotes) {
            if (note.getNoteLink().equals(noteLink)) {
                return note;
            }
        }

        return null;
    }

    public NotesEntity getNotebyId(int noteid) {

        List<NotesEntity> notesRepo = notesRepository.getNotesList();
        List<NotesEntity> notesDB = notesEntityRepository.findAll();
        List<NotesEntity> allnotes = new ArrayList<>();
        allnotes.addAll(notesRepo);
        allnotes.addAll(notesDB);

        
        for (NotesEntity note : allnotes) {
            if (note.getNoteId() == noteid) {
                return note;
            }
        }
        return null;
    }

    private int cookiePosFinder(Cookie[] cookies, String cookieName){
        if(cookies != null){
            for(int i = 0; i < cookies.length; ++i){
                if(cookies[i].getName().equals(cookieName)){
                    return i;
                }
            }
        }
        return -1;
    }

    public List<String> getCategoriesByPopularity() {
        List<NotesEntity> notesRepo = notesRepository.getNotesList();
        List<NotesEntity> notesDB = notesEntityRepository.findAll();
        List<NotesEntity> allnotes = new ArrayList<>();
        allnotes.addAll(notesRepo);
        allnotes.addAll(notesDB);
        UsersEntity usersEntity = (UsersEntity) session.getAttribute("UsersEntity");
        allnotes = filterNotesByUserId(allnotes, usersEntity.getUserId());

        Map<String, List<NotesEntity>> notesByCategory = allnotes.stream()
                .collect(Collectors.groupingBy(notesEntity -> notesEntity.getCategoriesByCategoryId().getCategoryName()));

        List<String> sortedCategories = notesByCategory.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt((List<NotesEntity> list) -> list.size()).reversed()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return sortedCategories;
    }

    public List<NotesEntity> findNotesBySharedNotes() {
        List<SharednotesEntity> sharedNotes = sharednotesEntityRepository.findAll();
        List<NotesEntity> notes = new ArrayList<>();
        UsersEntity usersEntity = (UsersEntity) session.getAttribute("UsersEntity");
        for (SharednotesEntity sharedNote : sharedNotes) {
            int noteId = sharedNote.getNoteId();
            NotesEntity note = notesEntityRepository.findByNoteId(noteId);
            if (note != null && sharedNote.getUserId() == usersEntity.getUserId()) {
                notes.add(note);
            }
        }
        return notes;
    }

    @Transactional
    public boolean deleteNote(int noteIndex){
        if(findNoteById(notesRepository.getNotesList(),noteIndex)!= null){
            notesRepository.setNotesList(removeNoteById(notesRepository.getNotesList(),noteIndex));
            return true;

        }else if(notesEntityRepository.findByNoteId(noteIndex)!= null) {
            notesEntityRepository.deleteByNoteId(noteIndex);
        }

        return false;
    }

    public List<NotesEntity> removeNoteById(List<NotesEntity> notesList, int noteId) {
        NotesEntity noteToRemove = null;
        for (NotesEntity note : notesList) {
            if (note.getNoteId() == noteId) {
                noteToRemove = note;
                break;
            }
        }

        if (noteToRemove != null) {
            notesList.remove(noteToRemove);
        }

        return notesList;
    }



    public boolean editNote(MyInteger noteIndex, Note editedNote){
        NotesEntity note = new NotesEntity();
        if(findNoteById(notesRepository.getNotesList(),noteIndex.getValue())!= null){
            note = findNoteById(notesRepository.getNotesList(),noteIndex.getValue());
        }else if(notesEntityRepository.findByNoteId(noteIndex.getValue())!= null) note = notesEntityRepository.findByNoteId(noteIndex.getValue());
        if(note == null){
            return false;
        }
        if(!editedNote.getTitle().isEmpty()){
            note.setTitle(editedNote.getTitle());
        }
        if(!editedNote.getContent().isEmpty()){
            note.setContent(editedNote.getContent());
        }
        if(!editedNote.getNoteLink().isEmpty()){
            note.setNoteLink(editedNote.getNoteLink());
        }
        if(!editedNote.getCategoryName().isEmpty()){
            note.getCategoriesByCategoryId().setCategoryName(editedNote.getCategoryName());
        }
        return true;
    }

    public boolean isDateValid(String dateString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate date = LocalDate.parse(dateString, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public NotesEntity findNoteById(List<NotesEntity> notesList, int noteId) {
        for (NotesEntity note : notesList) {
            if (note.getNoteId() == noteId) {
                return note;
            }
        }
        return null;
    }

    public void endNotesRepo(){
        notesRepository.setNotesList(new ArrayList<>());
        notesRepository.setDeletedNotesList(new ArrayList<>());
    }

    public void saveNotesToDB(){
        List<NotesEntity> notesEntityList = notesRepository.getNotesList();
        for (NotesEntity note : notesEntityList) {
            if (notesEntityRepository.findByNoteId(note.getNoteId()) == null) {
                note.setCategoryId(categoriesEntityRepository.findByCategoryName(note.getCategoriesByCategoryId().getCategoryName()).getCategoryId());
                notesEntityRepository.save(note);
            }
        }


    }

}
