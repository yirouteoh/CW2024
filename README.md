#Project Title: Sky Battle Game (rmb to put table content here) (add which line in modified class)


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

##Boss:
1. A shield with a glow effect happen randomly, at this moment, it **keeps userprojectile from attacking the boss**. Boss health won't decrease even when it is attacked. 

**New Java Class**

**Modified Java Class**
##Boss
1. Changes made (Encapsulation):
    - Added the `isShielded()` method to provide read access to the `isShielded` field.
   ###Reason:
    - To protect the `isShielded` field and ensure it can only be accessed through a method, reinforcing the principles of encapsulation. 
    - This prevents other classes from directly changing the shield status, thereby maintaining the integrity of the object's state management.

2. Changes made (Shield virtual effect):
    - Enhanced the `updateActor()` method to include conditional logic that adjusts visual effects based on the shield status:
    - When `isShielded()` returns true, a glow effect is applied.
    - When `isShielded()` returns false, any existing effect is removed.
   ###Reason:
   - To provide immediate visual feedback regarding the boss's shield status, enhancing player experience and interaction. 
   - This change helps players visually identify when the boss is shielded and when it is vulnerable.

3. Changes made (`getCustomHitbox()`):
    - Added a getCustomHitbox method to define a precise collision area for the Boss instead of relying on default bounding boxes.
    - djusted the position recalibration logic to ensure the Boss stays within bounds.
    - Ensured projectiles only interact with the custom hitbox for collisions.
   ###Reason:
    - The default collision logic allowed projectiles to damage the Boss when they were visually near but not actually intersecting it. 
    - These changes ensure fairness and improve gameplay by making the collisions accurate.


##LevelParent
1. Changes made (Event Handling and Inter-Component Communication):
    - Added a method `addPropertyChangeListener` to allow the registration of `PropertyChangeListener` objects:
    - This method enables other components of the application to listen for and react to property changes within the `LevelParent` class.
   Reason:
    - Facilitates the communication between different parts of the system by notifying registered listeners of changes to the properties within the `LevelParent` class, 
    - Thereby enhancing the responsiveness and interactivity of the application.

2. Changes made (Dynamic Level Loading and Seamless Transitions):
    -  Implemented the `goToNextLevel` method to transition from the current level to a dynamically specified next level:
    - Stops the current level's timeline.
    - Dynamically loads the next level class using reflection (`Class.forName(levelName)`).
    - Creates an instance of the next level class and initializes the scene.
    - Sets the new scene on the primary stage and starts the next level's game logic.
   Reason:
    - Allows the game to dynamically transition between different levels without hard-coding specific level details, supporting extensibility and scalability of the game. 
    - Facilitates adding new levels or changing existing ones without changing the central game logic, improving maintainability and flexibility of the game's architecture.

3. Changes made (Error Handling):
    - Added the `showErrorDialog` method to display error messages in a dialog box:
    - Utilizes JavaFX's `Alert` class to create and display an error alert with a specific message passed to the method.
    Reason:
    - Enhances the user interface by providing clear and immediate feedback to the user when an error occurs during game operations, such as failing to load a level.

4. Changes made (Collision logic):
   - Added the `handleCollisions` method to manage interactions between projectiles and enemies:
   - The method checks for collisions between each projectile and each enemy.
   - It handles different behaviors based on the type of enemy and whether a boss is shielded.
   Reason:
   - By differentiating the collision response based on the enemy type and states (such as a boss being shielded), it allows for a nuanced interaction model. 
   - This ensures that the game remains challenging yet fair, maintaining the integrity and continuity of gameplay by applying appropriate effects based on the context of the collision.

##Levelone
    Changes made:
    - Adjusted the level transition logic to use the goToNextLevel method in LevelParent.
   Reason:
    - Previous hardcoded logic for transitions caused errors when moving to LevelTwo. This update ensures a dynamic and error-free transition system.

##Leveltwo
    Changes made:
   - Integrated Boss mechanics and limited enemy spawning.
   Reason:
   - To optimize performance and ensure the Boss works correctly with updated collision logic.


##HeartDisplay.java
   Changes made:
   - Fields containerXPosition, containerYPosition, and numberOfHeartsToDisplay were declared as final to ensure that these values, once set during construction, do not change throughout the lifecycle of an object, emphasizing immutability.
   - Added null check before using toExternalForm() on the image URL to prevent NullPointerException if the image resource is not found.
   Reason:
   - Making fields final aligns with best practices for class design where these values should not change after initialization, enhancing thread safety and predictability of the object's state.
   - Implementing a null check prevents application crashes due to missing resources, improving the robustness and reliability of the application.


##WinImage.java
   Changes made: 
   - Adjusted the destroy() method to only allow setting isDestroyed to true. This reflects the irreversible state change of an object being destroyed.
   Reason:
   - This change clarifies the intended use of the destroy() method, ensuring that the destruction state is final and cannot be mistakenly reverted, which aligns with the typical game logic where objects, once destroyed, do not return to an active state.

##GameOverImage.java
   Changes made: 
   - Implemented a robust resource loading strategy with error handling for the game over image, similar to other image handling updates in the project.
   - Consistency in error handling across all classes that load images ensures that missing images are handled gracefully and errors are logged, improving maintainability and user experience.
