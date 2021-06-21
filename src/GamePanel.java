import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{


    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    GamePanel (){
        newPaddles();
        newBall();
        score = new Score(Finals.GAME_WIDTH,Finals.GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(Finals.SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        random = new Random();
        ball = new Ball((Finals.GAME_WIDTH/2)-(Finals.BALL_DIAMETER/2),random.nextInt(Finals.GAME_HEIGHT-Finals.BALL_DIAMETER),Finals.BALL_DIAMETER,Finals.BALL_DIAMETER);
    }
    public void newPaddles() {
        paddle1 = new Paddle(0,(Finals.GAME_HEIGHT/2)-(Finals.PADDLE_HEIGHT/2),Finals.PADDLE_WIDTH,Finals.PADDLE_HEIGHT,1);
        paddle2 = new Paddle(Finals.GAME_WIDTH-Finals.PADDLE_WIDTH,(Finals.GAME_HEIGHT/2)-(Finals.PADDLE_HEIGHT/2),Finals.PADDLE_WIDTH,Finals.PADDLE_HEIGHT,2);
    }
    public void paint(Graphics g) {
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }
    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
        Toolkit.getDefaultToolkit().sync();

    }
    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }
    public void checkCollision() {

        if(ball.y <=0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y >= Finals.GAME_HEIGHT-Finals.BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if(ball.yVelocity>0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if(ball.yVelocity>0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(paddle1.y<=0)
            paddle1.y=0;
        if(paddle1.y >= (Finals.GAME_HEIGHT-Finals.PADDLE_HEIGHT))
            paddle1.y = Finals.GAME_HEIGHT-Finals.PADDLE_HEIGHT;
        if(paddle2.y<=0)
            paddle2.y=0;
        if(paddle2.y >= (Finals.GAME_HEIGHT-Finals.PADDLE_HEIGHT))
            paddle2.y = Finals.GAME_HEIGHT-Finals.PADDLE_HEIGHT;
        if(ball.x <=0) {
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2: "+score.player2);
        }
        if(ball.x >= Finals.GAME_WIDTH-Finals.BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: "+score.player1);
        }
    }
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks =60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true) {
            long now = System.nanoTime();
            delta += (now -lastTime)/ns;
            lastTime = now;
            if(delta >=1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}