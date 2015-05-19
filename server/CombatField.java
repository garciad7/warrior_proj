// Manage the combat field and warriors

import java.io.*;
import java.net.*;
import java.util.*;

public class CombatField
{
    private ServerSocket tcpSocket;
    private Map<String, UserThread> users;
    private Map<Integer, Combat> combats;
    private int combatId = 1;    // combat id

    // Construct the server
    public CombatField(int port) throws IOException
    {
        tcpSocket = new ServerSocket(port);
        users = new TreeMap<String, UserThread>();
        combats = new TreeMap<Integer, Combat>();
        System.out.println("Server running on");
    }
    
    public int getCombatId()
    {
        int id = 0;
        synchronized(this)
        {
            id = combatId;
            combatId++;
        }
        return id;
    }

    public void addCombat(Combat combat)
    {
        synchronized(combats)
        {
            combats.put(combat.getCombatId(), combat);
        }
    }

    public void processCombat(int id, String[] params)
    {
        Combat c = null;

        synchronized(combats)
        {
            c = combats.get(id);
            combats.remove(id);
        }
        
        if (c != null)
            c.process(params);
            
    }

    public Map<String, UserThread> getUsers()
    {
        return users;
    }

    public void startRun() throws IOException
    {
        while (true)
        {
            Socket connectionSocket = tcpSocket.accept();
            UserThread thread = new UserThread(connectionSocket, this);
            thread.start();
        }
        
    }

}
