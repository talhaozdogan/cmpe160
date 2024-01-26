/**
 * Javadoc description part:
 * The program is an imitation of the game "Bubble Trouble"
 * This class is the Bar class of the game. The bar is to show the Remaining Time/TOTAL_GAME_DURATION.
 * Javadoc tags part:
 * @since Date: 15.04.2023
 */

public class Bar {
    //at first remaining time ratio is 1
    private double remaining = 1.0;
    private boolean isGameOver = false;

    //constructor
    Bar(){
    }

    //getter of the isGameOver
    public boolean isGameOver() {
        return isGameOver;
    }

    //the update method takes startTime and currentTime, then calculates how much of TOTAL_GAME_DURATION is spent
    public void update(long startTime, long currentTime){
        long timeDifference = currentTime - startTime;
        if(timeDifference > Environment.TOTAL_GAME_DURATION) isGameOver = true;
        else remaining = 1 -((double) timeDifference / Environment.TOTAL_GAME_DURATION);
    }

    //draw method draws a bar, whose color and length changes while the game continues, on a background image
    public void draw(){
        StdDraw.picture(8.0, -0.5, "bar.png", 16.0,1);
        if(!isGameOver){
            StdDraw.setPenColor(255,(int)(255*remaining), 0);
            StdDraw.filledRectangle(0.5*(16.0*remaining), -0.5, 0.5*(16.0*remaining),0.25);
        }
    }
}
