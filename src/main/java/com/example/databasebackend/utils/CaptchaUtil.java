package com.example.databasebackend.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

public class CaptchaUtil {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;
    private static final int CODE_LENGTH = 4;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final int NUM_LINES = 5;
    private static final int NUM_POINTS = 100;

    public static Captcha generateCaptcha(int distortionStrength) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Set background color
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char c = CHARACTERS.charAt(index);
            captchaText.append(c);
        }

        // Draw text with distortion
        Font font = new Font("Arial", Font.BOLD, 30);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();

        int totalWidth = metrics.stringWidth(captchaText.toString());
        int charWidth = totalWidth / CODE_LENGTH;

        int x = (WIDTH - totalWidth) / 2;
        int y = (HEIGHT - metrics.getHeight()) / 2 + metrics.getAscent();

        for (char c : captchaText.toString().toCharArray()) {
            int yOffset = random.nextInt(distortionStrength) - distortionStrength / 2;
            g2d.setColor(TEXT_COLOR);
            g2d.drawString(String.valueOf(c), x, y + yOffset);
            x += charWidth;
        }

        // Draw lines
        for (int i = 0; i < NUM_LINES; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Draw points
        for (int i = 0; i < NUM_POINTS; i++) {
            int xPoint = random.nextInt(WIDTH);
            int yPoint = random.nextInt(HEIGHT);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.fillOval(xPoint, yPoint, 2, 2);
        }

        g2d.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        return new Captcha(captchaText.toString(), base64Image);
    }

    public static class Captcha {
        private String text;
        private String imageBase64;

        public Captcha(String text, String imageBase64) {
            this.text = text;
            this.imageBase64 = imageBase64;
        }

        public String getText() {
            return text;
        }

        public String getImageBase64() {
            return imageBase64;
        }
    }
}
