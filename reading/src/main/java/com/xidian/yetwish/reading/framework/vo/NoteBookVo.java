package com.xidian.yetwish.reading.framework.vo;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class NoteBookVo {

    private String noteBookId;
    private String bookId;
    private String noteBookName;
    private String iconPath;
    private int iconResId;
    private int noteNumber;
    private String introduction;

    public String getNoteBookId() {
        return noteBookId;
    }

    public void setNoteBookId(String noteBookId) {
        this.noteBookId = noteBookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getNoteBookName() {
        return noteBookName;
    }

    public void setNoteBookName(String noteBookName) {
        this.noteBookName = noteBookName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
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
