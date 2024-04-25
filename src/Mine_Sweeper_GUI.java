
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;



public class Mine_Sweeper_GUI extends JPanel implements MouseListener, ActionListener, MouseWheelListener {
    Board Minesweeper;
    int x_cord = 0;
    int y_cord = 0;
    int Tile_Size = 30;
    int[] BoardSize = {10,10};
    int Max_Bombs = 10;
    LocalDateTime date;
    Timer time;

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
        addMouseWheelListener(this);
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
        TimeRepaint();

    }
    // initialiser timer og repaint metode på timer, tager hensyn til skærmstørrelse
    private void TimeRepaint(){
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Dimension screensize = Mine_Sweeper_GUI.this.getSize();
                x_cord = (int) (screensize.getWidth() - (Tile_Size*BoardSize[0])) / 2;
                y_cord = (int) (screensize.getHeight() - (Tile_Size*BoardSize[1])) / 2;
                repaint();
            }
        };
        time = new Timer(50, taskPerformer);
        time.start();
    }

    //paint component metode, tegner grafikken på panelet
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // indlæs font:
        loadFont(g, Tile_Size-5);
        int color = 0;
        //oprettet nested loop til tegning af felter
        for(int i = 0; i < BoardSize[0]; i++){
            for(int j = 0; j < BoardSize[1]; j++){
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
                    // tegner brikken
                    g.fillRect(x_koordinat, y_koordinat, Tile_Size, Tile_Size);
                    g.setColor(Color.BLACK);

                    // tjek om brikken er trykket  og ikke en bombe:
                    if (Minesweeper.Board[i][j][1 ] == "p" && !Minesweeper.Board[i][j][0].equals("0") && !Minesweeper.Board[i][j][0].equals("B")){
                        // set farve til skrift
                        g.setColor(colorList[Integer.parseInt(Minesweeper.Board[i][j][0])]);
                        // skriv tal
                        g.drawString(Minesweeper.Board[i][j][0], (int) (x_koordinat+Tile_Size/2.5), y_koordinat+g.getFont().getSize());
                    }
                    if (Minesweeper.Board[i][j][1] == "f"){
                        loadImage(g,i,j,"resources/image.png");
                    }
                    if(Minesweeper.Board[i][j][1] == "p" && Minesweeper.Board[i][j][0].equals("B")){
                        loadImage(g,i,j,"resources/image2.png");
                    }
                }
                // ++ color, for at kunne skifte farve alternerende
                color ++;
            }
            color++;

        }

        g.setFont(g.getFont().deriveFont(Font.BOLD,50));
        if(Minesweeper != null){
            g.drawString("Flag: "+Minesweeper.maxFlags,500, 100);
            g.drawString("Tid: " + Duration.between(date,LocalDateTime.now()).getSeconds(), 500, 140);
        }else{
            g.drawString("Flag: "+Max_Bombs, 500, 100);
        }
    }
    // indlæs font metode
    private void loadFont(Graphics g,int Size){
        try {
            //InputStream for at kunne læse font filen i executable jar fil
            InputStream loadFont =Main.class.getResourceAsStream("resources/Seven_Segment.ttf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, loadFont);
            customFont = customFont.deriveFont(Font.BOLD,Size);
            g.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

    }
    // tegn flag, hvis der er placeret et flag på brikken
    // tegn bombe, hvis brikken er trykket og en bombe.
    private void loadImage(Graphics g,int i,int j, String path){
        try {
            InputStream inputStream = Main.class.getResourceAsStream(path);
            Image image = ImageIO.read(inputStream);
            g.drawImage(image, Tile_Size*i+x_cord, Tile_Size*j+y_cord, Tile_Size, Tile_Size, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // metode til at initialisere boardet med koordinaterne for det første tryk
    private void initializeBoard(int[] cords){
        Minesweeper = new Board(cords, Max_Bombs, BoardSize);
        Minesweeper.press(cords);
        date = LocalDateTime.now();

    }
    // knap input registrering
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Nem":
                Max_Bombs = 10;
                BoardSize = new int[]{10,10};
                Minesweeper = null;
                break;
            case "Medium":
                Max_Bombs = 40;
                BoardSize = new int[]{16,16};
                Minesweeper = null;
                break;
            case "Svær":
                Max_Bombs = 99;
                BoardSize = new int[]{30,16};
                Minesweeper = null;
                break;
            default:
                break;
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {

        // udregn koordinater tilsvarende til minesweeper boardet:
        int[] cords = {(e.getX()-x_cord)/Tile_Size, (e.getY()-y_cord)/Tile_Size};

        if (e.getX()-x_cord <=0 || e.getY()-y_cord <=0 || e.getX()-x_cord >= Tile_Size*BoardSize[0] || e.getY()-y_cord >= Tile_Size*BoardSize[1]){
            System.out.println("Udenfor");
            return;
        }
            // Lav et nyt board første gang der trykkes på GUI:
        if(Minesweeper == null){
            initializeBoard(cords);
            time.start();
        }
        // reveal eller plant flag alt efter om det er venstre/højre klik på musen:
        else{
            if(e.getButton() == MouseEvent.BUTTON1){
                Minesweeper.press(cords);
            }
            else if(e.getButton() == MouseEvent.BUTTON3){
                Minesweeper.setFlag(cords);
            }
            // tjek om der er flag på alle bomber
            if(Minesweeper.winCheck()){
                time.stop();
                JOptionPane.showMessageDialog(null, "Du har vundet og din tid er " +Duration.between(date,LocalDateTime.now()).getSeconds() +" sekunder");
                time.start();
                Minesweeper = null;
            }
            // tjek om der er blevet trykket på en bombe, reset spil hvis der er
            if (Minesweeper.lossCheck(cords)) {
                time.stop();
                JOptionPane.showMessageDialog(null, "Du har tabt og din tid er "+Duration.between(date,LocalDateTime.now()).getSeconds()+ " sekunder");
                time.start();
                Minesweeper = null;
            }
        }
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
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() < 0 && !(Tile_Size >= 100)){
            Tile_Size += 1;
        }
        else if(e.getWheelRotation() > 0 && !(Tile_Size <=10)){
            Tile_Size -= 1;
        }
    }
}
