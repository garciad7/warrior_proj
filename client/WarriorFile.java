// This class will load and save .wdat file
import java.io.*;
import java.util.Scanner;

public class WarriorFile
{    
    // read warrior from the file
    public static Warrior loadWarrior(String fname)
    {
        Warrior wr = new Warrior();
        
        try
        {
            Scanner reader = new Scanner(
                    new FileInputStream(fname + ".wdat"));
            
            String type = reader.nextLine();
            int health = Integer.parseInt(reader.nextLine());
            int magic = Integer.parseInt(reader.nextLine());
            String place = reader.nextLine();
            String desc = reader.nextLine();
            
            wr.setHealthLevel(health);
            wr.setMagicPoint(magic);
            wr.setName(fname);
            wr.setDescription(desc);
            wr.setPlaceOriginal(place);
            
            reader.close();
        } catch (Exception e)
        {
            System.out.println(e);
            wr = null;
        }
        return wr;
    }
    
    // save the warrior to file
    public static void saveWarrior(String fname, Warrior w)
    {
        try
        {
            PrintStream file = new PrintStream(new File(fname + ".wdat"));
            file.println("Warrior"); // type
            file.println("" + w.getHealthLevel());
            file.println("" + w.getMagicPoint());
            file.println(w.getPlaceOriginal());
            file.println(w.getDescription());
            file.close();
            
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
