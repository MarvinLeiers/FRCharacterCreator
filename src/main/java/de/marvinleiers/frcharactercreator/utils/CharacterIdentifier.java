package de.marvinleiers.frcharactercreator.utils;

public enum CharacterIdentifier
{
    CHAR_1(1),
    CHAR_2(2),
    CHAR_3(3);

    private int identifier;

    CharacterIdentifier(int identifier)
    {
        this.identifier = identifier;
    }

    public int getIdentifier()
    {
        return identifier;
    }

    public static CharacterIdentifier getFromIdentifier(int identifier)
    {
        for (CharacterIdentifier characterIdentifier : values())
        {
            if (characterIdentifier.getIdentifier() == identifier)
                return characterIdentifier;
        }

        return null;
    }
}
