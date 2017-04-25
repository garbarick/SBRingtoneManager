package ru.net.serbis.ringtonemanager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

/**
 * SEBY0408
 */
public class FileAdapter extends ArrayAdapter<FileItem>
{
    private Activity context;
    private Type type;
    private int checked = -1;

    public FileAdapter(Activity context, Type type, File selected, boolean silent)
    {
        super(context, R.layout.file);
        this.context = context;
        this.type = type;
        initItems(new FileItem(context, type, selected), silent);
    }

    private void initItems(FileItem selected, boolean silent)
    {
        if (silent)
        {
            add(new FileItem(context, type, null));
        }
        for (File dir : type.getLocations())
        {
            findFile(dir);
        }
        sort(new Comparator<FileItem>()
        {
            @Override
            public int compare(FileItem lhs, FileItem rhs)
            {
                return lhs.compareTo(rhs);
            }
        });

        checked = getPosition(selected);
    }

    private void findFile(File dir)
    {
        dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File file)
            {
                if (file.isDirectory())
                {
                    findFile(file);
                }
                else
                {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".mp3") ||
                        name.endsWith(".m4a") ||
                        name.endsWith(".wav") ||
                        name.endsWith(".ogg"))
                    {
                        add(new FileItem(context, type, file));
                    }
                }
                return false;
            }
        });
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = context.getLayoutInflater().inflate(R.layout.file, null, true);
        }

        FileItem fileItem = getItem(position);

        ((TextView) view.findViewById(R.id.dir)).setText(fileItem.getLocation());
        ((TextView) view.findViewById(R.id.file)).setText(fileItem.getName());

        RadioButton check = (RadioButton) view.findViewById(R.id.check);
        if (checked == position)
        {
            check.setChecked(true);
            check.setVisibility(View.VISIBLE);
        }
        else
        {
            check.setChecked(false);
            check.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public void setChecked(int checked)
    {
        this.checked = checked;
    }

    public int getChecked()
    {
        return checked;
    }
}
