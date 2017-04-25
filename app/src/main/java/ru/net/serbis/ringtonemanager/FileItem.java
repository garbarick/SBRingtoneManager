package ru.net.serbis.ringtonemanager;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * SEBY0408
 */
public class FileItem implements Comparable<FileItem>
{
    private Activity context;
    private Type type;
    private File file;

    public FileItem(Activity context, Type type, File file)
    {
        this.context = context;
        this.type = type;
        this.file = file;
    }

    public File getFile()
    {
        return file;
    }

    public String getPath()
    {
        return file != null ? file.getAbsolutePath() : "";
    }

    public String getLocation()
    {
        return file != null ? file.getParentFile().getAbsolutePath() : "";
    }

    public long getLength()
    {
        return file != null ? file.length() : 0;
    }

    public String getName()
    {
        if (file == null)
        {
            return context.getResources().getString(R.string.silent);
        }
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0)
        {
            return fileName.substring(0, i);
        }
        return "";
    }

    public Uri getUri()
    {
        if (file == null)
        {
            return null;
        }
        String path = getPath();

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, path);
        values.put(MediaStore.MediaColumns.TITLE, getName());
        values.put(MediaStore.MediaColumns.SIZE, getLength());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/" + getExt());
        values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE, Type.Ringtone.equals(type));
        values.put(MediaStore.Audio.AudioColumns.IS_NOTIFICATION, Type.Notification.equals(type));
        values.put(MediaStore.Audio.AudioColumns.IS_ALARM, Type.Alarm.equals(type));
        values.put(MediaStore.Audio.AudioColumns.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(path);
        context.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + path + "\"", null);
        return context.getContentResolver().insert(uri, values);
    }

    private String getExt()
    {
        if (file != null)
        {
            String fileName = file.getName();
            int i = fileName.lastIndexOf('.');
            if (i > 0)
            {
                return fileName.substring(i + 1).toLowerCase();
            }
        }
        return "";
    }

    @Override
    public int compareTo(FileItem o)
    {
        File thatFile = o.file;
        if (file == null && thatFile == null)
        {
            return 0;
        }
        else if (file == null)
        {
            return -1;
        }
        else if (thatFile == null)
        {
            return 1;
        }
        return file.compareTo(thatFile);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileItem fileItem = (FileItem) o;
        return !(file != null ? !file.equals(fileItem.file) : fileItem.file != null);
    }

    @Override
    public int hashCode()
    {
        return file != null ? file.hashCode() : 0;
    }
}
