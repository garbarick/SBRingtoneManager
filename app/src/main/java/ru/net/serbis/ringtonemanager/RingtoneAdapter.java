package ru.net.serbis.ringtonemanager;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * SEBY0408
 */
public class RingtoneAdapter extends ArrayAdapter<RingtoneItem>
{
    private Activity context;

    public RingtoneAdapter(Activity context)
    {
        super(context, R.layout.ringtone);
        this.context = context;
        initItems();
    }

    private void initItems()
    {
        clear();
        addItem(Type.Ringtone);
        addItem(Type.Notification);
        addItem(Type.Alarm);
    }

    private void addItem(Type type)
    {
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context, type.getCode());
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        add(new RingtoneItem(type, ringtone != null ? ringtone.getTitle(context) : context.getResources().getString(R.string.silent)));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = context.getLayoutInflater().inflate(R.layout.ringtone, null, true);
        }
        final RingtoneItem ringtone = getItem(position);

        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(ringtone.getName());

        TextView description = (TextView) view.findViewById(R.id.type);
        description.setText(ringtone.getType().toString());

        return view;
    }

    public void setRingtone(RingtoneItem ringtone, String name, Uri uri)
    {
        RingtoneManager.setActualDefaultRingtoneUri(context, ringtone.getType().getCode(), uri);
        ringtone.setName(name);
        notifyDataSetChanged();
    }
}
