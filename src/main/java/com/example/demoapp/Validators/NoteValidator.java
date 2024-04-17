package com.example.demoapp.Validators;

import com.example.demoapp.Data.Note;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoteValidator implements ConstraintValidator<NoteValidation, Note> {

    @Override
    public boolean isValid(Note note, ConstraintValidatorContext cxt) {
        String title = note.getTitle();
        String content = note.getContent();
        String noteLink = note.getNoteLink();

        boolean isTitleValid_SpaceCharacter = !title.contains(" ");
        boolean isTitleNameValid_Size = title.length() >= 3 && title.length() <= 20;

        boolean isContentValid_Size = content.length() >= 5 && content.length() <= 500;

        boolean isNoteLinkValid_SpaceCharacter = !noteLink.contains(" ");


        if (!isTitleValid_SpaceCharacter) {
            cxt.buildConstraintViolationWithTemplate("{err.string.TitleNoSpaceCharacter}")
                    .addPropertyNode("title")
                    .addConstraintViolation();
        }

        if (!isNoteLinkValid_SpaceCharacter) {
            cxt.buildConstraintViolationWithTemplate("{err.string.NoteLinkNoSpaceCharacter}")
                    .addPropertyNode("noteLink")
                    .addConstraintViolation();
        }

        if (!isTitleNameValid_Size) {
            cxt.buildConstraintViolationWithTemplate("{err.string.TitleLength}")
                    .addPropertyNode("title")
                    .addConstraintViolation();
        }

        if (!isContentValid_Size) {
            cxt.buildConstraintViolationWithTemplate("{err.string.ContentLength}")
                    .addPropertyNode("content")
                    .addConstraintViolation();
        }

        return  isTitleValid_SpaceCharacter && isTitleNameValid_Size && isContentValid_Size && isNoteLinkValid_SpaceCharacter;
    }
}
