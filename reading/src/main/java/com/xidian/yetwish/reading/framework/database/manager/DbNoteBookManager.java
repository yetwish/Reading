package com.xidian.yetwish.reading.framework.database.manager;

import android.os.Handler;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.generator.NoteBook;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.vo.NoteBookVo;
import com.xidian.yetwish.reading.framework.vo.NoteVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yetwish on 2016/4/25 0025.
 */
public class DbNoteBookManager {

    public Handler mHandler;

    public static final String NOTE_BOOK_NAME = "杂了个记";

    public DbNoteBookManager(Handler handler) {
        this.mHandler = handler;
    }

    public void refresh(final NoteBookVo upload) {
        if (upload == null) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getNoteBookDao().insertOrReplaceInTx(upload.convertToDb());
                }
            }
        });
    }

    public void refresh(final ImmutableList<NoteBookVo> uploadList) {
        if (uploadList == null || uploadList.size() == 0) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    List<NoteBook> dbList = new ArrayList<NoteBook>();
                    for (NoteBookVo upload : uploadList) {
                        dbList.add(upload.convertToDb());
                    }
                    manager.getDaoSession().getNoteBookDao().insertOrReplaceInTx(dbList);
                }
            }
        });
    }

    public ImmutableList<NoteBookVo> queryAll() {
        List<NoteBookVo> noteBookList = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            List<NoteBook> dbList = manager.getDaoSession().getNoteBookDao().loadAll();
            if (dbList != null && dbList.size() != 0) {
                for (NoteBook dbEntity : dbList) {
                    noteBookList.add(new NoteBookVo(dbEntity));
                }
            } else {//无notebook数据
                LogUtils.w("init noteBook");
                NoteBookVo noteBook = new NoteBookVo(NOTE_BOOK_NAME);
                refresh(noteBook);
                noteBookList.add(noteBook);
            }
        }
        return ImmutableList.copyOf(noteBookList);
    }
}
