package com.xidian.yetwish.reading.framework.vo;

import com.xidian.yetwish.reading.framework.database.generator.Note;
import com.xidian.yetwish.reading.framework.utils.BookUtils;

import java.io.File;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class NoteVo {

    private long noteId;
    private long noteBookId;
    private String name;
    private String filePath;


    public NoteVo(){
        this.noteId = BookUtils.generateSequenceId();
    }


    public NoteVo(Note note){
        this.noteId = note.getNoteId();
        this.noteBookId = note.getNoteBookId();
        this.name = note.getName();
        this.filePath = note.getPath();
    }

    public Note convertToDb(){
        Note note = new Note();
        note.setNoteId(noteId);
        note.setNoteBookId(noteBookId);
        note.setName(name);
        note.setPath(filePath);
        return note;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public long getNoteBookId() {
        return noteBookId;
    }

    public void setNoteBookId(long noteBookId) {
        this.noteBookId = noteBookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
