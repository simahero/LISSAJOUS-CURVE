import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main implements Runnable {

    public class Point {

        double x;  // the point's x-coordinate
        double y;  // the point's y-coordinate
        double angle;  // the point's angle, used later to increase it differently
        Color c;  // the point's color

        public Point(int x, int y, double angle, Color c) {
            this.x = x;
            this.y = y;
            this.angle = angle;
            this.c = c;
        }
    }

    JFrame frame;
    Canvas canvas;
    int width = 900;  // screen width
    int height = 930;  // screen height
    int offset = 30;  //gap between circles
    int scale = 95; //scale every point to it's place on the canvas
    ArrayList<Point> pointsX = new ArrayList<>();  // guide points on the X-axis
    ArrayList<Point> pointsY = new ArrayList<>();  // guide points on the Y-axis
    ArrayList<Point> lines = new ArrayList<>();


    public Main() {
        // MAKE THE FRAME, START THE THREAD
        frame = new JFrame("LISSAJOUS CURVE!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(canvas = new Canvas());
        canvas.setBackground(new Color(46, 46, 46, 253));
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        new Thread(this).start();
    }

    // MAIN LOOP FOR ANIMATION
    @Override
    public void run() {
        BasicTimer bt = new BasicTimer(6000);
        init();
        while (true) {
            bt.sync();
            render();
            update();
        }
    }

    // INITIALIZE THE GUIDE POINTS
    public void init() {
        Point x1 = new Point(0, 1, 0, new Color(0, 0, 0));

        Point x2 = new Point(0, 1, 0, new Color(210, 108, 118));
        Point x3 = new Point(0, 1, 0, new Color(202, 154, 116));
        Point x4 = new Point(0, 1, 0, new Color(231, 233,151));
        Point x5 = new Point(0, 1, 0, new Color(115, 200,116));
        Point x6 = new Point(0, 1, 0, new Color(125, 202,214));
        Point x7 = new Point(0, 1, 0, new Color(225, 135,174));

        Point y1 = new Point(0, 1, 0, new Color(0, 0, 0));

        Point y2 = new Point(0, 1, 0, new Color(210, 108, 118));
        Point y3 = new Point(0, 1, 0, new Color(202, 154, 116));
        Point y4 = new Point(0, 1, 0, new Color(231, 233,151));
        Point y5 = new Point(0, 1, 0, new Color(115, 200,116));
        Point y6 = new Point(0, 1, 0, new Color(125, 202,214));
        Point y7 = new Point(0, 1, 0, new Color(225, 135,174));

        pointsX.add(x1);
        pointsX.add(x2);
        pointsX.add(x3);
        pointsX.add(x4);
        pointsX.add(x5);
        pointsX.add(x6);
        pointsX.add(x7);

        pointsY.add(y1);
        pointsY.add(y2);
        pointsY.add(y3);
        pointsY.add(y4);
        pointsY.add(y5);
        pointsY.add(y6);
        pointsY.add(y7);

    }

    // DRAW TO THE CANVAS
    private void render() {
        BufferStrategy bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(2);
            return;
        }
        Graphics graphics = bs.getDrawGraphics();
        Graphics2D g = (Graphics2D)graphics;
        g.setColor(new Color(46, 46, 46, 253));
        g.fillRect(0, 0, width, height);
        if (pointsX.get(1).angle > 0.111) {
            reset();
        }

        /**************************************
         *     DRAW GUIDE CIRCLES (X)
         *************************************/
        for (int i = 1; i < pointsX.size(); i++) {
            //DRAW GUIDE CIRCLES ON THE X-AXIS
            g.setColor(pointsX.get(i).c);
            g.setStroke(new BasicStroke(3));
            g.drawOval(25 + (i * offset) + (i * scale), (offset), scale, scale);
        }

        /**************************************
         *     DRAW GUIDE CIRCLES (Y)
         *************************************/
        for (int i = 1; i < pointsY.size(); i++) {
            //DRAW GUIDE CIRCLES ON THE Y-AXIS
            g.setColor(pointsY.get(i).c);
            g.drawOval((offset), 25 + (i * offset) + (i * scale), scale, scale);
        }

        /**************************************
         *    DRAW GUIDE POINTS ON Y-AXIS
         *************************************/
        for (int j = 1; j < pointsY.size(); j++) {
            Point c = pointsY.get(j);
            g.setColor(Color.WHITE);
            g.drawOval(offset + (int)(scale / 2 * (c.x + 1)), offset + (int)(j * (scale + offset) + scale / 2 * (c.y + 1)) - 5, 5, 5);

        }

        /**************************************
         *    DRAW GUIDE POINTS ON X-AXIS
         *************************************/
        for (int k = 1; k < pointsX.size(); k++) {
            Point c = pointsX.get(k);
            g.setColor(Color.WHITE);
            g.drawOval(offset + (int) (k * (scale + offset) + scale / 2 * (c.x + 1)) - 5, offset + (int) (scale / 2 * (c.y + 1)), 5, 5);
        }

        /**************************************
         *         DRAW INTERSECTIONS
         *************************************/
        for (int l = 1; l < pointsX.size(); l++){
            for (int m = 1; m < pointsY.size(); m++){
                int tempX = (int)((pointsX.get(l).x+1)*scale/2);
                int tempY = (int)((pointsY.get(m).y+1)*scale/2);
                int countedX = offset + tempX + l * (scale + offset) - 5;
                int countedY = offset + tempY + m * (scale + offset) - 5;
                lines.add(new Point(countedX, countedY, 0, null));
                g.setColor(Color.WHITE);
                //g.drawOval(offset + tempX + l * (scale + offset) - 5, offset + tempY + m * (scale + offset) - 5, 5, 5);
                g.setStroke(new BasicStroke(2));
                for (Point p : lines)
                g.drawLine((int)p.x, (int)p.y, (int)p.x, (int)p.y);
            }
        }

        /**************************************
         *          DRAW GUIDELINES
         *************************************/
        for (int j = 1; j < pointsY.size(); j++) {
            Point c = pointsY.get(j);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(1));
            float[] dashingPattern1 = {1f, 5f};
            Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
            g.setStroke(stroke1);
            g.drawLine(0, offset + (int)(j * (scale + offset) + scale / 2 * (c.y + 1)) - 2, width, offset + (int)(j * (scale + offset) + scale / 2 * (c.y + 1)) - 2);
        }
        for (int k = 1; k < pointsX.size(); k++) {
            Point c = pointsX.get(k);
            g.setColor(Color.WHITE);
            g.drawLine(offset + (int) (k * (scale + offset) + scale / 2 * (c.x + 1)) - 2, 0, offset + (int) (k * (scale + offset) + scale / 2 * (c.x + 1)) - 2, height);
        }

        g.dispose();
        bs.show();

    }

    // UPDATE THE POINTS COORDINATES
    public void update() {
        double angleoffset = 0.0001;
        for (int i = 0; i < pointsX.size(); i++) {
            Point c = pointsX.get(i);
            c.x = Math.cos(Math.toDegrees(c.angle));
            c.y = Math.sin(Math.toDegrees(c.angle));
            c.angle += i * angleoffset;

        }
        for (int i = 0; i < pointsY.size(); i++) {
            Point c = pointsY.get(i);
            c.x = Math.cos(Math.toDegrees(c.angle));
            c.y = Math.sin(Math.toDegrees(c.angle));
            c.angle += i * angleoffset;

        }
    }

    // RESETS THE BOARD AFTER A FULL CURVE
    public void reset() {
        for (Point p : pointsX) {
            p.angle = 0;
        }
        for (Point p : pointsY) {
            p.angle = 0;
        }
        lines.clear();
    }

    public static void main(String[] args) {
        new Main();
    }

}
