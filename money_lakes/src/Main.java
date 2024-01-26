/**
 * @since Date: 01.05.2023
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{

        //reading the file
        String fileName = "src/input.txt";
        //creating file and scanner objects
        File file = new File(fileName);
        Scanner scanner1 = new Scanner(file);

        //getting size of the board
        String sizeLine = scanner1.nextLine().trim();
        int[] size = {Integer.parseInt(sizeLine.split(" ")[0]), Integer.parseInt(sizeLine.split(" ")[1])};

        //creating the board
        int[][] board = new int[size[1]][size[0]];

        //reading the board
        for(int row = 0; row < size[1]; row++){
            for(int col = 0; col < size[0]; col++){
                board[row][col] = scanner1.nextInt();
            }
        }
        scanner1.close();

        //printing the board
        printBoard(board, null, false);

        //modifications
        int successfulInputs = 0;
        Scanner scanner2 = new Scanner(System.in);

        //getting modifications until there are 10 of them
        while (successfulInputs < 10){

            //getting user input
            System.out.printf("Add stone %d / 10 to coordinate:", successfulInputs + 1);
            String input = scanner2.nextLine().trim();

            //checking input format
            //finding the index of the first integer
            int splitIdx = 0;
            for(int i = 0; i < input.length(); i++){
                if(Character.isDigit(input.charAt(i))){
                    splitIdx = i;
                    break;
                }
            }

            if (splitIdx == 0 || input.length() > 6){
                System.out.println("Not a valid step!");
                continue;
            }

            boolean isValid = true;
            //splitting the input into letter and index parts
            String first = input.substring(0, splitIdx);
            String second = input.substring(splitIdx);

            for(int i = 0; i < first.length(); i++){
                if(!('a' <= first.charAt(i) && first.charAt(i) <= 'z')){
                    isValid = false;
                    break;
                }
            }

            for(int i = 0; i < second.length(); i++){
                if(!Character.isDigit(second.charAt(i))){
                    isValid = false;
                }
            }
            if(!isValid) {
                System.out.println("Not a valid step!");
                continue;
            }

            int row = Integer.parseInt(second);
            //calculating column index
            int column = 0;
            if(first.length() == 2){
                column += first.charAt(1) - 'a';
                column += (first.charAt(0) - 'a' + 1) * 26;
            } else if(first.length() == 1){
                column += first.charAt(0) - 'a';
            } else {
                System.out.println("Not a valid step!");
                continue;
            }

            //checking if indexes are in the limits
            if(row >= board.length || column >= board[0].length) {
                System.out.println("Not a valid step!");
                continue;
            }

            //modifying
            board[row][column] += 1;
            successfulInputs++;
            printBoard(board, null, false);
            System.out.println("---------------");
        }

        scanner2.close();

        //finding lakes
        int[] deepestAndHighest = findDeepestAndHighest(board);
        int[][] isLakeBoard = new int[board.length][board[0].length];
        int[][] scoreBoard = new int [board.length][board[0].length];

        //deepcopying board array
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                scoreBoard[i][j] = board[i][j];
            }
        }

        //iterating through the deepest elements of the list and finding the deepest one
        while(deepestAndHighest[0] < deepestAndHighest[1]) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (scoreBoard[i][j] == deepestAndHighest[0]) {
                        // for the deepest coordinate check if it is a lake
                        isLakeFunc(scoreBoard, isLakeBoard, i, j);
                        //increasing the height of the deepest coordinates
                        if (isLakeBoard[i][j] == 1) scoreBoard[i][j]++;
                    }
                }
            }
            // new deepest is the previous deepest + 1
            deepestAndHighest[0]++;

            //if a coordinate is a lake for the current layer, it is increased so it becomes not-checked(0)
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if(isLakeBoard[i][j] == 1) isLakeBoard[i][j] = 0;
                }
            }
        }

        //marking lakes finally
        markLakes(board, scoreBoard, isLakeBoard);

        //naming lakes
        int letterCount = 0;
        String[][] letterBoard = new String[board.length][board[0].length];
        for(int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                //if a coordinate is a lake, then names it
                if(isLakeBoard[i][j] == 1 && letterBoard[i][j] == null){
                    nameBoard(board, isLakeBoard, letterBoard, i, j, intToLetter(letterCount, true));
                    letterCount++;
                }
            }
        }

        //printing the final board
        printBoard(board, letterBoard, true);

        //calculating and printing the score
        System.out.printf("Final score: %.2f", calculateScore(board, scoreBoard, letterBoard));
    }

    /**
     * Javadoc description part:
     * This method prints the board considering the length of each element. For final print call, if a there is a lake at current coordinate, it prints the letter code instead of its height
     * Javadoc tags part:
     * @param board 2D array that stores original height of each coordinate
     * @param letterBoard 2D array that stores letter code of each lake
     * @param isFinal boolean variable which is true for final print call, false for others
     */
    public static void printBoard(int[][] board, String[][] letterBoard, boolean isFinal){
        //lines
        for(int row = 0; row < board.length; row++){
            String line = String.format("%3s", row);
            for(int col = 0; col < board[0].length; col++){
                line += String.format("%3s", (isFinal && letterBoard[row][col] != null) ? letterBoard[row][col] : board[row][col]);
            }
            System.out.println(line + " ");
        }

        // bottom part
        String line = "   ";
        for(int i = 0; i < board[0].length; i++) line += String.format("%3s", (intToLetter(i, false)));
        System.out.println(line + " ");
    }

    /**
     * Javadoc description part:
     * This method finds the lowest and highest height value
     * Javadoc tags part:
     * @param heightBoard 2D array that stores original height of each coordinate
     * @return an array whose 1st and 2nd elements are the lowest and highest points of the heightBoard
     */
    public static int[] findDeepestAndHighest(int[][] heightBoard){
        int[] lst = new int[2];
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for(int[] row : heightBoard){
            for(int el : row){
                if(el < min) min = el;
                else if(el > max) max = el;
            }
        }
        lst[0] = min;
        lst[1] = max;
        return lst;
    }

    /**
     * Javadoc description part:
     * This method checks if given coordinate is a lake ank marks it
     * Javadoc tags part:
     * @param heightBoard 2D array that stores original height of each coordinate
     * @param isLakeBoard a 2D array that stores isLake data  0--> not checked, 1--> lake, -1-->not lake, 2-->marked
     * @param row row index
     * @param col column index
     */
    public static void isLakeFunc(int[][] heightBoard, int[][]isLakeBoard, int row, int col){
        //if the coordinate is in the end of the board, it is not a lake
        if(row == heightBoard.length - 1 || row == 0 || col == heightBoard[0].length - 1 || col == 0){
            isLakeBoard[row][col] = -1;
            return;
        }
        //marking
        isLakeBoard[row][col] = 2;

        //iterating through all neighbors
        int[][] neighbors = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] neigh : neighbors){
            int newRow = row + neigh[0];
            int newCol = col + neigh[1];

            if (newRow > heightBoard.length - 1 || newRow < 0 || newCol > heightBoard[0].length - 1 || newCol < 0) continue;

            //checking the neighbors with same height
            if(heightBoard[newRow][newCol] == heightBoard[row][col] && isLakeBoard[newRow][newCol] == 0){
                isLakeFunc(heightBoard, isLakeBoard, newRow, newCol);
            }

            if (isLakeBoard[newRow][newCol] == -1) {
                isLakeBoard[row][col] = -1;
                return;
            }
        }
        isLakeBoard[row][col] = 1;
    }

    /**
     * Javadoc description part:
     * This method names the board with given letter
     * Javadoc tags part:
     * @param heightBoard 2D array that stores original height of each coordinate
     * @param isLakeBoard a 2D array that stores isLake data  0--> not checked, 1--> lake, -1-->not lake, 2-->marked
     * @param letterBoard 2D array that stores letter value of lakes
     * @param row row index
     * @param col column index
     * @param letter current letter value
     */
    public static void nameBoard(int[][] heightBoard, int[][] isLakeBoard, String[][]letterBoard, int row, int col, String letter){
        letterBoard[row][col] = letter;

        //iterating through all neighbors
        int[][] neighbors = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] neigh : neighbors){
            int newRow = row + neigh[0];
            int newCol = col + neigh[1];

            if (newRow < heightBoard.length && newRow >= 0 && newCol < heightBoard[0].length && newCol >= 0){
                if(isLakeBoard[newRow][newCol] == 1 && letterBoard[newRow][newCol] == null) nameBoard(heightBoard, isLakeBoard, letterBoard, newRow, newCol, letter);
            }
        }
    }

    /**
     * Javadoc description part:
     * This method returns a letter that corresponds to the num
     * Javadoc tags part:
     * @param num number to be converted into letter
     * @param isUppercase if true return text is uppercase
     * @return the letter that corresponds to the num
     */
    public static String intToLetter(int num, boolean isUppercase){
        String strForm = "";
        if(num >= 26){
            strForm += (char)(num/26 + 'a' - 1) + "" + (char)(num % 26 + 'a');
        } else {
            strForm += (char)('a' + num);
        }

        if(isUppercase) return strForm.toUpperCase(Locale.ENGLISH);
        else return strForm;
    }

    /**
     * Javadoc description part:
     * This method calculates the score for each lake by taking the difference between scoreBoard and heightBoard. Then, it adds them up, and returns the final score.
     * Javadoc tags part:
     * @param heightBoard 2D array that stores original height of each coordinate
     * @param scoreBoard 2D array that stores lake height + original height of each coordinate
     * @param letterBoard 2D array that stores letter value of lakes
     * @return score value
     */
    public static double calculateScore(int[][] heightBoard, int[][]scoreBoard, String[][] letterBoard){
        double score = 0;
        ArrayList<String> lakeNames = new ArrayList<>();

        //finding lakenames
        for(int i = 0; i < letterBoard.length; i++){
            for (int j = 0; j < letterBoard[0].length; j++){
                if(letterBoard[i][j] != null && !lakeNames.contains(letterBoard[i][j])){
                    lakeNames.add(letterBoard[i][j]);
                }
            }
        }

        int[] volumes = new int[lakeNames.size()];

        //adding the score value of the coordinate to the corresponding index of scores array
        for(int i = 0; i < scoreBoard.length; i++) {
            for (int j = 0; j < scoreBoard[0].length; j++) {
                if(letterBoard[i][j] != null){
                    int idx = lakeNames.indexOf(letterBoard[i][j]);
                    volumes[idx] += scoreBoard[i][j] - heightBoard[i][j];
                }
            }
        }
        //adding up all scores
        for(int tempScore : volumes) score += Math.pow(tempScore, 0.5);
        return score;
    }

    /**
     * Javadoc description part:
     * This method marks isLakeBoard if the coordinate is a lake
     * Javadoc tags part:
     * @param heightBoard 2D array that stores original height of each coordinate
     * @param scoreBoard 2D array that stores lake height + original height of each coordinate
     * @param isLakeBoard a 2D array that stores isLake data  0--> not checked, 1--> lake, -1-->not lake, 2-->marked
     */
    public static void markLakes(int[][] heightBoard, int[][]scoreBoard, int[][]isLakeBoard){
        for(int i = 0; i < scoreBoard.length; i++){
            for(int j = 0; j < scoreBoard[0].length; j++){
                if(scoreBoard[i][j] > heightBoard[i][j]) isLakeBoard[i][j] = 1;
                else isLakeBoard[i][j] = -1;
            }
        }
    }
}