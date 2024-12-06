package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the lifecycle of all actors in the game, including friendly units, enemies, projectiles, and power-ups.
 */
public class ActorManager {

    private final List<ActiveActorDestructible> friendlyUnits = new ArrayList<>();
    private final List<ActiveActorDestructible> enemyUnits = new ArrayList<>();
    private final List<ActiveActorDestructible> userProjectiles = new ArrayList<>();
    private final List<ActiveActorDestructible> enemyProjectiles = new ArrayList<>();
    private final List<ActiveActorDestructible> powerUps = new ArrayList<>();

    /**
     * Adds an actor to the specified list and the scene graph.
     *
     * @param actor The actor to add.
     * @param list  The list to track the actor.
     * @param root  The scene graph root group.
     */
    private void addActor(ActiveActorDestructible actor, List<ActiveActorDestructible> list, Group root) {
        if (!list.contains(actor) && !root.getChildren().contains(actor)) {
            list.add(actor);
            root.getChildren().add(actor);
        }
    }

    /**
     * Adds a friendly unit to the game.
     *
     * @param unit The friendly unit to add.
     * @param root The scene graph root group.
     */
    public void addFriendlyUnit(ActiveActorDestructible unit, Group root) {
        addActor(unit, friendlyUnits, root);
    }

    /**
     * Adds an enemy unit to the game.
     *
     * @param unit The enemy unit to add.
     * @param root The scene graph root group.
     */
    public void addEnemyUnit(ActiveActorDestructible unit, Group root) {
        addActor(unit, enemyUnits, root);
    }

    /**
     * Adds a projectile fired by the user.
     *
     * @param projectile The user projectile to add.
     * @param root       The scene graph root group.
     */
    public void addUserProjectile(ActiveActorDestructible projectile, Group root) {
        addActor(projectile, userProjectiles, root);
    }

    /**
     * Adds a projectile fired by an enemy.
     *
     * @param projectile The enemy projectile to add.
     * @param root       The scene graph root group.
     */
    public void addEnemyProjectile(ActiveActorDestructible projectile, Group root) {
        addActor(projectile, enemyProjectiles, root);
    }

    /**
     * Adds a power-up to the game.
     *
     * @param powerUp The power-up to add.
     * @param root    The scene graph root group.
     */
    public void addPowerUp(ActiveActorDestructible powerUp, Group root) {
        addActor(powerUp, powerUps, root);
    }

    /**
     * Updates all actors' states.
     */
    public void updateActors() {
        updateActorList(friendlyUnits);
        updateActorList(enemyUnits);
        updateActorList(userProjectiles);
        updateActorList(enemyProjectiles);
        updateActorList(powerUps);
    }

    /**
     * Helper method to update a specific list of actors.
     *
     * @param list The list of actors to update.
     */
    private void updateActorList(List<ActiveActorDestructible> list) {
        list.forEach(ActiveActorDestructible::updateActor);
    }

    /**
     * Cleans up destroyed actors from the game and the scene graph.
     *
     * @param root The scene graph root group.
     */
    public void cleanUpDestroyedActors(Group root) {
        cleanUpList(friendlyUnits, root);
        cleanUpList(enemyUnits, root);
        cleanUpList(userProjectiles, root);
        cleanUpList(enemyProjectiles, root);
        cleanUpList(powerUps, root);
    }

    /**
     * Helper method to clean up destroyed actors from a specific list and the scene graph.
     *
     * @param list The list of actors to clean up.
     * @param root The scene graph root group.
     */
    private void cleanUpList(List<ActiveActorDestructible> list, Group root) {
        // Use toList() for compatibility with Java 16+.
        List<ActiveActorDestructible> destroyed = list.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .toList();
        root.getChildren().removeAll(destroyed);
        list.removeAll(destroyed);
    }

    /**
     * Getter for enemy units.
     *
     * @return A list of enemy units.
     */
    public List<ActiveActorDestructible> getEnemyUnits() {
        return new ArrayList<>(enemyUnits); // Return a copy to prevent external modifications.
    }

    /**
     * Getter for user projectiles.
     *
     * @return A list of user projectiles.
     */
    public List<ActiveActorDestructible> getUserProjectiles() {
        return new ArrayList<>(userProjectiles); // Return a copy to prevent external modifications.
    }

    /**
     * Getter for enemy projectiles.
     *
     * @return A list of enemy projectiles.
     */
    public List<ActiveActorDestructible> getEnemyProjectiles() {
        return new ArrayList<>(enemyProjectiles); // Return a copy to prevent external modifications.
    }

    /**
     * Getter for friendly units.
     *
     * @return A list of friendly units.
     */
    public List<ActiveActorDestructible> getFriendlyUnits() {
        return new ArrayList<>(friendlyUnits); // Return a copy to prevent external modifications.
    }

    /**
     * Getter for power-ups.
     *
     * @return A list of power-ups.
     */
    public List<ActiveActorDestructible> getPowerUps() {
        return new ArrayList<>(powerUps); // Return a copy to prevent external modifications.
    }
}
