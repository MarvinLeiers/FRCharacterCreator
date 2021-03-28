package de.marvinleiers.frcharactercreator;

import org.jetbrains.annotations.NotNull;

public class Character implements Comparable<Character>
{
    private String description;
    private final int id;
    private String name;
    private String age;

    public static Character EMPTY_CHARACTER()
    {
        return new Character("", "", "", -1);
    }

    public Character(String name, String description, String age, int id)
    {
        this.description = description;
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public String getAge()
    {
        return age;
    }

    public boolean isValid()
    {
        return !name.isEmpty() && !description.isEmpty() && Integer.parseInt(age) > 0;
    }

    @Override
    public int compareTo(@NotNull Character otherCharacter)
    {
        return getName().compareTo(otherCharacter.getName());
    }
}
