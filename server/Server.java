// This class will parse the command line arguments (if exists)
//  and create the CombatField program

import java.util.Scanner;

public class Server
{
    public static final int DEFAULT_PORT = 62250;

    public static void main(String[] args)
    {
        try
        {
            Scanner scan = new Scanner(System.in);
            int port = DEFAULT_PORT;
            
            if (args.length > 0)
            {
                port = Integer.parseInt(args[1]);
            }
            else
            {
                System.out.print("Enter the port(or empty for default): ");
                String portStr = scan.nextLine().trim();
                if (!portStr.isEmpty())
                    port = Integer.parseInt(portStr);
            }
            
            CombatField prog = new CombatField(port);
            prog.startRun();            
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
