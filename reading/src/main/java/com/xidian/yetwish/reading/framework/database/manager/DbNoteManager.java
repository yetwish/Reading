package com.xidian.yetwish.reading.framework.database.manager;

import android.os.Handler;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.generator.Note;
import com.xidian.yetwish.reading.framework.database.generator.NoteDao;
import com.xidian.yetwish.reading.framework.vo.NoteVo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Created by Yetwish on 2016/4/25 0025.
 */
public class DbNoteManager {

    public Handler mHandler;

    public DbNoteManager(Handler handler) {
        this.mHandler = handler;
    }


    public void refresh(final ImmutableList<NoteVo> uploadList) {
        if (uploadList == null || uploadList.size() == 0) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    List<Note> dbList = new ArrayList<Note>(uploadList.size());
                    for (NoteVo note : uploadList) {
                        dbList.add(note.convertToDb());
                    }
                    manager.getDaoSession().getNoteDao().insertOrReplaceInTx(dbList);
                }
            }
        });
    }

    public void refresh(final NoteVo upload) {
        if (upload == null) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getNoteDao().insertOrReplaceInTx(upload.convertToDb());
                }
            }
        });
    }

    /**
     * todo
     */
    public ImmutableList<NoteVo> queryByBook(long noteBookId) {
        List<NoteVo> noteList = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            NoteDao dao = manager.getDaoSession().getNoteDao();
            Query<Note> query = dao.queryBuilder().where(NoteDao.Properties.NoteBookId.eq(noteBookId)).build();
            List<Note> dbList = query.list();
            if (dbList != null && dbList.size() > 0) {
                for (Note note : dbList) {
                    noteList.add(new NoteVo(note));
                }
            }
        }
        return ImmutableList.copyOf(noteList);
    }

    public ImmutableList<NoteVo> queryByNoteBook(long noteBookId) {
        List<NoteVo> noteList = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            NoteDao dao = manager.getDaoSession().getNoteDao();
            Query<Note> query = dao.queryBuilder().where(NoteDao.Properties.NoteBookId.eq(noteBookId)).build();
            List<Note> dbList = query.list();
            if (dbList != null && dbList.size() > 0) {
                for (Note note : dbList) {
                    noteList.add(new NoteVo(note));
                }
            }
        }
        return ImmutableList.copyOf(noteList);
    }
}

