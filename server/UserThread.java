import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;


public class UserThread extends Thread
{
    private CombatField combatField;
    private Socket clientSocket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;  
    private User user;

    public UserThread(Socket sock, CombatField combat) throws IOException
    {
        combatField = combat;
        clientSocket = sock;

        outputStream = new DataOutputStream(sock.getOutputStream()); 
        inputStream = new DataInputStream(sock.getInputStream());
        
        processRegister();
    }
    
    public User getUser()
    {
        return user;
    }

    // Run method of the thread
    public void run()
    {
        if (user == null)
            return;
        String msg = "";
        try
        {
            msg = inputStream.readUTF();
            while (!msg.equals("DIE") && !msg.equals("EXIT"))
            {
                if (msg.startsWith("REQ"))
                    processRequest(msg);
                else if (msg.startsWith("RSP"))
                    processResponse(msg);
                else
                {
                    msg = "EXIT";
                    break;
                }
                    
                msg = inputStream.readUTF();
            }
        }
        catch (Exception e)
        {
            msg = "EXIT";
        }
        
        processQuit(msg);
        
        closeConn();
    }
    
    private void closeConn()
    {
        try
        {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
        }
    }
    
    // user quit the program
    private void processQuit(String msg)
    {
        String cmd = "QUIT\n" + user.getName() + "\n";
        if (msg.equals("EXIT"))
            cmd += "Warrior " + user.getName() + " quits the game.";
        else
            cmd += "Warrior " + user.getName() + " dies and quits the game.";

        Map<String, UserThread> mp = combatField.getUsers();

        synchronized(mp)
        {
            if (mp.containsKey(user.getName()))
            {
                mp.remove(user.getName());
                for (UserThread thread : mp.values())
                {                    
                    thread.sendMessage(cmd);
                }
            }
        }

        System.out.println(user.getName() + " quits the field");
    }

    // user send request to server
    private void processRequest(String msg)
    {
        // REQ target action
        String[] params = msg.split("\n");

        Map<String, UserThread> mp = combatField.getUsers();

        UserThread thread = null;
        synchronized(mp)
        {
            thread = mp.get(params[1]);
        }        

        if (thread == null)
        {
            sendMessage("ERR\nWarrior " + params[1] + " does not exist.");
        }
        else
        {
            // NOTIFY id source action message
            int id = combatField.getCombatId();
            String cmd = "NOTIFY\n" + id + "\n" + user.getName() + "\n" +
                    params[2] + "\nWarrior " + user.getName() + " wants to challenge " +
                    "you (" + params[2] + ")";
            thread.sendMessage(cmd);
            
            Combat combat = new Combat(id, combatField, this, thread, params);
            combatField.addCombat(combat);
        }
    }

    // user send response to server
    private void processResponse(String msg)
    {
        // RSP id target action
        String[] params = msg.split("\n");
        int id = Integer.parseInt(params[1]);
        combatField.processCombat(id, params);
    }
    
    // process the user register message
    private void processRegister() throws IOException
    {
        String msg = inputStream.readUTF();
        String[] params = msg.split("\n");
        String name = params[0];
        String place = params[1];
        String desc = params[2];
        String added = "USER\n" + name + "\n" + place + "\n" + desc;
        
        Map<String, UserThread> mp = combatField.getUsers();

        synchronized(mp)
        {
            if (!mp.containsKey(name))
            {
                user = new User(name, place, desc);
                sendMessage("Welcome to the battle field, warrior " + name);
                sendMessage("" + mp.size());
                for (UserThread thread : mp.values())
                {
                    thread.sendMessage(added);
                    User u = thread.getUser();
                    sendMessage(u.getName() + "\n" + u.getPlaceOriginal()
                            + "\n" + u.getDescription());
                }
                mp.put(name, this);
            }
        }
        if (user != null)
            System.out.println(user.getName() + " entered the field");
    }

    // send message to the client
    public void sendMessage(String msg)
    {
        synchronized(outputStream)
        {
            try
            {
                outputStream.writeUTF(msg);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
