package com.example.demoapp.Controllers;

import com.example.demoapp.Data.Note;
import com.example.demoapp.Data.db.NotesEntity;
import com.example.demoapp.Data.db.SharednotesEntity;
import com.example.demoapp.Data.db.UsersEntity;
import com.example.demoapp.ModelAttributeClasses.CategoriesData;
import com.example.demoapp.ModelAttributeClasses.MyInteger;
import com.example.demoapp.Services.CategoriesService;
import com.example.demoapp.Services.NotesService;
import com.example.demoapp.repositories.CategoriesRepository;
import com.example.demoapp.repositories.NotesRepository;
import com.example.demoapp.repositories.SharedNotesRepositiory;
import com.example.demoapp.repositories.db.NotesEntityRepository;
import com.example.demoapp.repositories.db.SharednotesEntityRepository;
import com.example.demoapp.repositories.db.UsersEntityRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/Logged/Notes")
public class NotesController {
    @Autowired
    NotesService notesService;
    @Autowired
    CategoriesRepository categoriesRepository;

    @Autowired
    private HttpSession session;
    @Autowired
    CategoriesService categoriesService;
    @Autowired
    NotesEntityRepository notesEntityRepository;
    @Autowired
    NotesRepository notesRepository;
    @Autowired
    UsersEntityRepository usersEntityRepository;

    @Autowired
    SharednotesEntityRepository sharednotesEntityRepository;

    @Autowired
    SharedNotesRepositiory sharedNotesRepositiory;

    @GetMapping("/GetToDisplayingNotes")
    public String displayNotes(){
        return "redirect:/Logged/Notes/DisplayNotes";
    }

    @PostMapping("/DisplayNotes")
    public String sortNotes(@RequestParam("sortOrder") String sortOrder, HttpServletResponse response) {

        Cookie sortCookie = new Cookie("sortOrder", sortOrder);
        sortCookie.setPath("/");
        response.addCookie(sortCookie);

        return "redirect:/Logged/Notes/DisplayNotes";
    }

    @GetMapping("/DisplayNotes/SharedLinks/{noteLink}")
    public String displayNote(@PathVariable String noteLink, Model model) {
        NotesEntity note = notesService.getNoteByLink(noteLink);

        if (note == null) {
            return "error";
        }

        model.addAttribute("note", note);


        return "note";
    }


    @GetMapping("/DisplayNotes")
    public String getNote(Model model) {
        List<NotesEntity> notesEntityList = notesService.updateNoteCategoryNames(notesService.getAllNotes(),categoriesService.getAllCategoriesDB());
        model.addAttribute("notes", notesEntityList);
        List<String> categories = notesService.getCategoriesByPopularity();
        model.addAttribute("categories", categories);
        return "notes";
    }

    @PostMapping("/FilterByCategory")
    public String FilterByCategory(@RequestParam("category") String category, HttpServletResponse response) {

        if (category.isEmpty()) {
            Cookie FilterByCategory = new Cookie("category", "All");
            FilterByCategory.setPath("/");
            response.addCookie(FilterByCategory);
        }else{
            Cookie FilterByCategory = new Cookie("category", category);
            FilterByCategory.setPath("/");
            response.addCookie(FilterByCategory);
        }

        return "redirect:/Logged/Notes/DisplayNotes";
    }

    @PostMapping("/FilterByDate")
    public String FilterByDate(@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,Model model, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        if (startDate.isEmpty()) {
            Cookie FilterBystartDate = new Cookie("startDate", "none");
            FilterBystartDate.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(FilterBystartDate);
        } else if(notesService.isDateValid(startDate)){
            Cookie FilterBystartDate = new Cookie("startDate", startDate);
            FilterBystartDate.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(FilterBystartDate);
        } else redirectAttributes.addFlashAttribute("errorDate", "Date is invalid.");
        if(endDate.isEmpty()){
            Cookie FilterByendDate = new Cookie("endDate", "none");
            FilterByendDate.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(FilterByendDate);

        } else if(notesService.isDateValid(endDate)){
            Cookie FilterByendDate = new Cookie("endDate", endDate);
            FilterByendDate.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(FilterByendDate);
        } else redirectAttributes.addFlashAttribute("errorDate", "Date is invalid.");

        return "redirect:/Logged/Notes/DisplayNotes";
    }

    @GetMapping("/GetToAddingNewNote")
    public String createNewNote(){
        return "redirect:/Logged/Notes/AddNote";
    }

    @GetMapping("/AddNote")
    public String showNewNoteForm(Model model){
        List<String> categories = categoriesService.combineBothRepositories();

        CategoriesData categoriesData = new CategoriesData();
        categoriesData.setCategoriesNames(categories);

        model.addAttribute("categoriesData", categoriesData);
        model.addAttribute("note", new Note());
        return "newNote";
    }

    @PostMapping("/AddNote")
    public String showNewNoteForm(@Valid @ModelAttribute("note") Note note, BindingResult result, @ModelAttribute("categoriesData") CategoriesData categoriesData, Model model){
        if(result.hasErrors()){
            result.getAllErrors().forEach(el -> System.out.println(el));

            List<String> categories = categoriesService.combineBothRepositories();
            categoriesData.setCategoriesNames(categories);
            model.addAttribute("categoriesData", categoriesData);

            return "newNote";
        }
        notesService.addNewNote(note);
        return "redirect:/Logged/MainMenu";
    }

