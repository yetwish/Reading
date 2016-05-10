package com.xidian.yetwish.reading.framework.utils;

import android.os.Environment;

import com.xidian.yetwish.reading.framework.database.generator.Note;
import com.xidian.yetwish.reading.framework.reader.ChapterDivider;
import com.xidian.yetwish.reading.framework.reader.ChapterFactory;
import com.xidian.yetwish.reading.framework.vo.NoteVo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by Yetwish on 2016/5/10 0010.
 */
public class FileUtils {

    private FileUtils() {
        /* can not be instantiated */
        throw new UnsupportedOperationException("can not be instantiated");
    }

    //    private static final String ROOT_DIR = "/";
    public static final String SD_ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    private static final String APP_ROOT = SD_ROOT_DIR + "/Reading";

    private static final String NOTEBOOK_ROOT = APP_ROOT + "/NoteBook";

    public static String getNoteFilePath(NoteVo note) {
        String dir = NOTEBOOK_ROOT + File.separator + note.getNoteBookId() + File.separator;
        String filePath = dir + note.getNoteId();
        return filePath;
    }


    public static void saveNoteToFile(NoteVo note) {
        String dir = NOTEBOOK_ROOT + File.separator + note.getNoteBookId();
        File dirFile = new File(dir);
        String filePath = dir + File.separator + note.getNoteId();
        File file = new File(filePath);
        BufferedWriter writer = null;
        try {
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(note.getContent());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadNoteFromFile(NoteVo note) {
        File file = new File(note.getFilePath());
        if (!file.exists()) return;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), ChapterDivider.CHARSET_UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            note.setContent(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
