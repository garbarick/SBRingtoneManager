package ru.net.serbis.ringtonemanager;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Main extends android.app.Activity
{
    private RingtoneAdapter ringtoneAdapter;
    private RingtoneItem ringtone;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initRingtones();
    }

    private void initRingtones()
    {
        ListView ringtones = (ListView) findViewById(R.id.ringtones);
        ringtoneAdapter = new RingtoneAdapter(this);
        ringtones.setAdapter(ringtoneAdapter);
        ringtones.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View itemClicked, int position, long id)
            {
                Intent intent = new Intent(Main.this, FileSelector.class);
                intent.setAction(FileSelector.FILE_PICKER);
                ringtone = ringtoneAdapter.getItem(position);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, ringtone.getType().getCode());
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
                Main.this.startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (RESULT_OK == resultCode)
        {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            String name = intent.getStringExtra(FileSelector.NAME);
            ringtoneAdapter.setRingtone(ringtone, name, uri);
        }
    }
}
