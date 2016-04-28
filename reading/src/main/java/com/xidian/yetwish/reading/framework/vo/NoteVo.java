package com.xidian.yetwish.reading.framework.vo;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class NoteVo {

    private String noteId;
    private String notBookId;
    private String name;
    private String filePath;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNotBookId() {
        return notBookId;
    }

    public void setNotBookId(String notBookId) {
        this.notBookId = notBookId;
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
