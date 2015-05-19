// This class will connect to server and display the UI

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WarriorClient
{
    private Warrior warrior = null;
    private Scanner scan = new Scanner(System.in);

    private Socket tcpSocket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;    
    
    private ArrayList<Warrior> enemies = new ArrayList<Warrior>();
    
    // Constructor the client object
    public WarriorClient(String addr, int port) throws UnknownHostException, IOException
    {        
        while (warrior == null)
            selectWarrior();
        
        tcpSocket = new Socket(addr, port);
        outputStream = new DataOutputStream(tcpSocket.getOutputStream()); 
        inputStream = new DataInputStream(tcpSocket.getInputStream());
    }

    // Ask the user to select the warrior
    private void selectWarrior()
    {
        System.out.print("Select the warrior(type 1 for existed warrior, type 2 for new warrior): ");
        String cmd = scan.nextLine();
        if (cmd.equals("1"))
        {
            System.out.print("Enter the warrior name: ");
            String name = scan.nextLine();
            warrior = WarriorFile.loadWarrior(name);
            if (warrior == null)
                System.out.println("Error: the warrior does not exist.");
            else if (warrior.isDead())
            {
                System.out.println("Error: the warrior was dead.");
                warrior = null;
            }
        }
        else if (cmd.equals("2"))
        {
            warrior = new Warrior();
            System.out.print("Enter the name of the warrior: ");
            warrior.setName(scan.nextLine());
            System.out.print("Enter the original place: ");
            warrior.setPlaceOriginal(scan.nextLine());
            System.out.print("Enter the description: ");
            warrior.setDescription(scan.nextLine());
            
            WarriorFile.saveWarrior(warrior.getName(), warrior);
        }
        else
        {
            System.out.println("Unknown command.");
        }        
    }

    // Start to play
    public void startRun() throws IOException, InterruptedException
    {
        // send register information and get list of warriors
        register();

        System.out.println();
        System.out.println("1. Challenge another Warrior");
        System.out.println("2. Display information");
        System.out.println("3. Quit");
        System.out.println("Please enter the choice...");
        System.out.println();
        String cmd = "";
        do
        {
            // check notification from server
            while (inputStream.available() > 0 && !warrior.isDead())
            {
                System.out.println();
                processServer(inputStream.readUTF());
            }
            
            if (warrior.isDead())
                break;
            
            if (System.in.available() > 0)
            {            
                cmd = scan.nextLine();                
                
                if (cmd.equals("1"))
                {
                    challenge();
                }
                else if (cmd.equals("2"))
                {
                    displayInfo();
                }
                else if (!cmd.equals("3"))
                {
                    System.out.println("Unknown choice.");
                }
            }
            else
            {
                Thread.sleep(100);
            }
            
        } while (!cmd.equals("3") && !warrior.isDead());
        
        if (warrior.isDead())
        {
            outputStream.writeUTF("DIE");
        }
        else
        {
            outputStream.writeUTF("EXIT");
        }
        
        inputStream.close();
        outputStream.close();
        tcpSocket.close();
        
        WarriorFile.saveWarrior(warrior.getName(), warrior);
    }

    // Display information
    private void displayInfo()
    {
        System.out.println("Current warrior status: ");
        System.out.println(warrior);
        System.out.println(" ============ Warrior List ===========");
        for (int i = 0; i < enemies.size(); i++)
        {
            Warrior w = enemies.get(i);
            System.out.println(w.getName() + " (at " +
                    w.getPlaceOriginal() + ")");
            System.out.println("\t(" + w.getDescription() + ")");
            System.out.println();
        }        
    }
    
    // Find the index of the enemy 
    private int findWarrior(String name)
    {
        for (int i = 0; i < enemies.size(); i++)
        {
            Warrior w = enemies.get(i);
            if (w.getName().equals(name))
                return i;
        }
        return -1;
    }

    // send register information
    private void register() throws IOException
    {
        outputStream.writeUTF(warrior.getName() + "\n" +
                warrior.getPlaceOriginal() + "\n" +
                warrior.getDescription());
        
        String message = inputStream.readUTF();
        System.out.println(message);
        
        int size = Integer.parseInt(inputStream.readUTF());
        for (int i = 0; i < size; i++)
        {
            Warrior w = new Warrior();
            String info = inputStream.readUTF();
            String[] data = info.split("\n");
            
            w.setName(data[0]);
            w.setPlaceOriginal(data[1]);
            w.setDescription(data[2]);
            enemies.add(w);
        }
        
    }

    // challenge another Warrior
    private void challenge() throws IOException
    {
        if (enemies.size() == 0)
        {
            System.out.println("No warrior found.");
            return;
        }
        
        String target = selectTarget();
        String action = selectAction();
        
        int mp = warrior.getActionMagic(action);
        warrior.setMagicPoint(warrior.getMagicPoint() - mp);
        
        outputStream.writeUTF("REQ\n" + target + "\n" + action);
        processServer(inputStream.readUTF());
    }

    // Ask the user to select action
    private String selectAction()
    {
        ArrayList<String> attacks = warrior.getAttacks();
        ArrayList<String> defenses = warrior.getDefenses();
        System.out.println("Select your operations: ");
        System.out.print("  Attack Actions: ");
        for (int i = 0; i < attacks.size(); i++)
            System.out.print(attacks.get(i) + " ");
        System.out.print("\n  Defense Actions: ");
        for (int i = 0; i < defenses.size(); i++)
            System.out.print(defenses.get(i) + " ");
        System.out.print("\nEnter the action: ");
        
        boolean valid = false;
        String action = "";
        while (!valid)
        {
            action = scan.nextLine();
            if (!attacks.contains(action) && !defenses.contains(action))
            {
                System.out.print("Invalid action. Enter again: ");
            }
            else if (warrior.getActionMagic(action) > warrior.getMagicPoint())
            {
                System.out.print("Insufficient magic point. Enter again: ");
            }
            else
            {
                valid = true;
            }            
        }
        
        return action;
    }

    // Ask the user to select target
    private String selectTarget()
    {
        System.out.println("Select warrior from the list: ");
        for (int i = 0; i < enemies.size(); i++)
        {
            System.out.println("\t" + enemies.get(i).getName());
        }
        System.out.print("Enter the name: ");
        String name = scan.nextLine();
        while (findWarrior(name) < 0)
        {
            System.out.print("Invalid name. Enter again: ");
            name = scan.nextLine();
        }        
        
        return name;
    }

    // NOTIFY QUIT 
    private void processServer(String message) throws IOException 
    {        
        
        if (message.startsWith("USER"))
            processUser(message);
        else if (message.startsWith("QUIT"))
            processQuit(message);
        else if (message.startsWith("ERR"))
            processError(message);
        else if (message.startsWith("NOTIFY"))
            processNotify(message);
        else if (message.startsWith("RESULT"))
            processResult(message);
        else
            System.out.println("Unknown message: " + message);
    }

    private void processResult(String message)
    {
        // RESULT enemyAction demage message
        String[] params = message.split("\n");
        System.out.println(params[3]);
        int demage = Integer.parseInt(params[2]);
        
        warrior.applyAction(demage, params[1]);
        System.out.println("Your current health level: " + 
                warrior.getHealthLevel()); 
    }

    private void processNotify(String message) throws IOException
    {
        // NOTIFY id source action message
        String[] params = message.split("\n");

        String id = params[1];
        String source = params[2];
        String act = params[3];
        System.out.println(params[4]);
        
        String rspAct = selectAction();
        outputStream.writeUTF("RSP\n" + id + "\n" + source + "\n" + rspAct);
        processServer(inputStream.readUTF());
    }

    private void processError(String message)
    {
        // ERR message
        String[] params = message.split("\n");
        System.out.println("Error: " + params[1]);
    }

    private void processQuit(String message)
    {
        // QUIT name message
        String[] params = message.split("\n");
        System.out.println(params[2]);
        int pos = findWarrior(params[1].trim());
        if (pos >= 0)
            enemies.remove(pos);
    }

    private void processUser(String message)
    {
        // USER name place descriptoin
        String[] params = message.split("\n");
        System.out.println("New warrior " + params[1] +
                " entered the battle field.");
        Warrior w = new Warrior();
        w.setName(params[1]);
        w.setPlaceOriginal(params[2]);
        w.setDescription(params[3]);
        enemies.add(w);        
    }

}
