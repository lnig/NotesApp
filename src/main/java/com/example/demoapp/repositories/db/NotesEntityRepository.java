package com.example.demoapp.repositories.db;

import com.example.demoapp.Data.db.NotesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface NotesEntityRepository extends JpaRepository<NotesEntity, Integer> {
    NotesEntity findByUserIdAndAndTitle(Integer userId, String title);
    Collection<NotesEntity> findByCategoryId(Integer categoryID);
    NotesEntity findByNoteId(Integer noteId);
    void deleteByNoteId(Integer noteId);
}