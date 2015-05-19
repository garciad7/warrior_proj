
public class Combat
{
    private CombatField combatField;
    private UserThread srcUser;
    private UserThread dstUser;
    private int combatId;
    
    private String[] srcParams; // REQ target action 
    private String[] dstParams; // RSP id target action
    
    public Combat(int id, CombatField field, UserThread src, 
            UserThread dst, String[] params)
    {
        combatId = id;
        combatField = field;
        srcUser = src;
        dstUser = dst;
        srcParams = params;
    }

    public int getCombatId()
    {
        return combatId;
    }

    public void process(String[] params)
    {
        dstParams = params;
        String src = srcUser.getUser().getName();
        String dst = dstUser.getUser().getName();
        String srcAct = srcParams[2];
        String dstAct = dstParams[3];
        int srcDemage = 0;
        int dstDemage = 0;
        String srcMsg = "";
        String dstMsg = "";
        
        int srcAttack = getAttack(srcAct);
        int srcDef = getDefense(srcAct);
        int dstAttack = getAttack(dstAct);
        int dstDef = getDefense(dstAct);
        
        if (dstAttack > 0)
            srcDemage = dstAttack - srcDef;
        if (srcDemage < 0)
            srcDemage = 0;
        if (srcAttack > 0)
            dstDemage = srcAttack - dstDef;
        if (dstDemage < 0)
            dstDemage = 0;

        if (srcDemage > 0)
        {
            srcMsg = dst + " " + dstAct + " on you and the demage is " + srcDemage;
        }
        else
        {
            srcMsg = dst + " " + dstAct + ", no demage";
        }

        if (dstDemage > 0)
        {
            dstMsg = src + " " + srcAct + " on you and the demage is " + dstDemage;
        }
        else
        {
            dstMsg = src + " " + srcAct + ", no demage";
        }
        
        // RESULT enemyAction demage message
        srcUser.sendMessage("RESULT\n" + dstAct + "\n" +
                srcDemage + "\n" + srcMsg);
        dstUser.sendMessage("RESULT\n" + srcAct + "\n" +
                dstDemage + "\n" + dstMsg);
    }

    private int getDefense(String act)
    {
        if (act.equals("shield"))
            return 10;
        else if (act.equals("dodge"))
            return 3;
        else if (act.equals("block"))
            return 4;
        else if (act.equals("shoot"))
            return -1;
        return 0;
    }

    private int getAttack(String act)
    {
        if (act.equals("shoot"))
            return 15;
        else if (act.equals("slash") ||
                act.equals("stab") ||
                act.equals("kick"))
            return 7;
        return 0;
    }
}
