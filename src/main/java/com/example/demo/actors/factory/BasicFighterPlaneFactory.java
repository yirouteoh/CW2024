package com.example.demo.actors.factory;

import com.example.demo.actors.plane.BasicFighterPlane;
import com.example.demo.actors.plane.FighterPlane;

import java.util.Random;

public class BasicFighterPlaneFactory implements FighterPlaneFactory {
    private final Random random = new Random();

    @Override
    public FighterPlane createEnemy(double screenWidth, double enemyMaximumYPosition) {
        double yPosition = random.nextDouble() * enemyMaximumYPosition;
        return new BasicFighterPlane(screenWidth, yPosition); // Replace with your actual concrete FighterPlane subclass
    }
}
