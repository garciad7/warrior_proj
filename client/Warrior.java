import java.util.ArrayList;

// This class stores the information of the Warrior

public class Warrior
{
    private int healthLevel;  // 0 to 100
    private int magicPoint;  // 0 to 100
    private String placeOriginal;
    private String description;
    private String name;
    
    // Constructor
    public Warrior()
    {
        healthLevel = 100;
        placeOriginal = "";
        description = "";
        magicPoint = 100;
        name = "";
    }
    
    // Return description
    public String toString()
    {
        String str = "";
        str += "Name: " + name;
        str += "\nPlace: " + placeOriginal;
        str += "\nDescription: " + description;
        str += "\nHealth Level: " + healthLevel;
        str += "\nMagic Point: " + magicPoint;        
        return str;
    }
    
    // Return the attack actions
    public ArrayList<String> getAttacks()
    {
        ArrayList<String> attacks = new ArrayList<String>();
        attacks.add("slash");
        attacks.add("stab");
        attacks.add("shoot");
        attacks.add("kick");
        
        return attacks;
    }
    
    // Return the defense actions
    public ArrayList<String> getDefenses()
    {
        ArrayList<String> attacks = new ArrayList<String>();
        attacks.add("block");
        attacks.add("dodge");
        attacks.add("shield");
        
        return attacks;
    }
    
    // Return the magic point needed for the action
    public int getActionMagic(String act)
    {
        if (act.equals("shoot"))
            return 8;
        else if (act.equals("slash") ||
                 act.equals("stab") ||
                 act.equals("kick"))
            return 3;
        else if (act.equals("shield"))
            return 1;
        return 0;
    }
    
    public void applyAction(int demage, String act)
    {
        setHealthLevel(getHealthLevel() - demage);
    }
    
    // Return if the warrior is dead
    public boolean isDead()
    {
        return healthLevel == 0;
    }
    
    // getters and setters
    public int getHealthLevel()
    {
        return healthLevel;
    }
    
    public void setHealthLevel(int lvl)
    {
        if (lvl < 0)
            lvl = 0;
        if (lvl > 100)
            lvl = 100;
        healthLevel = lvl;
    }
    
    public String getPlaceOriginal()
    {
        return placeOriginal;
    }
    
    public void setPlaceOriginal(String place)
    {
        placeOriginal = place;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String desc)
    {
        description = desc;
    }

    public int getMagicPoint()
    {
        return magicPoint;
    }

    public void setMagicPoint(int pt)
    {
        magicPoint = pt;
    }

    public String getName()
    {
        return name;
    }
    
    public void setName(String nm)
    {
        name = nm;
    }
    
}
