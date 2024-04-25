import java.util.Arrays;


public class Board {

    String[][][] Board;
    int maxFlags;

    // board constructor
    Board(int[] startCords, int maxFlags, int[] BoardSize){
        this.Board = genNewBoard(new int[]{BoardSize[0],BoardSize[1]},maxFlags, startCords);
        this.maxFlags = maxFlags;

    }


    // metode der generer et nyt board:
    private String[][][]genNewBoard(int[] size, int bombCount, int[] startCords){
        String[][][] newBoard = new String[size[0]][size[1]][2];
        
        // put bomber på tilfældige pladser:
        for (int i = 0; i < bombCount; i++){
            // vælg tilfælgig plads
            int x = (int) (Math.random() * size[0]);
            int y = (int) (Math.random() * size[1]);

            // tjek om det tilfældige koordinat allerede har en bombe eller er start koordinatet:
            if (isStartCoordinate(startCords,x,y) || newBoard[x][y][0] == "B"){
                i--;
            }
            // sæt bomberne:
            else {
                newBoard[x][y][0] = "B";
            }
        }

        // put tallene ind på boardet, itere igennem hele brættet og kald på countBombs metoden:
        for (int i = 0; i < size[0];i++){
            for(int j = 0; j<size[1];j++){
                if (newBoard[i][j][0] != ("B")){
                    // sæt bræt værdi til antal bomber fundet:
                    newBoard[i][j][0] = String.valueOf(countBombs(new int[]{i,j},newBoard));
                }
            }
        }
        return newBoard;
    }

    // metode til at tjekke om et koordinat er inde for en vis radius af start koordinatet
    private boolean isStartCoordinate(int[] startCoor,int x, int y) {
        for(int i = -2; i < 2 ;i++){
            for(int j = -2; j < 2;j++){
                if(startCoor[0]+i == x && startCoor[1]+j == y){
                    return true;

                }
            }
        }

    return false;
    }

    // metode til at tælle bomber rundt om et koordinat
    private int countBombs(int[] cords, String[][][] board){
        // kig på alle brikker rundt om det valgte og tæl efter bomber
        int bombCount = 0;
        for (int i = -1; i<2;i++){
            for (int j  = -1; j<2;j++){
                try {
                    if (board[cords[0]+i][cords[1]+j][0].equals("B")){
                        bombCount++;
                    }
                } catch (Exception e){
                    // hvis der er en fejl så er det fordi vi er ude for brættet og vi ignorerer det.
                }
            }
        }
        return bombCount;
    }
    // sæt en plads status til pressed, hvis pladsen ikke er flagget:
    public void press(int[] cords){
        // set p value
        if (!(this.Board[cords[0]][cords[1]][1] == "f")){
            this.Board[cords[0]][cords[1]][1] = "p";
            // hvis brikken er nul, så kald på reveal area metoden
            if (this.Board[cords[0]][cords[1]][0].equals("0")) {
                revealArea(cords);
            }
        }
    }
    // sæt eller fjern flaget, hvis pladsen ikke er pressed endnu
    public void setFlag(int[] cords){
        // get value af brikkenc:
        String value = this.Board[cords[0]][cords[1]][1];

        // set unchecked
        if (value == "f"){
            this.Board[cords[0]][cords[1]][1] = null;
            this.maxFlags ++;
        }
        // set flag
        if (value == null && this.maxFlags >0){
            this.Board[cords[0]][cords[1]][1] = "f";
            this.maxFlags --;

        }
    }

    // metode der viser alle de omkringliggende 0 brikker.
    private void revealArea(int[] cords){
        // vis alle omkring liggende 0 brikker.
        for (int i = -1; i<2;i++){
            for (int j  = -1; j<2;j++){
                try {
                    // kigger rundt om koordinatet, hvis brikkerne rundt om er 0 og ikke trykket på endnu
                    // reveal dem og rekursivt kald på reveal area
                    if(((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)) || (i/j == -1 || i/j == 1)){
                        if(!(this.Board[cords[0] + i][cords[1] + j][1] =="p")){
                            this.Board[cords[0]+i][cords[1]+j][1] = "p";
                            if (this.Board[cords[0] + i][cords[1] + j][0].equals("0")){
                                revealArea(new int[]{cords[0] + i, cords[1] + j});
                            }

                        }
                    }
                }
                catch (Exception e){
                    // hvis der er en fejl så er det fordi vi er ude for brættet og vi ignorerer det.
                }
            }
        }
    }

    // tjekker om alle bomber har flag
    public boolean winCheck(){
        for(int i = 0; i < this.Board.length;i++){
            for(int j =0; j < this.Board.length;j++){
                if(!(Board[i][j][1] == "f") && Board[i][j][0].equals("B") || Board[i][j][1] != "p" && !Board[i][j][0].equals("B")) {
                    System.out.println("not all bombs are flagged");
                    return false;
                }
            }
        }
        return true;
    }
    // tjekker om en bombe er trykket på
    public boolean lossCheck(int[] cords){
        return this.Board[cords[0]][cords[1]][0] == "B" && !this.Board[cords[0]][cords[1]][1].equals( "f");
    }


    public void print(){
        for(int i =0; i< this.Board[0].length;i++){
            System.out.println(Arrays.deepToString(this.Board[i]));
        }
        //System.out.println(Arrays.deepToString(this.Board));
    }


}
