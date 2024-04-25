import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mine Sweeper");
        frame.add(new Mine_Sweeper_GUI());
        frame.setVisible(true);
        frame.setSize(1000, 1000);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



    }

}