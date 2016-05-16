package com.xidian.yetwish.reading.framework.utils;

import android.os.Environment;

import com.google.common.collect.Ordering;
import com.xidian.yetwish.reading.framework.reader.ChapterDivider;
import com.xidian.yetwish.reading.framework.vo.NoteVo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Comparator;

/**
 * Created by Yetwish on 2016/5/10 0010.
 */
public class FileUtils {

    private FileUtils() {
        /* can not be instantiated */
        throw new UnsupportedOperationException("can not be instantiated");
    }

    //    private static final String ROOT_DIR = "/";
    public static final String SD_DIR = "/sdcard";

    private static final String APP_ROOT = "/Reading";

    private static final String NOTEBOOK_ROOT = "/NoteBook";


    private static final Comparator<File> COMPARATOR_FILE = new Comparator<File>() {
        @Override
        public int compare(File lhs, File rhs) {
            if (lhs.isDirectory() && rhs.isFile())
                return -1;
            if (lhs.isFile() && rhs.isDirectory())
                return 1;
            return lhs.getName().compareToIgnoreCase(rhs.getName());
        }
    };

    public static final Ordering<File> ORDERING_FILE = Ordering.from(COMPARATOR_FILE);


    private static String getNotebookDir() {
        return getStorageDir() + APP_ROOT + NOTEBOOK_ROOT;
    }

    public static String getStorageDir() {
        String dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            dir = Environment.getExternalStorageDirectory().getPath();
        else
            dir = SD_DIR;
        LogUtils.w(dir + " +  dir");
        return dir;
    }

    public static File getStorageRootFile() {
        String dir = getStorageDir();
        File file = null;
        try {
            file = new File(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static String getNoteBookPath(long noteBookId) {
        return getNotebookDir() + File.separator + noteBookId;
    }

    public static String getNoteFilePath(NoteVo note) {
        return getNoteBookPath(note.getNoteBookId()) + File.separator + note.getNoteId();
    }

    public static void saveNoteToFile(NoteVo note) {
        String dir = getNoteBookPath(note.getNoteBookId());
        File dirFile = new File(dir);
        String filePath = getNoteFilePath(note);
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

    public static boolean deleteNoteBooK(long noteBookId) {
        String dir = getNoteBookPath(noteBookId);
        File noteBookFile = new File(dir);
        if (!noteBookFile.exists() || !noteBookFile.isDirectory()) return false;
        File[] files = noteBookFile.listFiles();
        for (File file : files) {
            file.delete();
        }
        noteBookFile.delete();
        return true;
    }

    public static boolean deleteNote(NoteVo note) {
        String filePath = getNoteFilePath(note);
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) return false;
        return file.delete();
    }

    public static String getFileSize(File file) {
        long length = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (length < 1024) {
            fileSizeString = df.format((double) length) + "B";
        } else if (length < 1048576) {
            fileSizeString = df.format((double) length / 1024) + "K";
        } else if (length < 1073741824) {
            fileSizeString = df.format((double) length / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) length / 1073741824) + "G";
        }
        return fileSizeString;
    }

}
