package com.aidingmao.database;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
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
		book.addIdProperty().autoincrement();
		Property bookId = book.addLongProperty("bookId").getProperty();
		book.addStringProperty("name");
		book.addStringProperty("author");
		book.addStringProperty("language");
		book.addFloatProperty("progress");
		book.addStringProperty("iconPath");
		book.addStringProperty("path");
		
		Index index = new Index();
		index.addProperty(bookId);
		index.makeUnique();
		
		book.addIndex(index);
	}
	
	private static void addNoteBook(Schema schema){
		Entity noteBook = schema.addEntity("NoteBook");
		noteBook.addIdProperty().autoincrement();
		Property noteBookId = noteBook.addLongProperty("NoteBookId").getProperty();
		Property bookId = noteBook.addLongProperty("bookId").getProperty();
		noteBook.addStringProperty("name");
		noteBook.addStringProperty("iconPath");
		noteBook.addIntProperty("size");
		noteBook.addStringProperty("intro");
		
		Index index = new Index();
		index.addProperty(noteBookId);
		index.addProperty(bookId);
		index.makeUnique();
		
		noteBook.addIndex(index);
	}

	private static void addNote(Schema schema){
		Entity note = schema.addEntity("NoteBook");
		note.addIdProperty().autoincrement();
		Property noteBookId = note.addLongProperty("NoteBookId").getProperty();
		Property noteId = note.addLongProperty("noteId").getProperty();
		note.addStringProperty("name");
		note.addStringProperty("path");
		
		Index index = new Index();
		index.addProperty(noteBookId);
		index.addProperty(noteId);
		index.makeUnique();
		
		note.addIndex(index);
	}
	
	
	private static void addChapter(Schema schema) {
		Entity chapter = schema.addEntity("Chapter");
		chapter.addIdProperty().autoincrement();
		Property chapterId = chapter.addLongProperty("chapterId").getProperty();
		Property bookId = chapter.addLongProperty("bookId").getProperty();
		chapter.addStringProperty("path");
		chapter.addStringProperty("name");
		chapter.addIntProperty("firstCharPosition");
		chapter.addIntProperty("lastCharPosition");
		chapter.addIntProperty("pageNumber");
		
		Index index = new Index();
		index.addProperty(chapterId);
		index.addProperty(bookId);
		index.makeUnique();
		
		chapter.addIndex(index);

	}

	private static void addPage(Schema schema) {
		Entity page = schema.addEntity("Page");
		page.addIdProperty().autoincrement();
		Property pageId = page.addLongProperty("pageId").getProperty();
		Property chapterId = page.addLongProperty("chapterId").getProperty();
		page.addLongProperty("firstCharPosition");
		page.addLongProperty("lastCharPosition");
		page.addStringProperty("path");
		page.addStringProperty("content");

		Index index = new Index();
		index.addProperty(pageId);
		index.addProperty(chapterId);
		index.makeUnique();

		page.addIndex(index);
	}


}
