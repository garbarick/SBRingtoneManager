package ru.net.serbis.ringtonemanager;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import java.io.File;

/**
 * SEBY0408
 */
public class FileSelector extends android.app.Activity
{
    public static String FILE_PICKER = "ru.net.serbis.ringtonemanager.FileSelector.FILE_PICKER";
    public static String NAME = "ru.net.serbis.ringtonemanager.FileSelector.NAME";

    private MediaPlayer player;
    private Button select;
    private FileItem selected;
    private Type type;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_selector);
        setResult(RESULT_CANCELED);

        initType();
        initTitle();
        initSelect();
        initFiles();
    }

    private void initType()
    {
        Intent intent = getIntent();
        type = Type.getType(intent.getIntExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, -1));
        if (type == null)
        {
            finish();
        }
    }

    private void initTitle()
    {
        Intent intent = getIntent();
        String title = intent.getStringExtra(RingtoneManager.EXTRA_RINGTONE_TITLE);
        if (title == null || title.length() == 0)
        {
            title = getResources().getString(R.string.select_file);
        }
        setTitle(String.format(title, type.toString()));
    }

    private void initFiles()
    {
        Intent intent = getIntent();
        boolean showSilent = intent.getBooleanExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        ListView files = (ListView) findViewById(R.id.files);
        final FileAdapter adapter = new FileAdapter(this, type, getFile(), showSilent);
        files.setAdapter(adapter);
        runForScroll(100, files, adapter.getChecked());
        files.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View itemClicked, int position, long id)
            {
                itemClicked.setSelected(true);
                selected = (FileItem) parent.getItemAtPosition(position);
                select.setEnabled(true);
                adapter.setChecked(position);
                adapter.notifyDataSetChanged();

                stop();
                play(selected);
            }
        });
    }

    public void runForScroll(long time, final ListView listView, final int position)
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            public void run()
            {
                try
                {
                    listView.smoothScrollToPosition(position);
                }
                catch (Exception ignored)
                {
                }
            }
        }, time);
    }

    private File getFile()
    {
        String path = getRealPathFromURI(RingtoneManager.getActualDefaultRingtoneUri(this, type.getCode()));
        if (path != null)
        {
            return new File(path);
        }
        return null;
    }

    private String getRealPathFromURI(Uri uri)
    {
        if (uri == null)
        {
            return null;
        }
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null)
        {
            int idx = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
            if (cursor.moveToFirst())
            {
                return cursor.getString(idx);
            }
        }
        else
        {
            return uri.getPath();
        }
        return null;
    }

    private void initSelect()
    {
        select = (Button) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                stop();

                Intent intent = new Intent(getIntent());
                intent.putExtra(NAME, selected.getName());
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, selected.getUri());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void play(FileItem fileItem)
    {
        try
        {
            File file = fileItem.getFile();
            if (file != null)
            {
                player = new MediaPlayer();
                player.setDataSource(file.getAbsolutePath());
                player.prepare();
                player.start();
            }
        }
        catch (Throwable e)
        {
            Log.error(this, "Error on play", e);
        }
    }

    private void stop()
    {
        try
        {
            if (player != null)
            {
                player.stop();
                player.release();
                player = null;
            }
        }
        catch (Throwable e)
        {
            Log.error(this, "Error on stop", e);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        stop();
    }
}
