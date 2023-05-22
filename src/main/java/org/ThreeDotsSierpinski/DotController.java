//
//        -2,147,483,648 до -715,827,882 (Integer.MIN_VALUE до Integer.MIN_VALUE / 3)
//        -715,827,881 до 715,827,881 (Integer.MIN_VALUE / 3 + 1 до Integer.MAX_VALUE / 3)
//        715,827,882 до 2,147,483,647 (Integer.MAX_VALUE / 3 * 2 до Integer.MAX_VALUE)
//
//        1,431,655,767
//        1,431,655,763
//        1,431,655,766
//
//        1,431,655,767 + 1,431,655,763 + 1,431,655,766 = 4,294,967,296
//
//
package org.ThreeDotsSierpinski;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DotController extends JPanel {
    private final RndNumGeneratorService qrngService;  // Add this field
    private static final int SIZE = 1050; //  size of the plane
    public static final int HEIGHT1 = 10;
    public static final int WIDTH1 = 10;
    public static final int DELAY_TIME = 1000;
    public static final int WIDTH2 = 10;
    public static final int HEIGHT2 = 10;
    Point dot;
    private final List<Dot> dots; //  list of dots
    private final RndNumProvider dice;
    private int dotCounter; // counter of the number of dots

    public int getDotCounter() {
        return dotCounter;
    }

    public DotController(RndNumGeneratorService qrngService) {
        setPreferredSize(new Dimension(SIZE, SIZE));
        dot = new Point(SIZE / 2, SIZE / 2);
        dots = new ArrayList<>();

        this.qrngService = qrngService;
        dice = new RndNumProvider(this.qrngService);

        dotCounter = 0;

        setBackground(new Color(176, 224, 230));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Dot dot : dots) {
            long diffInMillis = new Date().getTime() - dot.creationDate.getTime();
            long diffInSeconds = diffInMillis / DELAY_TIME;
            float alpha = 1f - Math.min(0.7f, diffInSeconds / 30f);

            alpha = Math.max(alpha, 0.3f);
            Color c = new Color(0, 0, 0, (int) (alpha * 255));

            if (alpha <= 0.3f)
                c = new Color(0.0f, 0.0f, 0.0f, alpha);

            g2d.setColor(c);
            g2d.fillOval(dot.point.x, dot.point.y, WIDTH2, HEIGHT2);
        }

        // Drawing the last dot in red
        if (!dots.isEmpty()) {
            g2d.setColor(Color.RED);
            Dot lastDot = dots.get(dots.size() - 1);
            g2d.fillOval(lastDot.point.x, lastDot.point.y, WIDTH2, HEIGHT2);
        }

        Font myFont1 = new Font("Sans Serif", Font.ITALIC, 32); // adjust font name, style and size as needed
        Font myFont2 = new Font("Sans Serif", Font.ITALIC, 78); // adjust font name, style and size as needed

        // adjust alpha value from 0 (completely transparent) to 255 (completely opaque)
        int alpha1 = 128; // half transparent
        int alpha2 = 64; // more transparent

        g2d.setFont(myFont1);
        g2d.setColor(new Color(105, 105, 105, alpha1));  // blue text with adjusted transparency
        String text = "Dot Counter  ";
        int textX = SIZE - 50; // adjust these values to place the text in the desired location
        int textY = SIZE - 120;
        g2d.drawString(text, textX, textY);

        g2d.setFont(myFont2);
        g2d.setColor(new Color(105, 105, 105, alpha2));  // dark gray text with adjusted transparency
        String counter = String.valueOf(getDotCounter()); // get the counter value as a string
        int counterX = textX + g2d.getFontMetrics(myFont1).stringWidth(text); // place the counter right after the text
        g2d.drawString(counter, counterX, textY);

        // do the same for the second line of text
        g2d.setFont(myFont1);
        g2d.setColor(new Color(105, 105, 105, alpha1));  // blue text with adjusted transparency
        String text2 = "Rnd Value  ";
        int textX2 = SIZE - 50; // adjust these values to place the text in the desired location
        int textY2 = SIZE - 30;
        g2d.drawString(text2, textX2, textY2);

        g2d.setFont(myFont2);
        g2d.setColor(new Color(105, 105, 105, alpha2));  // dark gray text with adjusted transparency
        String value = String.valueOf(dice.rollDice()); // get the dice value as a string
        int valueX = textX2 + g2d.getFontMetrics(myFont1).stringWidth(text2); // place the value right after the text
        g2d.drawString(value, valueX, textY2);
    }

    public void moveDot() {
        int roll = dice.rollDice();

        if (roll <= Integer.MIN_VALUE / 3 || roll > Integer.MAX_VALUE / 3 * 2) {
            dot.x = dot.x / 2;
            dot.y = dot.y / 2;
        } else if (roll <= Integer.MAX_VALUE / 3) {
            dot.x = SIZE / 2 + dot.x / 2;
            dot.y = dot.y / 2;
        } else {
            dot.x = dot.x / 2;
            dot.y = SIZE / 2 + dot.y / 2;
        }

        dots.add(new Dot(new Point(dot.x, dot.y), new Date()));
        dotCounter++;

        repaint();
    }

}
