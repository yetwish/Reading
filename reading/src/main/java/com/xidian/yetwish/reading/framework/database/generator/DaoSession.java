package com.xidian.yetwish.reading.framework.database.generator;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.xidian.yetwish.reading.framework.database.generator.Book;
import com.xidian.yetwish.reading.framework.database.generator.NoteBook;
import com.xidian.yetwish.reading.framework.database.generator.Note;
import com.xidian.yetwish.reading.framework.database.generator.Chapter;
import com.xidian.yetwish.reading.framework.database.generator.Page;
import com.xidian.yetwish.reading.framework.database.generator.Bookmark;

import com.xidian.yetwish.reading.framework.database.generator.BookDao;
import com.xidian.yetwish.reading.framework.database.generator.NoteBookDao;
import com.xidian.yetwish.reading.framework.database.generator.NoteDao;
import com.xidian.yetwish.reading.framework.database.generator.ChapterDao;
import com.xidian.yetwish.reading.framework.database.generator.PageDao;
import com.xidian.yetwish.reading.framework.database.generator.BookmarkDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bookDaoConfig;
    private final DaoConfig noteBookDaoConfig;
    private final DaoConfig noteDaoConfig;
    private final DaoConfig chapterDaoConfig;
    private final DaoConfig pageDaoConfig;
    private final DaoConfig bookmarkDaoConfig;

    private final BookDao bookDao;
    private final NoteBookDao noteBookDao;
    private final NoteDao noteDao;
    private final ChapterDao chapterDao;
    private final PageDao pageDao;
    private final BookmarkDao bookmarkDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bookDaoConfig = daoConfigMap.get(BookDao.class).clone();
        bookDaoConfig.initIdentityScope(type);

        noteBookDaoConfig = daoConfigMap.get(NoteBookDao.class).clone();
        noteBookDaoConfig.initIdentityScope(type);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        chapterDaoConfig = daoConfigMap.get(ChapterDao.class).clone();
        chapterDaoConfig.initIdentityScope(type);

        pageDaoConfig = daoConfigMap.get(PageDao.class).clone();
        pageDaoConfig.initIdentityScope(type);

        bookmarkDaoConfig = daoConfigMap.get(BookmarkDao.class).clone();
        bookmarkDaoConfig.initIdentityScope(type);

        bookDao = new BookDao(bookDaoConfig, this);
        noteBookDao = new NoteBookDao(noteBookDaoConfig, this);
        noteDao = new NoteDao(noteDaoConfig, this);
        chapterDao = new ChapterDao(chapterDaoConfig, this);
        pageDao = new PageDao(pageDaoConfig, this);
        bookmarkDao = new BookmarkDao(bookmarkDaoConfig, this);

        registerDao(Book.class, bookDao);
        registerDao(NoteBook.class, noteBookDao);
        registerDao(Note.class, noteDao);
        registerDao(Chapter.class, chapterDao);
        registerDao(Page.class, pageDao);
        registerDao(Bookmark.class, bookmarkDao);
    }
    
    public void clear() {
        bookDaoConfig.getIdentityScope().clear();
        noteBookDaoConfig.getIdentityScope().clear();
        noteDaoConfig.getIdentityScope().clear();
        chapterDaoConfig.getIdentityScope().clear();
        pageDaoConfig.getIdentityScope().clear();
        bookmarkDaoConfig.getIdentityScope().clear();
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public NoteBookDao getNoteBookDao() {
        return noteBookDao;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public ChapterDao getChapterDao() {
        return chapterDao;
    }

    public PageDao getPageDao() {
        return pageDao;
    }

    public BookmarkDao getBookmarkDao() {
        return bookmarkDao;
    }

}
