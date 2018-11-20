package main;

import core.GameManager;

import java.awt.image.BufferedImage;

public interface Battleable
{
    public static final BufferedImage RED_TEAM = GameManager.loadImage("redTeam.png");
    public static final BufferedImage BLUE_TEAM = GameManager.loadImage("greenTeam.png");
    public abstract void applyDamage(Battleable attacker, double damage);
    
}
