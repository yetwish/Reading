package com.xidian.yetwish.reading.framework.database.generator;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table NOTE_BOOK.
 */
public class NoteBook {

    private Long NoteBookId;
    private Long bookId;
    private String name;
    private Integer iconResId;
    private Integer noteNumber;
    private String intro;

    public NoteBook() {
    }

    public NoteBook(Long NoteBookId) {
        this.NoteBookId = NoteBookId;
    }

    public NoteBook(Long NoteBookId, Long bookId, String name, Integer iconResId, Integer noteNumber, String intro) {
        this.NoteBookId = NoteBookId;
        this.bookId = bookId;
        this.name = name;
        this.iconResId = iconResId;
        this.noteNumber = noteNumber;
        this.intro = intro;
    }

    public Long getNoteBookId() {
        return NoteBookId;
    }

    public void setNoteBookId(Long NoteBookId) {
        this.NoteBookId = NoteBookId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIconResId() {
        return iconResId;
    }

    public void setIconResId(Integer iconResId) {
        this.iconResId = iconResId;
    }

    public Integer getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(Integer noteNumber) {
        this.noteNumber = noteNumber;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

}
