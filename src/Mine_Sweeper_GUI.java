import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class Mine_Sweeper_GUI extends JPanel implements MouseListener, ActionListener {

    Board Minesweeper;

    int x_cord = 375;

    int y_cord = 100;

    int Tile_Size = 40;

    int Max_Bombs = 25;



    // colorlist til tal på brikker
     Color[] colorList = {Color.BLACK,
            new Color(47, 127, 230),
            new Color(69, 209, 34),
            new Color(243, 15, 15),
            new Color(120, 38, 246),
            new Color(255, 104, 14),
            new Color(96, 255, 216),
            new Color(255, 191, 68),
            new Color(255, 94, 244),};


    // GUI klasse konstruktor:
    public Mine_Sweeper_GUI(){
        addMouseListener(this);
        setVisible(true);
        // tilføj knapper til at vælge sværhedsgrad
        JButton nemButton = new JButton("Nem");
        JButton mediumButton = new JButton("Medium");
        JButton sværButton = new JButton("Svær");
        add(nemButton);
        add(mediumButton);
        add(sværButton);
        nemButton.addActionListener(this);
        mediumButton.addActionListener(this);
        sværButton.addActionListener(this);
        revalidate();
    }

    //paint component metode, tegner grafikken på panelet
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // indlæs font:
        loadFont(g);
        if(Minesweeper != null){
            g.drawString("Flag: "+Minesweeper.maxFlags, x_cord+200, 70);
        }else{
            g.drawString("Flag: "+Max_Bombs, x_cord+200, 70);
        }

        int color = 0;
        //oprettet nested loop til tegning af felter
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 14; j++){

                int x_koordinat = i*Tile_Size+x_cord;
                int y_koordinat = j*Tile_Size+y_cord;

                // kode ansvarlig for GUI når der ikke er lavet et minesweeper spil
                if(Minesweeper == null){
                    if(color %2 == 0){
                        g.setColor(new Color(142,204, 57));
                    }else{
                        g.setColor(new Color(167, 217, 72, 255));
                    }
                    g.fillRect(x_koordinat, y_koordinat, Tile_Size, Tile_Size);
                    g.setColor(Color.BLACK);
                }

                // Tegn brættet, vælger farver med hensyn til om brikken er trykket på eller ej:
                if (Minesweeper != null){
                    // brikken er trykket:
                    if ((Minesweeper.Board[i][j][1]) == "p"){
                        if(color %2 == 0){
                            g.setColor(new Color(215, 184, 153));
                        }else{
                            g.setColor(new Color(229, 194, 159));
                        }
                    }else{
                        // brikken er ukendt:
                        if(color %2 == 0){
                            g.setColor(new Color(142,204, 57));
                        }else{
                            g.setColor(new Color(167, 217, 72, 255));
                        }
                    }
                    // tegner firkanten
                    g.fillRect(x_koordinat, y_koordinat, Tile_Size, Tile_Size);
                    g.setColor(Color.BLACK);

                    // tjek om brikken er trykket og ikke en bombe:
                    if (Minesweeper.Board[i][j][1] == "p" && !Minesweeper.Board[i][j][0].equals("0") && !Minesweeper.Board[i][j][0].equals("B")){
                        // set farve til skrift
                        g.setColor(colorList[Integer.parseInt(Minesweeper.Board[i][j][0])]);
                        // skriv tal
                        g.drawString(Minesweeper.Board[i][j][0], x_koordinat+15, y_koordinat+28);
                    }
                    if (Minesweeper.Board[i][j][1] == "f"){
                        loadImage(g,i,j,"src/resources/image.png");
                    }
                    if(Minesweeper.Board[i][j][1] == "p" && Minesweeper.Board[i][j][0].equals("B")){
                        loadImage(g,i,j,"src/resources/image2.png");

                    }


                }
                // ++ color, for at kunne skifte farve
                color ++;
            }
            color++;

        }
    }

    private void loadFont(Graphics g){
        try {
            //InputStream is = Main.class.getResourceAsStream("resources/Seven_Segment.ttf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/Seven_Segment.ttf"));
            customFont = customFont.deriveFont(Font.BOLD,30);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

    }
    // tegn flag, hvis der er placeret et flag på brikken
    // tegn bombe, hvis brikken er trykket og en bombe.
    private void loadImage(Graphics g,int i,int j, String path){
        try {
            //InputStream inputStream = Main.class.getResourceAsStream(path);
            //System.out.println(Objects.requireNonNull(inputStream) );
            Image image = ImageIO.read(new File(path));
            g.drawImage(image, Tile_Size*i+x_cord, Tile_Size*j+y_cord, Tile_Size, Tile_Size, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeBoard(int[] cords){
        Minesweeper = new Board(cords, Max_Bombs);
        Minesweeper.press(cords);
    }
    // knap input registrering
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Nem":
                Max_Bombs = 25;
                Minesweeper = null;
                repaint();
                break;
            case "Medium":
                Max_Bombs = 35;
                Minesweeper = null;
                repaint();
                break;
            case "Svær":
                Max_Bombs = 45;
                Minesweeper = null;
                repaint();
                break;
            default:
                break;
        }
    }
    // mus events:
    @Override
    public void mousePressed(MouseEvent e) {
        int[] cords = {(e.getX()-x_cord)/Tile_Size, (e.getY()-y_cord)/Tile_Size};
        // udregn koordinater tilsvarende til minesweeper boardet:
        if(e.getX()-x_cord <=0 || e.getY()-y_cord <=0 || e.getX()-x_cord >= 560 || e.getY()-y_cord >= 560){
            throw new IllegalArgumentException("x: "+cords[0] + " y: " + cords[1] + " er ud for brættet");
        }
        // Lav et nyt board første gang der trykkes på GUI:
        if(Minesweeper == null){
            initializeBoard(cords);
        }
        // reveal eller plant flag alt efter om det er venstre/højre klik på musen:
        else{
            if(e.getButton() == MouseEvent.BUTTON1){
                Minesweeper.press(cords);
                // tjek om der er blevet trykket på en bombe, reset spil hvis der er
                if (Minesweeper.lossCheck(cords)) {
                    repaint();
                    JOptionPane.showMessageDialog(null, "Du har tabt");
                    Minesweeper = null;
                }
            }
            else if(e.getButton() == MouseEvent.BUTTON3){
                // sæt flag
                Minesweeper.setFlag(cords);
                // tjek om der er flag på alle bomber
                if(Minesweeper.winCheck()){
                    repaint();

                    JOptionPane.showMessageDialog(null, "Du har vundet");
                    Minesweeper = null;

                }
            }
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
