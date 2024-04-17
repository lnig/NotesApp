package com.example.demoapp.repositories;

import com.example.demoapp.Data.db.NotesEntity;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
public class NotesRepository {
    List<NotesEntity> NotesList = new ArrayList<>();
    List<NotesEntity> DeletedNotesList = new ArrayList<>();

    public NotesEntity getNoteByCategoryId(int categoryId) {

        for (NotesEntity note : NotesList) {
            if (note.getCategoriesByCategoryId().getCategoryId() == categoryId) {
                return note;
            }
        }

        return null;
    }

}
