package ru.net.serbis.ringtonemanager;

/**
 * SEBY0408
 */
public class RingtoneItem
{
    private Type type;
    private String name;

    public RingtoneItem(Type type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public Type getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
