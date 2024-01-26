/**
 * Javadoc description part:
 * The program is an imitation of the game "Bubble Trouble"
 * This class is the Ball class of the game. Collision detections and position updates of the Ball are done here.
 * Javadoc tags part:
 * @since Date: 15.04.2023
 */

public class Ball {
    //setting constant values
    private static final double GRAVITY = 0.005;
    private final double RADIUS_MULTIPLIER = 2.0;
    private final double HEIGHT_MULTIPLIER = 1.75;
    private final int PERIOD_OF_BALL = 15000;
    private int level;
    private final double minPossibleRadius = 10.0 * 0.0175;
    private double radius;
    private final double minHeight = (10.0/8.0) * 1.4;
    private double height;
    private double xPos;
    private double yPos;

    //velocity = (distance / time) * (time spent on each loop)
    private double velocityX = (16.0 / PERIOD_OF_BALL) * (Environment.PAUSE_DURATION + 30);
    private double velocityY = 0.15;

    //constructors
    Ball(){
    }

    Ball(int level, double xPos, double yPos, int direction){
        this.xPos = xPos;
        this.yPos = yPos;
        this.level = level;
        this.radius = Math.pow(RADIUS_MULTIPLIER, level) * minPossibleRadius;
        this.height = minHeight * Math.pow(HEIGHT_MULTIPLIER, level);
        this.velocityX = this.velocityX * direction;
    }
    //getter of the level
    public int getLevel() {
        return level;
    }
    //getter of the xPos
    public double getxPos() {
        return xPos;
    }

    //getter of the yPos
    public double getyPos() {
        return yPos;
    }

    //update method that moves the ball according to projectile motion and elastic collision
    public void update(){
        //x axis
        if(xPos + radius > 16.0 || xPos - radius < 0.0) velocityX = velocityX * -1;
        xPos += velocityX;

        //y axis
        if(yPos - radius < 0.0){
            velocityY = Math.pow(2*GRAVITY*height, 0.5);
        }else{
            //velocity changes due to gravity
            velocityY -= GRAVITY;
        }
        yPos += velocityY;
    }
    //the method draws the ball
    public void draw(){
        StdDraw.picture(xPos, yPos - radius < 0.0  ? radius : yPos, "ball.png", 2*radius, 2*radius);
    }

    //the method checks if the player(rectangle) and the ball(circle) intersects.
    //It finds the closest distance between the ball and the player by considering the location of the ball with respect to the player, and checks if the distance is less than the radius --> collision
    public boolean hitPlayer(Player player){
        //storing player coordinates in variables
        double playerLeftX = player.getxPosition() - player.getPlayerWidth() / 2;
        double playerRightX = player.getxPosition() + player.getPlayerWidth() / 2;
        double playerY = player.getyPosition() + player.getPlayerHeight() / 2;
        double distanceSqrd;
        if(xPos < playerLeftX){
            if(yPos > playerY){
                distanceSqrd = Math.pow((xPos - playerLeftX), 2) + Math.pow((yPos - playerY), 2);
            } else distanceSqrd = Math.pow((xPos - playerLeftX), 2);
        }
        else if(xPos > playerRightX){
            if(yPos > playerY){
                distanceSqrd = Math.pow((xPos - playerRightX), 2) + Math.pow((yPos - playerY), 2);
            } else distanceSqrd = Math.pow((xPos - playerRightX), 2);
        }
        else{
            distanceSqrd = Math.pow(yPos - playerY, 2);
        }

        if(distanceSqrd <= radius*radius) return true;
        else return false;

    }

    //the method checks if the arrow(line) and the ball(circle) intersects.
    //if the arrow is active, the method checks the distance between the center of the ball and the closest point on the arrow to the center
    //if the distance is less than the radius of the ball, it means colllision
    public boolean hitArrow(Arrow arrow){
        double arrowX = arrow.getxPosition();
        double arrowTopY = arrow.getTopYPosition();
        double distanceSqrd;

        if(arrow.getActivity()) {
            if(yPos < arrowTopY) distanceSqrd = Math.pow((xPos - arrowX), 2);
            else distanceSqrd = Math.pow((xPos - arrowX), 2) + Math.pow((yPos - arrowTopY), 2);

            if(distanceSqrd <= radius*radius) return true;
        }
        return false;
    }
}
