package core;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GameManager
{
    private GameManager()
    {
    }

    public static Scene scene;
    public static Handler handler;

    public static void tick()
    {
        if (scene != null)
            scene.tick();
        if (KeyInput.isKeyPressed(KeyEvent.VK_ESCAPE))
            closeOperator();
    }

    public static void render(Graphics2D g2d)
    {
        if (scene != null)
            scene.render(g2d);
    }
    public static boolean endMake;
    public static <T extends Scene> void changeScene(Class<T> cls)
    {

        handler.clear();
        MouseInput.clearEvent();
        try
        {
            endMake = false;
            cls.getConstructor(Handler.class).newInstance(handler);
            while(!endMake);
            System.out.println(scene);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void closeOperator()
    {
        System.exit(0);
    }

    public static boolean isPointInCircle(double px, double py, double circleX, double circleY, double R)
    {
        double dist = Math.sqrt((px - circleX) * (px - circleX) + (py - circleY) * (py - circleY));
        if (dist <= R) return true;
        return false;
    }

    public static boolean circleRectangleIntersection(double x, double y, double range, Rectangle rec)
    {
        if (rec.x <= x && x <= rec.x + rec.width || rec.y <= y && y <= rec.y + rec.height)
        {
            Rectangle ex = new Rectangle(rec.x, rec.y, rec.width, rec.height);
            ex.grow(2 * (int) range, 2 * (int) range);
            ex.x -= range;
            ex.y -= range;
            if (ex.contains(x, y)) return true;
        } else
        {
            int dx[] = {0, rec.width, 0, rec.width};
            int dy[] = {rec.height, 0, 0, rec.height};
            for (int i = 0; i < 4; ++i)
                if (isPointInCircle(rec.x + dx[i], rec.y + dy[i], x, y, range)) return true;
        }
        return false;
    }

    private static HashMap<String, BufferedImage> imageHashMap = new HashMap<>();

    public static BufferedImage loadImage(String path)
    {
        if (imageHashMap.containsKey(path))
            return imageHashMap.get(path);
        BufferedImage image = null;
        try
        {
            if (!new File(path).exists()) System.out.println("Adsf");
            image = ImageIO.read(new File(path));

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        imageHashMap.put(path, image);
        return image;
    }

    public static BufferedImage loadImage(String path, Color edgeColor)
    {
        if (imageHashMap.containsKey(path + ".edge" + edgeColor.toString()))
        {
            //System.out.println("fast1");
            return imageHashMap.get(path + ".edge" + edgeColor.toString());

        }
        BufferedImage image = loadImage(path);
        BufferedImage res = setEdge(image, edgeColor);
        imageHashMap.put(path + ".edge" + edgeColor.toString(), res);
        return res;
    }
    private static HashMap<Integer, BufferedImage> edgedImage = new HashMap<>();
    public static BufferedImage setEdge(BufferedImage image, Color edgeColor)
    {
        if (edgedImage.containsKey((image.hashCode() + edgeColor.toString()).hashCode()))
        {
            //System.out.println("fast2");
            return edgedImage.get((image.hashCode() + edgeColor.toString()).hashCode());
        }
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int dr[] = {0, 1, 0, -1}, dc[] = {-1, 0, 1, 0};
        int[][] map = new int[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); ++i)
            for (int j = 0; j < image.getHeight(); ++j)
            {
                res.setRGB(i, j, image.getRGB(i, j));
                int c1, c2;
                c1 = c2 = 0;
                for (int q = 0; q < 4; ++q)
                {
                    int nr = dr[q] + i;
                    int nc = dc[q] + j;
                    if (nr < 0 || nr >= image.getWidth()) continue;
                    if (nc < 0 || nc >= image.getHeight()) continue;
                    if (image.getRGB(nr, nc) == 0)
                    {
                        c1++;
                    } else
                        c2++;
                }
                if (c1 != 0 && c2 != 0) map[i][j] = 1;
            }
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight(); ++j)
            {
                if (map[i][j] == 1) res.setRGB(i, j, edgeColor.getRGB());
            }
        }
        edgedImage.put((image.hashCode() + edgeColor.toString()).hashCode(), res);
        return res;
    }
    public static void playSound(String file, boolean Loop)
    {
        try
        {
            AudioInputStream au= AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(au);
            clip.start();
            if (Loop) clip.loop(-1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
