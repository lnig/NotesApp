package com.example.demoapp.repositories;

import com.example.demoapp.Data.db.SharednotesEntity;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
@Data
public class SharedNotesRepositiory {
    List<SharednotesEntity> sharednotesEntityList = new ArrayList<>();
}
