/**
 * Javadoc description part:
 * The program is an imitation of the game "Bubble Trouble"
 * This class is the Environment class of the game. Ball, Player, Bar, and Arrow objects are created here.
 * Also, collisions and game status are checked here.
 * Javadoc tags part:
 * @since Date: 15.04.2023
 */

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Environment {

    //setting constant values
    public static final int canvasWidth = 800;
    public static final int canvasHeight = 500;
    public static final int TOTAL_GAME_DURATION = 40000;
    public static final int PAUSE_DURATION = 5;
    private boolean isGameOver = false;
    private boolean won = false;
    private final long startTime;

    //creating the objects
    Player player = new Player();
    Arrow arrow = new Arrow();
    Bar bar = new Bar();
    ArrayList<Ball> balls = new ArrayList<>();

    //constructor
    Environment(){
        this.startTime = System.currentTimeMillis();

        //adding first three balls to the balls list
        balls.add(new Ball(0, 16.0/4, 0.5, 1));
        balls.add(new Ball(1, 16.0/3, 0.5, -1));
        balls.add(new Ball(2, 16.0/4, 0.5, 1));
    }

    //getter of isGameOver
    public boolean isGameOver() {
        return isGameOver;
    }

    //getter of isWon
    public boolean isWon() {
        return won;
    }

    //run method updates the positions of the objects and checks their interactions
    public void run(){
        //checking if time is over
        isGameOver = bar.isGameOver();

        //if there are no balls left, game is over
        if(balls.size() == 0) won = true;

        //moving the player
        if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) player.move(1);
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) player.move(-1);
        if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE) && arrow.getActivity() == false) arrow = new Arrow(player.getxPosition());


        //updating the arrow
        arrow.update();

        //updating the bar
        bar.update(startTime, System.currentTimeMillis());

        //updating the balls
        for(int i = 0; i < balls.size(); i++){
            balls.get(i).update();
        }

        //checking collisions
        for(int i = 0; i < balls.size(); i++){
            boolean hitPlayer = balls.get(i).hitPlayer(player);
            boolean hitArrow = balls.get(i).hitArrow(arrow);

            //if a ball is hit, two balls are created and added to the balls list
            if(hitArrow){
                arrow.setActivity(false);
                if(balls.get(i).getLevel() > 0){
                    balls.add(new Ball(balls.get(i).getLevel() - 1, balls.get(i).getxPos(), balls.get(i).getyPos(), 1));
                    balls.add(new Ball(balls.get(i).getLevel() - 1, balls.get(i).getxPos(), balls.get(i).getyPos(),-1));
                }
                //the ball which was hit is removed from the list
                balls.remove(i);
            }
            //if the player is hit, the game is over
            if(hitPlayer){
                isGameOver = true;
            }
        }
    }

    //draw method draws all objects and the background
    public void draw() {
        //drawing the background scaling background image
        StdDraw.picture(8.0,4.0, "background.png", 16, 10);

        //drawing the objects
        arrow.draw();
        player.draw();
        bar.draw();
        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).draw();
        }
    }
}
