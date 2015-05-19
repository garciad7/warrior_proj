// This class will parse the command line arguments (if exists)
//  and create the WarriorClient program
import java.util.Scanner;


public class Client
{
    public static final int DEFAULT_PORT = 62250;

    public static void main(String[] args)
    {
        try
        {
            Scanner scan = new Scanner(System.in);
            String addr = "";
            int port = DEFAULT_PORT;
            if (args.length > 0)
            {
                addr = args[0];
            }
            else
            {
                System.out.print("Enter the server address(or empty for localhost): ");
                addr = scan.nextLine().trim();
            }
            if (addr.isEmpty())
                addr = "localhost";
            
            if (args.length > 1)
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
            
            WarriorClient client = new WarriorClient(addr, port);
            client.startRun();            
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

}