    @GetMapping("/GetToDeletingNote/{noteIndex}")
    public String deleteNote(@PathVariable("noteIndex") int noteIndex) {
        notesService.deleteNote(noteIndex);
        return "redirect:/Logged/Notes/GetToDisplayingNotes";
    }

    @GetMapping("/GetToSharingNote/{noteIndex}")
    public String getToSharingNote(@PathVariable("noteIndex") int noteIndex, Model model) {
        MyInteger noteIndex2 = new MyInteger();
        noteIndex2.setValue(noteIndex);
        NotesEntity note = notesService.getNotebyId(noteIndex2.getValue());
        List<UsersEntity> users = usersEntityRepository.findAll();
        model.addAttribute("note", note);
        model.addAttribute("users",users);
        return "sharingNote";
    }




    @GetMapping("/GetToEditingNote/{noteIndex}")
    public String editNote(@PathVariable("noteIndex") int noteIndex, RedirectAttributes redirectAttributes){
        MyInteger noteIndex2 = new MyInteger();
        noteIndex2.setValue(noteIndex);
        redirectAttributes.addFlashAttribute("noteIndex", noteIndex2);
        return "redirect:/Logged/Notes/EditNote";
    }

    @GetMapping("/EditNote")
    public String showEditNoteForm(@ModelAttribute("noteIndex") MyInteger noteIndex, Model model){
        List<String> categories = categoriesService.combineBothRepositories();

        CategoriesData categoriesData = new CategoriesData();
        categoriesData.setCategoriesNames(categories);

        model.addAttribute("categoriesData", categoriesData);
        NotesEntity note = new NotesEntity();
        if(notesService.findNoteById(notesRepository.getNotesList(),noteIndex.getValue())!= null){
            note = notesService.findNoteById(notesRepository.getNotesList(),noteIndex.getValue());
        }else if(notesEntityRepository.findByNoteId(noteIndex.getValue())!= null){
            note = notesEntityRepository.findByNoteId(noteIndex.getValue());
        }
        model.addAttribute("note", note);
        model.addAttribute("editedNote", new Note());
        model.addAttribute("noteIndex", noteIndex);
        return "editNote";
    }

    @PostMapping("/EditNote")
    public String showEditNoteForm(@ModelAttribute("editedNote") Note editedNote, BindingResult result, @ModelAttribute("noteIndex") MyInteger noteIndex, @ModelAttribute("categoriesData") CategoriesData categoriesData, Model model){
        if(!editedNote.getTitle().isEmpty()){
            boolean isTitleValid_SpaceCharacter = !editedNote.getTitle().contains(" ");
            boolean isTitleNameValid_Size = editedNote.getTitle().length() >= 3 && editedNote.getTitle().length() <= 20;
            if(!isTitleValid_SpaceCharacter){
                result.rejectValue("title", "err.string.TitleNoSpaceCharacter");
            }
            if(!isTitleNameValid_Size){
                result.rejectValue("title", "err.string.TitleLength");
            }
        }
        if(!editedNote.getContent().isEmpty()){
            boolean isContentValid_Size = editedNote.getContent().length() >= 5 && editedNote.getContent().length() <= 500;
            if(!isContentValid_Size){
                result.rejectValue("content", "err.string.ContentLength");
            }
        }
        if(!editedNote.getNoteLink().isEmpty()){
            boolean isNoteLinkValid_SpaceCharacter = !editedNote.getNoteLink().contains(" ");
            if(!isNoteLinkValid_SpaceCharacter){
                result.rejectValue("noteLink", "err.string.NoteLinkNoSpaceCharacter");
            }
        }
        if(result.hasErrors()){

            NotesEntity note = new NotesEntity();
            if(notesService.findNoteById(notesRepository.getNotesList(),noteIndex.getValue())!= null){
                note = notesService.findNoteById(notesRepository.getNotesList(),noteIndex.getValue());
            }else if(notesEntityRepository.findByNoteId(noteIndex.getValue())!= null) note = notesEntityRepository.findByNoteId(noteIndex.getValue());
            model.addAttribute("note", note);
            result.getAllErrors().forEach(el -> System.out.println(el));

            List<String> categories = categoriesService.combineBothRepositories();
            categoriesData.setCategoriesNames(categories);
            model.addAttribute("categoriesData", categoriesData);

            return "editNote";
        }
        notesService.editNote(noteIndex, editedNote);
        return "redirect:/Logged/Notes/GetToDisplayingNotes";
    }

    @GetMapping("/ShareNoteToUser")
    public String ShareNoteToUser(@RequestParam("noteId") Integer noteId, @RequestParam("userId") Integer userId) {
        SharednotesEntity sharednotesEntity = new SharednotesEntity();
        sharednotesEntity.setNoteId(noteId);
        sharednotesEntity.setUserId(userId);
        sharednotesEntity.setUsersByUserId(usersEntityRepository.findByUserId(userId));
        sharednotesEntityRepository.save(sharednotesEntity);
        List<SharednotesEntity> sharednotesEntityList = sharedNotesRepositiory.getSharednotesEntityList();
        sharednotesEntityList.add(sharednotesEntity);
        sharedNotesRepositiory.setSharednotesEntityList(sharednotesEntityList);
        return "redirect:/Logged/Notes/GetToDisplayingNotes";
    }
    @GetMapping("/ShowSharedNotes")
    public String ShowSharedNotes(Model model) {
        List<NotesEntity> notesEntityList= notesService.findNotesBySharedNotes();
        model.addAttribute("notes",notesEntityList);

        return "ShowSharedNotes";
    }







}
