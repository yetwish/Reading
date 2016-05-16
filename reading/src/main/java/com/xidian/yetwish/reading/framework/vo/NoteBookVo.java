package com.xidian.yetwish.reading.framework.vo;


import com.xidian.yetwish.reading.framework.database.generator.NoteBook;
import com.xidian.yetwish.reading.framework.utils.BookUtils;

import java.io.Serializable;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class NoteBookVo implements Serializable {

    private long noteBookId;
    private long bookId;
    private String name;
    private int iconResId;
    private int noteNumber;
    private String introduction;

    public NoteBookVo(String name){
        this.noteBookId = BookUtils.generateSequenceId();
        this.name = name;
    }

    public NoteBookVo(NoteBook noteBook){
        this.noteBookId = noteBook.getNoteBookId();
        this.bookId = noteBook.getBookId();
        this.name = noteBook.getName();
        this.noteNumber = noteBook.getNoteNumber();
        this.introduction = noteBook.getIntro();
        this.iconResId = noteBook.getIconResId();
    }

    public NoteBook convertToDb(){
        NoteBook noteBook = new NoteBook();
        noteBook.setBookId(bookId);
        noteBook.setNoteBookId(noteBookId);
        noteBook.setName(name);
        noteBook.setNoteNumber(noteNumber);
        noteBook.setIntro(introduction);
        noteBook.setIconResId(iconResId);
        return noteBook;
    }


    public long getNoteBookId() {
        return noteBookId;
    }

    public void setNoteBookId(long noteBookId) {
        this.noteBookId = noteBookId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(int noteNumber) {
        this.noteNumber = noteNumber;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
