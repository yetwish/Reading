package com.xidian.yetwish.reading.framework.vo;

import com.xidian.yetwish.reading.framework.database.generator.Note;
import com.xidian.yetwish.reading.framework.utils.BookUtils;
import com.xidian.yetwish.reading.framework.utils.FileUtils;
import com.xidian.yetwish.reading.framework.utils.LogUtils;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class NoteVo implements Serializable {

    private long noteId;
    private long noteBookId;
    private String name;
    private String content; //本地数据
    private String filePath;


    public NoteVo(long noteBookId) {
        this.noteId = BookUtils.generateSequenceId();
        this.noteBookId = noteBookId;
        this.filePath = FileUtils.getNoteFilePath(this);
    }


    public NoteVo(Note note) {
        this.noteId = note.getNoteId();
        this.noteBookId = note.getNoteBookId();
        this.name = note.getName();
        this.filePath = note.getPath();
        FileUtils.loadNoteFromFile(this);
    }

    public Note convertToDb() {
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


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
