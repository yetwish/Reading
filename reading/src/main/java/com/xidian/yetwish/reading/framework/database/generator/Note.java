package com.xidian.yetwish.reading.framework.database.generator;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table NOTE.
 */
public class Note {

    private Long noteId;
    private Long NoteBookId;
    private String name;
    private String path;

    public Note() {
    }

    public Note(Long noteId) {
        this.noteId = noteId;
    }

    public Note(Long noteId, Long NoteBookId, String name, String path) {
        this.noteId = noteId;
        this.NoteBookId = NoteBookId;
        this.name = name;
        this.path = path;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getNoteBookId() {
        return NoteBookId;
    }

    public void setNoteBookId(Long NoteBookId) {
        this.NoteBookId = NoteBookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
