import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.ImageIcon;


public class Main {

    static BufferedImage img;
    static ImageIcon icon;
    static JFrame frame;
    static JLabel label;
    static int startX = 0;
    static int startY = 0;

    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors(); // amount of cores
        //Scanner sc = new Scanner(System.in);
        frame = new JFrame();

        // Taking the input from the user
        String fileName = args[0];
        int squareSize = Integer.parseInt(args[1]);
        String processingMode = args[2];

        // Reading the image
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This part is for Single-Threaded Mode
        if (processingMode.equals("S")) {
            blur(0, 0, squareSize, img.getWidth(), img.getHeight());
        }
        // This part is for Multi-Threaded Mode
        else if (processingMode.equals("M")) {
            ArrayList<Thread> threads = new ArrayList<Thread>();

            for (int i = 0; i < cores; i++) {
                int startThreadY = startY + img.getHeight() * i / cores;
                int endThreadY;

                if (i != cores - 1) endThreadY = startY + img.getHeight() * (i + 1) / cores;
                else endThreadY = img.getHeight();

                threads.add(new Thread(() -> blur(startX, startThreadY, squareSize, img.getWidth(), endThreadY)));
                threads.get(i).start();
            }
        }

        /* xEnd and yEnd are the coordinates until which the image should be blurred
         *   In Single-threaded mode xEnd and yEnd are the right and lowest point
         *   (as we start from the top left and finish at top right)
         *   For the Multi-Threaded mode xEnd and yEnd are the starting points of the next Threads',
         * not to paint again the changed colors
         */

        else System.out.println("Please print either S or M");

        try {
            File resultImage = new File("result.jpg");
            ImageIO.write(img, "jpg", resultImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This is where all of the process happens
    // (I named it blur because it reminds me the process of blurring the image)
    static void blur(int x, int y, int n, int xEnd, int yEnd) {
        icon = new ImageIcon(img);
        label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        // The process of changing color for the boxes of the given size happens here
        for (int j = y; j < yEnd; j = j + n) {
            for (int i = x; i < xEnd; i = i + n)
                avgColor(i, j, n, xEnd, yEnd);
            icon.setImage(img);
        }
        frame.setVisible(true);
    }

    static void avgColor(int x, int y, int n, int xEnd, int yEnd) {
        Color avg;
        ArrayList<Integer> totalRed = new ArrayList<Integer>();
        ArrayList<Integer> totalGreen = new ArrayList<Integer>();
        ArrayList<Integer> totalBlue = new ArrayList<Integer>();

        // Here i basically get RGB values for each of the color
        for (int yy = y; yy < y + n && yy < yEnd; yy++) {
            for (int xx = x; xx < x + n && xx < xEnd; xx++) {
                int pixel = img.getRGB(xx, yy);
                Color c = new Color(pixel, true);
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                totalRed.add(red);
                totalGreen.add(green);
                totalBlue.add(blue);
            }
        }

        // here I find the average of the values of each color
        int avgRed = average(totalRed);
        int avgBlue = average(totalBlue);
        int avgGreen = average(totalGreen);

        // after finding each of RGB color values, I get the new average Color for the given square box
        avg = new Color(avgRed, avgGreen, avgBlue);

        // here I change the color of every pixel in the box
        for (int i = x; i < x + n && i < xEnd; i++) {
            for (int j = y; j < y + n && j < yEnd; j++) {
                img.setRGB(i, j, avg.getRGB());
                frame.repaint();
                frame.revalidate();
            }
        }
    }

    // method for counting the average of the ArrayList
    static int average(ArrayList<Integer> arr) {
        int sum = 0;
        for (int i = 0; i < arr.size(); i++)
            sum += arr.get(i);
        return sum / arr.size();
    }

}
