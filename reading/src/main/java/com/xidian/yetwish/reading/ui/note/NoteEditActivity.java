package com.xidian.yetwish.reading.ui.note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.exception.IllegalIntentDataException;
import com.xidian.yetwish.reading.framework.utils.FileUtils;
import com.xidian.yetwish.reading.framework.utils.SharedPreferencesUtils;
import com.xidian.yetwish.reading.framework.vo.NoteVo;
import com.xidian.yetwish.reading.ui.ToolbarActivity;

import java.io.Serializable;

/**
 * todo 监听enter
 * Created by Yetwish on 2016/5/10 0010.
 */
public class NoteEditActivity extends ToolbarActivity {


    public static final void startActivity(Context context, long noteBookId) {
        Intent intent = new Intent(context, NoteEditActivity.class);
        intent.putExtra(SharedPreferencesUtils.EXTRA_NOTEBOOK_ID, noteBookId);
        context.startActivity(intent);
    }


    public static final void startActivityForResult(Activity context, NoteVo note) {
        Intent intent = new Intent(context, NoteEditActivity.class);
        intent.putExtra(SharedPreferencesUtils.EXTRA_NOTE, note);
        context.startActivityForResult(intent,SharedPreferencesUtils.REQUEST_CODE_NOTE_EDIT);
    }

    private NoteVo mNote;

    private MenuItem menuFinish;

    private EditText etTitle;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.note_detail);
        initView();
        initData();
    }

    private void initView() {
        etTitle = (EditText) findViewById(R.id.etNoteTitle);
        etContent = (EditText) findViewById(R.id.etNoteContent);
        etTitle.setVisibility(View.VISIBLE);
        etContent.addTextChangedListener(mTextWatcher);
        etTitle.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateMenuFinishStatus();
        }
    };

    private void initData() {
        final Serializable data = getIntent().getSerializableExtra(SharedPreferencesUtils.EXTRA_NOTE);
        if (null == data) {
            //创建笔记
            final long noteBookId = getIntent().getLongExtra(SharedPreferencesUtils.EXTRA_NOTEBOOK_ID, 0);
            if (noteBookId == 0)
                throw new IllegalIntentDataException(this.getClass());
            mNote = new NoteVo(noteBookId);
            getSupportActionBar().setTitle(getString(R.string.note_create));

        } else {//编辑笔记
            if (!(data instanceof NoteVo))
                throw new IllegalIntentDataException(NoteVo.class, data.getClass());
            mNote = (NoteVo) data;
            getSupportActionBar().setTitle(getString(R.string.note_edit));
            etTitle.setText(mNote.getName());
            etContent.setText(mNote.getContent());
        }
    }

    private void updateMenuFinishStatus(){
        if(menuFinish == null) return;
        if (!TextUtils.isEmpty(etTitle.getText()) && !TextUtils.isEmpty(etContent.getText()))
            menuFinish.setEnabled(true);
        else
            menuFinish.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_edit, menu);
        menuFinish = menu.getItem(0);
        updateMenuFinishStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_note_finish:
                mNote.setName(etTitle.getText().toString().trim());
                mNote.setContent(etContent.getText().toString());
                //保存noteVo
                FileUtils.saveNoteToFile(mNote);
                DatabaseManager.getsInstance().getNoteManager().refresh(mNote);
                if(etTitle.getText().equals(getString(R.string.note_edit))){
                    Intent intent = getIntent();
                    intent.putExtra(SharedPreferencesUtils.EXTRA_NOTE,mNote);
                    setResult(RESULT_OK,intent);
                }
                finish();
                break;
        }
        return true;
    }
}
