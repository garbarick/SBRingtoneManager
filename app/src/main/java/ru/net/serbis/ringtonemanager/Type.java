package ru.net.serbis.ringtonemanager;

import android.media.RingtoneManager;
import android.os.Environment;

import java.io.File;
import java.util.*;

/**
 * SEBY0408
 */
public enum Type
{
    Ringtone(RingtoneManager.TYPE_RINGTONE, Arrays.asList(Environment.DIRECTORY_RINGTONES)),
    Notification(RingtoneManager.TYPE_NOTIFICATION, Arrays.asList(Environment.DIRECTORY_NOTIFICATIONS)),
    Alarm(RingtoneManager.TYPE_ALARM, Arrays.asList(Environment.DIRECTORY_ALARMS));

    private static final String AUDIO = "/system/media/audio";
    private static final Map<Integer, Type> TYPES = new HashMap<Integer, Type>()
    {
        {
            for (final Type type : Type.values())
            {
                put(type.code, type);
            }
        }
    };

    private int code;
    private List<String> locations;

    private Type(int code, List<String> locations)
    {
        this.code = code;
        this.locations = locations;
    }

    public int getCode()
    {
        return code;
    }

    public List<File> getLocations()
    {
        List<File> result = new ArrayList<File>();
        for (String location : locations)
        {
            File dir = Environment.getExternalStoragePublicDirectory(location);
            if (dir.isDirectory() && dir.exists())
            {
                result.add(dir);
            }
            dir = new File(AUDIO, location.toLowerCase());
            if (dir.isDirectory() && dir.exists())
            {
                result.add(dir);
            }
        }
        return result;
    }

    public static Type getType(int code)
    {
        return TYPES.get(code);
    }
}
