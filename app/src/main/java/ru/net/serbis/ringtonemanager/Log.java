package ru.net.serbis.ringtonemanager;

/**
 * SEBY0408
 */
public class Log
{
    public static void info(Object object, String message)
    {
        android.util.Log.i(object.getClass().getName(), message);
    }

    public static void error(Object object, String message, Throwable e)
    {
        android.util.Log.e(object.getClass().getName(), message, e);
    }
}
