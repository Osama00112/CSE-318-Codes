import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("F:\\3-2\\Sessional\\CSE 318\\Latin Square\\src\\data\\1.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        st = br.readLine();
        int i1 = st.indexOf("=");
        int i2 = st.indexOf(";");
        int size = Integer.parseInt(st.substring(i1+1,i2));
        System.out.println(size);
        LatinBoard board = new LatinBoard(size);
        int i=0;
        while ((st = br.readLine()) != null){
            //System.out.println(st);
            String  []words = st.split(", ");
            int j=0;
            for(String s: words){
                board.cells[i][j] = new SquareCell(Integer.parseInt(s), i, j, size);
                j++;
            }
        i++;
        }

        long start = System.currentTimeMillis();
        board.updateDomains();
        board.calculateDegree();

        Solver sv = new Solver(1,1, board);
        sv.backTrack();
        System.out.println("answer : \n" + sv.solvedBoard);

        long end = System.currentTimeMillis();
        System.out.println("Time elapsed " + (end-start) + " ms\n" +
                "Node Count:    " + sv.nodecount+"\n" +
                "BT Node Count: " + sv.BTcount);
        //System.out.println(board);

    }
}
