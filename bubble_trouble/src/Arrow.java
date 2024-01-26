/**
 * Javadoc description part:
 * The program is an imitation of the game "Bubble Trouble"
 * This class is the Arrow class of the game. Movements and scaling of the arrow are handled here.
 * Javadoc tags part:
 * @since Date: 15.04.2023
 */

public class Arrow {
    private final int PERIOD_OF_ARROW = 1500;
    private double xPosition;
    private double yScale = 0;
    private double velocity = (9.0 / PERIOD_OF_ARROW) * (Environment.PAUSE_DURATION + 30);
    private boolean activity;

    //getter of the xPosition
    public double getxPosition() {
        return xPosition;
    }

    //getter of the yPosition
    public double getTopYPosition() {
        return yScale;
    }

    //getter of the activity
    public boolean getActivity() {
        return activity;
    }

    //setter of the activity
    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    //Constructor
    Arrow(){
        this.activity = false;
    }
    Arrow(double x){
        this.activity = true;
        this.xPosition = x;
    }

    //update moves the arrow to the top. if it reaches to the top, its activity becomes false
    public void update(){
        if (activity){
            if (getTopYPosition() > 9.0) activity = false;
            else yScale += velocity;
        }
    }
    //draws the arrow image and scales if as it goes up
    public void draw(){
        if(activity){
            StdDraw.picture(xPosition, yScale/2, "arrow.png", 9.0*(8.0/480.0), yScale);
        }
    }
}
