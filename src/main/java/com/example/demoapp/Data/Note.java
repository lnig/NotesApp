package com.example.demoapp.Data;

import com.example.demoapp.Validators.NoteValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoteValidation
@NoArgsConstructor
public class Note {
    private String title;
    private String content;
    private String noteLink;
    private String creationDate;
    private String categoryName;
}
