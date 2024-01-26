/**
 * Javadoc description part:
 * The program is an imitation of the game "Bubble Trouble"
 * This class is the Main class of the game. It includes the game loop.
 * Javadoc tags part:
 * @since Date: 15.04.2023
 */

import java.awt.*;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {

        boolean isExit = false;

        //creating the canvas and scaling
        StdDraw.setCanvasSize(Environment.canvasWidth, Environment.canvasHeight);
        StdDraw.setXscale(0.0, 16.0);
        StdDraw.setYscale(-1.0, 9.0);
        StdDraw.enableDoubleBuffering();

        //creating the first environment object. When the game is replayed, this object gets reset.
        Environment environment = new Environment();

        //the game loop. it loops until the user presses N
        while(!isExit){

            //if the game is neither over nor won, objects defined in environment class keep moving and interacting
            if(!(environment.isGameOver() || environment.isWon())) environment.run();

            //draws the pictures and shapes
            environment.draw();

            //if the game is over, a text is written on the canvas and the user is asked if he or she wants to play again or not
            if(environment.isGameOver() || environment.isWon()){
                StdDraw.picture(16.0/2 ,10.0/2.18, "game_screen.png", 16.0/3.8, 10.0/4);
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.setFont(new Font("Helvetica", Font.BOLD, 30));
                if(environment.isGameOver()) StdDraw.text(16.0/2, 10.0/2.0, "Game Over!");
                if(environment.isWon()) StdDraw.text(16.0/2, 10.0/2.0, "You Won!");
                StdDraw.setFont(new Font("Helvetica", Font.ITALIC, 15));
                StdDraw.text(16.0/2, 10.0/2.3, "To Replay Click “Y”");
                StdDraw.text(16.0/2, 10.0/2.6, "To Quit Click “N”");

                //if the user wants to play again, environment object is reset and loop continues the new environment object
                if (StdDraw.isKeyPressed(KeyEvent.VK_Y)){
                    environment = new Environment();
                }

                //if the user doesn't want to playa again, the program exits
                if (StdDraw.isKeyPressed(KeyEvent.VK_N)){
                    isExit = true;
                }
            }

            //showing and clearing the objects
            StdDraw.show();
            StdDraw.pause(Environment.PAUSE_DURATION);
            StdDraw.clear();
        }
        //exits
        System.exit(1);
    }
}