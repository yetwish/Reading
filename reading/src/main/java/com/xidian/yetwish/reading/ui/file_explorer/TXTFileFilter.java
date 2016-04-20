package com.xidian.yetwish.reading.ui.file_explorer;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * Created by Yetwish on 2016/4/20 0020.
 */
public class TXTFileFilter implements FileFilter {

    @Override
    public boolean accept(File file) {

        if (file.isDirectory())
            return true;

        if (file.getName().endsWith(".txt"))
            return true;
        else return false;
    }
}