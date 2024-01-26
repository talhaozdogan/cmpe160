/**
 * Javadoc description part:
 * The program is an imitation of the game "Bubble Trouble"
 * This class is the Player class of the game. Position updates of the Player are done here.
 * Javadoc tags part:
 * @since Date: 15.04.2023
 */

public class Player {
    public static final int PERIOD_OF_PLAYER = 6000;
    public static final double playerHeight = 10.0/8.0;
    public static final double playerWidth = playerHeight * (23.0/37.0);

    private double xPosition = 8.0;
    private double yPosition = 0.0 + playerHeight/2;

    //velocity = (distance / time) * (time spent on each loop)
    private double velocity = (16.0 / PERIOD_OF_PLAYER) * (Environment.PAUSE_DURATION + 30);

    //constructor
    Player(){
    }

    //move method of the player. if the direction is +1, the player moves to the right, if the direction is -1, the player moves to the left
    void move(int direction){
        if(direction == -1 && xPosition + velocity*direction > playerWidth/2){
            xPosition += velocity*direction;
        }
        else if(direction == 1 && xPosition + velocity*direction + playerWidth < 16.0 + playerWidth/2){
            xPosition += velocity*direction;
        }
    }

    //getter of the xPosition
    public double getxPosition() {
        return xPosition;
    }

    //getter of the yPosition
    public double getyPosition() {
        return yPosition;
    }

    //getter of the playerHeight
    public double getPlayerHeight() {
        return playerHeight;
    }

    //getter of the playerWidth
    public double getPlayerWidth() {
        return playerWidth;
    }

    //draw method that draws the picture of the player to the canvas
    public void draw(){
        StdDraw.picture(xPosition, yPosition, "player_back.png", playerWidth, playerHeight);
    }
}
