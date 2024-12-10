package com.example.demo.actors.factory;

import com.example.demo.actors.projectile.*;
import com.example.demo.actors.projectile.BossProjectile;
import com.example.demo.actors.projectile.UserProjectile;
import com.example.demo.actors.projectile.EnemyProjectile;

/**
 * Factory class for creating projectiles based on their type.
 */
public class ProjectileFactory {

    /**
     * Enumeration of projectile types.
     */
    public enum ProjectileType {
        BOSS,
        ENEMY,
        USER
    }

    /**
     * Creates a projectile based on the given type and initial positions.
     *
     * @param type         The type of projectile to create.
     * @param initialXPos  The initial X position of the projectile.
     * @param initialYPos  The initial Y position of the projectile.
     * @return The created projectile instance.
     */
    public static Projectile createProjectile(ProjectileType type, double initialXPos, double initialYPos) {
        switch (type) {
            case BOSS:
                return new BossProjectile(initialYPos); // BossProjectile only requires Y
            case ENEMY:
                return new EnemyProjectile(initialXPos, initialYPos);
            case USER:
                return new UserProjectile(initialXPos, initialYPos);
            default:
                throw new IllegalArgumentException("Unsupported projectile type: " + type);
        }
    }
}
