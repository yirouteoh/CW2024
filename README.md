#Project Title: Sky Battle Game (rmb to put table content here)


**GitHub**
[GitHub Repository Link](https://github.com/YourUsername/YourRepository)


**Compilation Instructions**
To compile and run the application, follow these steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/YourUsername/YourRepository.git

2. Navigate to the project directory:
   cd YourRepository

3. Add dependencies:
   Ensure you have JavaFX set up as it is required for this project.
   If using IntelliJ, go to File > Project Structure > Libraries and add the path to JavaFX SDK.

4. Run the application from your IDE (e.g., IntelliJ or Eclipse), ensuring that the necessary JavaFX configurations are set up. If running from the command line, use the following:
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar SkyBattle.jar


**Implemented and Working Properly**
##UserPlane:
1. UserPlane Element: UserPlane element is display when player runs the game.
2. UserPlane Control: The UserPlane responds to **UP AND DOWN arrow keys**, allowing smooth **vertical movement** within screen boundaries, providing reponsive gameplay.
3. UserPlane Projectile Firing: When the UserPlane fires a projectile by **pressing spacebar in keyboard**, it can **hit and destroy EnemyPlane** instances. Upon impact, the **EnemyPlane disappears**, simulating a successful hit and providing **visual feedback** to the player.

##Enemy:
1. EnemyPlane Display: EnemyPlanes appear at intervals when player runs the game, **moving towards the UserPlane**. They are correctly removed upon destruction, updating the player’s score and maintaining gameplay flow.
2. EnemyProjectile Behavior: EnemyProjectiles are **fired by enemy planes**, moving toward the UserPlane. Projectiles are **removed** on collision or when off-screen, with accurate collision detection for player damage.

##UserPlane and enemy interactions:
1. Enemy Interaction: Projectile **fired by UserPlane** correctly **distroyed EnemyPlane**. Then, **EnemyPlane disappears**.
2. Health Display: Health level **decrease** when EnemyPlane and EnemyProjectile **collides with the Userplane**.

##Level Transition:
1. Players successfully **transition from Level One to Level Two** upon achieving the required kill count.
2. Boss Behavior in Level Two: The boss in Level Two has **unique movement patterns** and a shield activation mechanism.

##Projectile Collision with Boss:
1. A custom collision detection logic ensures that projectiles only damage the Boss when they **truly collide with its custom-defined hitbox**.
2. Improved hitbox accuracy for the Boss plane to prevent projectiles from causing damage when they are just near the Boss.

**New Java Class**

**Modified Java Class**
1. Boss
    Changes made: 
    - Added a getCustomHitbox method to define a precise collision area for the Boss instead of relying on default bounding boxes.
    - Adjusted the position recalibration logic to ensure the Boss stays within bounds.
    - Ensured projectiles only interact with the custom hitbox for collisions.
   Reason: 
    - The default collision logic allowed projectiles to damage the Boss when they were visually near but not actually intersecting it. These changes ensure fairness and improve gameplay by making the collisions accurate.

2. LevelParent
    Changes made: 
    - Updated the handleCollisions method to check for the Boss's custom hitbox while keeping existing collision logic for other actors.
    - Stopped the Timeline during level transitions to prevent overlapping game loops that caused performance issues.
   Reason:
    - Without the custom hitbox integration, the Boss's collisions would remain inaccurate. Stopping the Timeline resolved lag and ensured smooth transitions between levels.

3. Levelone
    Changes made:
    - Adjusted the level transition logic to use the goToNextLevel method in LevelParent.
   Reason:
    - Previous hardcoded logic for transitions caused errors when moving to LevelTwo. This update ensures a dynamic and error-free transition system.

4. Leveltwo
    Changes made:
   - Integrated Boss mechanics and limited enemy spawning.
   Reason:
   - To optimize performance and ensure the Boss works correctly with updated collision logic.

5. HeartDisplay.java
   Changes made:
   - Fields containerXPosition, containerYPosition, and numberOfHeartsToDisplay were declared as final to ensure that these values, once set during construction, do not change throughout the lifecycle of an object, emphasizing immutability.
   - Added null check before using toExternalForm() on the image URL to prevent NullPointerException if the image resource is not found.
   Reason:
   - Making fields final aligns with best practices for class design where these values should not change after initialization, enhancing thread safety and predictability of the object's state.
   - Implementing a null check prevents application crashes due to missing resources, improving the robustness and reliability of the application.

6. WinImage.java
   Changes made: 
   - Adjusted the destroy() method to only allow setting isDestroyed to true. This reflects the irreversible state change of an object being destroyed.
   Reason:
   - This change clarifies the intended use of the destroy() method, ensuring that the destruction state is final and cannot be mistakenly reverted, which aligns with the typical game logic where objects, once destroyed, do not return to an active state.

7. GameOverImage.java
   Changes made: 
   - Implemented a robust resource loading strategy with error handling for the game over image, similar to other image handling updates in the project.
   - Consistency in error handling across all classes that load images ensures that missing images are handled gracefully and errors are logged, improving maintainability and user experience.
