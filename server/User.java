// Store the information of the users

import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class User
{
    private String name;
    private String placeOriginal;
    private String description;
    private Map<Integer, Combat> combats;
    
    public User(String nm, String place, String desc)
    {
        name = nm;
        placeOriginal = place;
        description = desc;
        combats = new TreeMap<Integer, Combat>();
    }

    public Map<Integer, Combat> getCombats()
    {
        return combats;
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
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String nm)
    {
        name = nm;
    }

}
