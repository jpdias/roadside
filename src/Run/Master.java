package Run;

import Common.Information;
import Common.Question;
import Common.Utilities;
import Interface.MasterMenuChart;
import Interface.MasterMenuConsole;
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;


public class Master extends Agent
{

    public static int numberofquestions = -1;
    public static int ncurrentquestion = 0;
    public static Hashtable<AID,Integer> results = new Hashtable<AID,Integer>();
    public static AID[] players;

    public Master(int numberofquestions){
        this.numberofquestions=numberofquestions;
    }

    protected void setup()
    {


       /* System.out.println("How many questions?(>0)");
        Scanner scn = new Scanner(System.in);
        String number="0";
        try {
            number= scn.nextLine();
        }catch (Exception ex){
            System.out.println("NaN");
        }*/

       // numberofquestions = Integer.parseInt(number);

        //System.out.println("START");
        /*while(true){
            System.out.print("");
            if(Run.init){
                break;
            }
        }*/
        // First set-up answering behaviour

        addBehaviour(new Ask(this));
    }
}

class Ask extends SimpleBehaviour {

    public Ask(Agent a) {
        super(a);
        Master.players = Utilities.searchDF(a, "player");
        for(int i =0;i<Master.players.length;i++){
            Master.results.put(Master.players[i],0);
        }
    }

    private int numberofquestions = Master.numberofquestions;
    private int n = 0;

    public void action() {

        //Choose random category

        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(Information.Categories.length);
        String cat = Information.Categories[index];

        Question current = Information.getQuestion(cat);
        ACLMessage msg;
        msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setContent(current.makeQuestion());
        System.out.println("Question: "+current.makeQuestion());

        for(int j = 0; j<Master.players.length;j++)
            msg.addReceiver(Master.players[j]);

        msg.setConversationId("Run.Master");
        myAgent.send(msg);
        Master.ncurrentquestion++;



        //System.out.println(response);
        //while(response)
        int temp = 0;
        while(true) {
            ACLMessage response=  myAgent.blockingReceive();
            if (response != null) {
                System.out.println("Player answer:" + response.getContent() + " -> from: " + response.getSender().getName());
                if (current.getSolution() == Integer.parseInt(response.getContent().split("|")[0])) {
                    System.out.println("---- Player is right! -----");
                    int pnt = Master.results.get(response.getSender());
                    Master.results.put(response.getSender(),pnt+1);
                    ACLMessage reply = response.createReply();
                    reply.setPerformative( ACLMessage.CONFIRM );
                    reply.setContent("true");
                    myAgent.send(reply);
                } else {
                    System.out.println("---- Player is wrong! ------");

                    ACLMessage reply = response.createReply();
                    reply.setPerformative( ACLMessage.CONFIRM );
                    reply.setContent("false");
                    myAgent.send(reply);
                }
               temp++;
            }
            if(temp == Master.players.length)
                break;

        }
        n++;
        if(n==numberofquestions){
            Enumeration<AID> pl = Master.results.keys();
            while(pl.hasMoreElements()) {
                AID x =  pl.nextElement();
                String str = x.getLocalName();
                int pnts =  Master.results.get(x);
                System.out.println( str + " -> Total right: " + pnts +"; Total wrong: "+ (n-pnts) );
            }
            MasterMenuConsole.showCharts.setEnabled(true);
        }

    }

    public boolean done() {
        return n==numberofquestions;
    }
}