package com.aidingmao.database;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DatabaseGenerator {

	public static final int DATABASE_VERSION = 1;

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(DATABASE_VERSION,
				"com.xidian.yetwish.reading.framework.database.generator");
		addBook(schema);
		addNoteBook(schema);
		addNote(schema);
		addChapter(schema);
		addPage(schema);
		new DaoGenerator().generateAll(schema, "../reading/src/main/java");
	}
	
	private static void addBook(Schema schema){
		Entity book = schema.addEntity("Book");
		book.addLongProperty("bookId").primaryKey();
		book.addStringProperty("name");
		book.addStringProperty("author");
		book.addStringProperty("language");
		book.addFloatProperty("progress");
		book.addStringProperty("iconPath");
		book.addIntProperty("iconResId");
		book.addStringProperty("path");
		book.addLongProperty("charNumber");
	}
	
	private static void addNoteBook(Schema schema){
		Entity noteBook = schema.addEntity("NoteBook");
		noteBook.addLongProperty("NoteBookId").primaryKey();
		noteBook.addLongProperty("bookId");
		noteBook.addStringProperty("name");
		noteBook.addStringProperty("iconPath");
		noteBook.addIntProperty("iconResId");
		noteBook.addIntProperty("noteNumber");
		noteBook.addStringProperty("intro");
	}

	private static void addNote(Schema schema){
		Entity note = schema.addEntity("Note");
		note.addLongProperty("noteId").primaryKey();
		note.addLongProperty("NoteBookId");
		note.addStringProperty("name");
		note.addStringProperty("path");
	}
	
	
	private static void addChapter(Schema schema) {
		Entity chapter = schema.addEntity("Chapter");
		chapter.addLongProperty("chapterId").primaryKey();
		chapter.addLongProperty("bookId");
		chapter.addStringProperty("path");
		chapter.addStringProperty("name");
		chapter.addLongProperty("firstCharPosition");
		chapter.addLongProperty("lastCharPosition");
		chapter.addIntProperty("pageNumber");

	}

	private static void addPage(Schema schema) {
		Entity page = schema.addEntity("Page");
		page.addLongProperty("pageId").primaryKey();
		page.addLongProperty("chapterId");
		page.addLongProperty("bookId");
		page.addLongProperty("firstCharPosition");
		page.addLongProperty("lastCharPosition");
		page.addStringProperty("path");
		page.addStringProperty("content");

	}


}
