package Interface;

import javax.swing.*;
import java.awt.*;

/**
 * Created by João on 03/12/2014.
 */
public class MenuManager extends JFrame {

    public static CardLayout cardlayout;
    public static JPanel cards;
    private static MasterMenuChart mmch;

    public MenuManager(){

        super("Trust in Information Sources");

        setLookAndFeel();

        cardlayout=new CardLayout();
        cards = new JPanel(new CardLayout());;

        InitialMenu im=new InitialMenu();
        MasterMenu mm=new MasterMenu();
        MasterMenuConsole mmc=new MasterMenuConsole();
        mmch = new MasterMenuChart();
        AgentMenu am=new AgentMenu();

        cards.add(im, "InitialMenu");
        cards.add(mm, "MasterMenu");
        cards.add(mmc, "MasterMenuConsole");
        cards.add(mmch, "MasterMenuChart");
        cards.add(am, "AgentMenu");

        cardlayout = (CardLayout) cards.getLayout();
        cardlayout.show(cards, "InitialMenu");

        add(cards);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(600, 400));
        setResizable(false);
        setVisible(true);
    }

    public static MasterMenuChart getMasterMenuChart(){
        return mmch;
    }

    public void setLookAndFeel(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }
}
