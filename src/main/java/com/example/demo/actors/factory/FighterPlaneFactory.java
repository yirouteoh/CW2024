package com.example.demo.actors.factory;

import com.example.demo.actors.plane.FighterPlane;

public interface FighterPlaneFactory {
    FighterPlane createEnemy(double screenWidth, double enemyMaximumYPosition);
}
