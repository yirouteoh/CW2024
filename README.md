# COMP2042 Coursework

## **Details**
- Written by: Teoh Yi Rou (20508985)
- GitHub Repository for COMP2042_Coursework: https://github.com/yirouteoh/CW2024.git

---------------------------------------------------------------------------------------------------------------------------------------------------------

## **Compilation Instructions:**

## **Pre-requisites**
- SDK: 21 (corretto-21) (Amazon Corretto 21.0.5)
- IDE: IntelliJ IDEA
- JavaFX: 19.0.2

1. Start by cloning the project repository to your local machine. ---  https://github.com/yirouteoh/CW2024.git
2. Set up javaFX
3. Import the Project into IntelliJ IDEA or other IDE
4. Compile and Run the Project

---------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------
## Table of Contents
1. [Details](#details)
2. [Compilation Instructions](#compilation-instructions)
3. [Features](#features)
    - [Implemented and Working Properly](#implemented-and-working-properly)
    - [Implemented but Not Working Properly](#implemented-but-not-working-properly)
    - [Features Not Implemented](#features-not-implemented)
4. [New Java Classes](#new-java-classes)
5. [Modified Java Classes](#modified-java-classes)

---------------------------------------------------------------------------------------------------------------------------------------------------------

# Features
## Implemented and Working Properly

### **1. User Interface (UI)**

#### **1.1 Heart Display**
- **Description**: Dynamically displays the player's health or remaining lives using heart icons that visually update in response to in-game events.
- **Details**: The `HeartDisplay` class manages the display of heart icons by adding them to an `HBox` container using the `addHeartsToContainer()` method. When the player loses health, the `removeHeart()` method updates the display, ensuring synchronization with the player's current health state.

#### **1.2 Kill Count Display**
- **Description**: Provides a real-time display of the total number of enemies defeated by the player, enhancing gameplay feedback.
- **Details**: The `KillCountDisplay` class uses the `incrementKillCount()` method to update the kill count each time an enemy is defeated. The `createDisplay(double x, double y)` method positions a `Text` element on the screen, ensuring visibility and alignment with the game's UI layout.

#### **1.3 Pause Screen**
- **Description**: Enables players to pause the game, offering options to resume, restart, or return to the main menu while maintaining the current game state.
- **Details**: The `PauseScreen` class creates a semi-transparent overlay using the `createPauseOverlay()` method, which houses buttons for `resumeAction`, `restartAction`, and `returnToMenuAction`. The intuitive design ensures seamless interaction without interrupting gameplay flow.

#### **1.4 Game Over Screen**
- **Description**: Displays a game-over screen when the player loses, featuring motivational text and interactive options to restart or exit.
- **Details**: The `GameOverImage` class renders an overlay with a darkened background and motivational text using the `createMotivationalText()` method. Navigation buttons are created via the `createButtonContainer()` method, allowing the player to restart or return to the menu with a single click.

#### **1.5 Win Screen**
- **Description**: Celebrates the player's victory with a congratulatory message and animated effects, providing options to replay or exit the game.
- **Details**: The `WinImage` class uses the `createAndAnimateConfetti()` method to generate colorful animated confetti that enhances the celebratory mood. The `createWinImage()` method centers the victory message on the screen, creating a satisfying end to the game.

#### **1.6 Audio Control Panel**
- **Description**: Allows players to toggle and adjust audio settings, including background music and sound effects, for a customizable gaming experience.
- **Details**: The `AudioControlPanel` class includes buttons created by `createMuteBackgroundMusicButton()` and `createMuteSoundEffectsButton()`. These methods toggle the mute state dynamically, ensuring player preferences are reflected instantly.

#### **1.7 Menu View**
- **Description**: Provides an intuitive main menu interface with options to start the game, access instructions, modify settings and exit.
- **Details**: The `MenuView` class renders the main menu using the `showMenu()` method, featuring a polished layout created by the `createMenuPanel()` method. Buttons are styled and strategically placed for easy navigation, ensuring an engaging first impression.

#### **1.8 Instruction Page**
- **Description**: Offers players a detailed guide on how to interact with the game, including gameplay mechanics and control configurations.
- **Details**: The `InstructionsPage` class provides an overlay displayed using the `show()` method. This overlay includes two pages:
   - The "How to Play" page, created using the `createHowToPlayPage()` method, provides gameplay instructions such as navigation, shooting, and objectives.
   - The "Control Keys" page, generated by the `createSettingsPage()` method, summarizes key bindings for essential actions.
     Navigation between pages is facilitated by the `createNavigationButtons()` method, which dynamically toggles between the pages, and a "Close" button allows players to exit the overlay.

#### **1.9 Audio Settings Page**
- **Description**: Allows players to manage audio preferences, including background music and sound effects, through an intuitive overlay interface.
- **Details**: The `AudioSettingsPage` class uses the `show()` method to display an interactive audio settings overlay. Players can:
   - Toggle background music using the `toggleBackgroundMusic()` method, which updates the button text and icon in real-time.
   - Adjust sound effects with the `toggleSoundEffects()` method, reflecting the current state dynamically.
     Each button has hover effects, implemented with the `addHoverEffect()` method, providing a responsive and polished user interaction experience. The "Close" button, created within the `show()` method, allows users to exit the overlay seamlessly.

### **2. Game Levels**

#### **2.1. Level One**
- **Description**: The introductory level that familiarizes players with basic game mechanics and simple enemy encounters.
- **Details**: The `LevelOne` class uses `spawnEnemyUnits()` to dynamically generate enemies and `initializeFriendlyUnits()` to set up the player plane. Progression to Level Two is triggered by `handleLevelAdvance()` upon reaching the kill target.

#### **2.2. Level Two**
- **Description**: A challenging level featuring more advanced enemies and a boss battle.
- **Details**: Managed in the `LevelTwo` class, the level spawns enemies via `spawnEnemyUnits()` and transitions to Level Three through `handleLevelAdvance()` upon defeating the boss using `isBossDefeated()`.

#### **2.3. Level Three**
- **Description**: The final level with waves of enemies and a climactic boss fight.
- **Details**: The `LevelThree` class spawns enemy waves using `spawnEnemyUnits()` and introduces the final boss via `spawnFinalBoss()`. Victory is achieved by defeating the boss, determined by `isFinalBossDefeated()`.

### **3. Game Actors**

#### **3.1. User Plane**
- **Description**: The player-controlled fighter plane capable of movement, shooting, and utilizing power-ups.
- **Details**: Defined in the `UserPlane` class. The `updatePosition()` method ensures smooth movement within boundaries, while `fireProjectile()` allows firing of regular or spreadshot projectiles.

#### **3.2. Enemy Planes**
- **Description**: Enemy planes that engage the player with horizontal movement and randomized projectile attacks.
- **Details**: Implemented in the `EnemyPlane` class. Movement is managed by `updatePosition()`, and `fireProjectile()` adds dynamic shooting mechanics.

#### **3.3. Boss Plane**
- **Description**: A formidable enemy with high health, a shield mechanic, and randomized movements.
- **Details**: The `Boss` class manages complex behavior through `updateActor()`, including shield activation (`isShielded()`) and projectile attacks. Damage is applied only when the shield is inactive, controlled by `takeDamage()`.

### **4. Power-Ups**

#### **4.1. Shield Power-Up**
- **Description**: Grants the player temporary invincibility by activating a protective shield around their plane.
- **Key Feature**: The `showShield()` method in the `ShieldImage` class displays a visible shield effect, while `hideShield()` removes it when the effect ends.

#### **4.2. Spreadshot Power-Up**
- **Description**: Enhances the player’s attack by allowing them to fire multiple projectiles simultaneously.
- **Key Feature**: The `activate()` method in the `SpreadshotPowerUp` class triggers the `activateOneTimeSpreadshot()` function in the `UserPlane`, enabling a spread pattern for the next attack.

### **5. Game Mechanics**

#### **5.1. Collision Management**
- **Description**: Handles interactions between objects, such as projectiles hitting enemies or the player colliding with power-ups.
- **Key Feature**: The `Projectile.takeDamage()` method in the `Projectile` class ensures immediate destruction of projectiles upon collision, maintaining game balance and realism.

#### **5.2. Health Bar**
- **Description**: Displays the current health of a game actor with a graphical bar and numeric representation.
- **Key Feature**: The `updateHealth()` method in the `HealthBar` class dynamically adjusts the width of the health bar and updates the numeric label to reflect the current and maximum health.

#### **5.3. Projectile Mechanics**
- **Description**: Manages the behavior of all projectiles, including movement, firing rates, and destruction.
- **Key Feature**: The `updatePosition()` method in the `UserProjectile`, `EnemyProjectile`, and `BossProjectile` classes ensures smooth, directional movement of projectiles during gameplay.

#### **5.4. Event Handling**
- **Description**: Manages win and loss conditions, transitioning the game state appropriately.
- **Key Feature**: The `handleWin()` and `handleLose()` methods in the `EventHandler` class transition to the appropriate screens (win or game over) while managing music and pausing gameplay.

### **6. Sound System**

#### **6.1. Background Music**
- **Description**: Provides immersive audio through looping background tracks tailored to each game state or level.
- **Key Feature**: The `playBackgroundMusic(String audioFilePath)` method in the `SoundManager` class allows dynamic playback of background music. It ensures smooth transitions and infinite looping for continuous immersion.

#### **6.2. Sound Effects**
- **Description**: Enhances gameplay feedback with sound effects for actions like shooting and collisions.
- **Key Feature**: The `playShootSound()` and `playCrashSound()` methods in the `SoundManager` class handle quick playback of audio clips, offering responsive and engaging auditory cues during gameplay.

### **7. Utility Features**

#### **7.1. Health Bar Utility**
- **Description**: Displays a dynamic visual representation of the player's or boss's health during gameplay.
- **Key Feature**: The `updateHealth(double healthPercentage, int currentHealth, int maxHealth)` method in the `HealthBar` class adjusts the bar width and updates the health text to reflect the current health state in real time.

#### **7.2. Countdown Overlay**
- **Description**: Provides a visual countdown at the start of levels to prepare players for action.
- **Key Feature**: The `startCountdown(int seconds)` method initializes the countdown timer, dynamically rendering numbers on the screen to create anticipation for gameplay.

#### **7.3. Shake Screen**
- **Description**: Adds dramatic effect by shaking the screen during critical events such as explosions or collisions.
- **Key Feature**: The `applyShakeEffect()` method adjusts the camera or viewport position temporarily to simulate a screen shake effect when userplane collides with enemy projectile or enemy plane, enhancing gameplay intensity.

### **8. Collision and Projectile Management**

#### **8.1. Remove Bullet When They Collide**
- **Description**: Ensures that bullets are removed from the game when they collide with targets, reducing unnecessary resource usage.
- **Key Feature**: The `Projectile.takeDamage()` method in the `Projectile` class triggers immediate destruction of the projectile upon collision, maintaining efficient memory usage and gameplay logic.

#### **8.2. Remove Projectiles When Out of Screen**
- **Description**: Automatically removes projectiles that move beyond the visible game area to optimize performance.
- **Key Feature**: The `isOutOfBounds()` method in the `Projectile` class detects when a projectile leaves the screen and invokes `destroy()` to remove it from the game loop.

#### **8.3. Bullet Pass Through the Boss Plane When Shielded**
- **Description**: Allows bullets to bypass the boss when its shield is active, maintaining the shield's integrity.
- **Key Feature**: The `Boss.isShielded()` method in the `Boss` class prevents bullets from applying damage when the boss's shield is activated, enforcing the shield mechanic.

---------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------

## Implemented but Not Working Properly

### **1. User Movement and Projectile Responsiveness Issue**

**Problem Description:**
- **Issue**: Player movement becomes laggy, especially in Level Three, and holding the spacebar for extended periods sometimes causes inconsistent projectile behavior (e.g., spamming or unresponsiveness).

**Possible Causes:**
1. **Input Handling Timing Issues**:
   - The `InputManager` relies on a `HashSet` for key state tracking, which may not handle rapid key presses effectively.
2. **Event Handling Conflicts**:
   - The `fireProjectile()` method tied to spacebar input might not properly regulate firing rate during extended key presses.
3. **Game Loop Processing Delays**:
   - High processing demands in Level Three may occasionally impact the responsiveness of input handling.

**Attempted Solutions:**
1. **Continuous Input Processing**:
   - Implemented the `processInput()` method in the game loop to dynamically handle key states and ensure smoother user movement.
2. **HashSet for Key Tracking**:
   - Utilized a `HashSet<KeyCode>` to track active key presses and prevent duplicate event handling.
3. **Input Timing Adjustment**:
   - Introduced a cooldown mechanism to regulate projectile firing rate by limiting how often `fireProjectile()` can trigger.

**Current Status:**
- **Outcome**: Movement issues have been resolved, but the projectile behavior remains inconsistent. Spacebar spamming sometimes results in rapid firing, while at other times, the input is not registered. Further debugging is required to fully stabilize projectile input handling.

### **2. Kill Count Logic Issue**

**Problem Description:**
- **Issue**: The kill count increases whenever an `EnemyPlane` is destroyed, regardless of the cause. This includes scenarios where the enemy exits the screen or collides with obstacles, even when the player did not directly shoot it.

**Possible Causes:**
1. **Generic Kill Event Handling**:
   - The kill count logic is tied to the destruction of `EnemyPlane` objects (`takeDamage()` or similar methods), rather than being specifically triggered by player actions.
2. **Screen Boundary Collision**:
   - Enemies exiting the screen are treated as destroyed, inadvertently triggering the kill count increment.
3. **Lack of Source Verification**:
   - The system does not verify the source of the damage or destruction event (e.g., projectile, collision, or screen boundary exit), leading to incorrect kill count increments.

**Attempted Solutions:**
1. **Event Source Validation**:
   - Added checks to determine if the enemy was destroyed by a `UserProjectile` before incrementing the kill count.
2. **Screen Exit Handling**:
   - Updated the logic for screen boundary checks to differentiate between destruction events caused by gameplay actions and natural exits.
3. **Debugging Enemy Logic**:
   - Conducted tests to ensure only valid enemy kills caused by the player contribute to the kill count.

**Current Status:**
- **Outcome**: The issue persists; enemies passing through the screen boundary still incorrectly increment the kill count. Further debugging and refinement of the screen boundary handling logic are required to resolve this behavior.

### **3. Screen Scaling and Alignment Issue**

**Problem Description:**
- **Issue**: When running the game on a laptop, the scaling of certain screens (e.g., pause screen and main menu) becomes inconsistent. The screen appears shifted to the bottom-right and does not fit properly. However, on a large monitor or with adjusted display settings, the screens align correctly.

**Possible Causes:**
1. **Scale Resolution Discrepancies**:
   - The game UI does not dynamically adapt to varying screen resolutions or DPI settings.
2. **JavaFX Scaling Limitations**:
   - JavaFX may not automatically scale UI components to fit screens with differing resolutions or scaling factors (e.g., 125%, 150% display settings).
3. **Fixed Layout Dimensions**:
   - Hardcoded dimensions for certain screens may prevent proper scaling on devices with smaller or non-standard resolutions.

**Attempted Solutions:**
1. **Dynamic Scene Sizing**:
   - Modified the scene initialization to bind the game layout dimensions to the window size dynamically using `Stage` and `Scene` resizing properties.
2. **Resolution Testing**:
   - Tested the application on multiple resolutions and DPI settings. The issue persists on smaller displays but resolves on large monitors with standard scaling (100%).
3. **Monitor Dependency**:
   - Used a large monitor with its own desktop configuration, ensuring the game scales and fits properly for all scenes.

**Current Status:**
- **Outcome**: The issue remains unresolved on laptops with smaller displays or non-standard scaling settings. To ensure proper screen alignment, the game must be run on a large monitor or the system display scaling must be reduced to 100%. Further adjustments to dynamic scaling logic are required for a permanent fix.

---------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------

## Features Not Implemented

### **1. Multiplayer Mode**

**Feature Description:**
- **Objective:**  
  Introduce a multiplayer mode to allow players to compete or cooperate in real-time. This feature would enable peer-to-peer gameplay, supporting modes such as cooperative survival or competitive high-score challenges.

**Challenges Encountered:**
1. **Networking Complexity:**
   - **Issue:** Developing a reliable and low-latency networking system for real-time multiplayer interaction proved challenging.
   - **Impact:** Synchronizing game states across players in different locations required robust server logic and extensive testing.
2. **User Interface Adjustments:**
   - **Issue:** Adding multiplayer required significant changes to the user interface, including a lobby system, player indicators, and chat support.
   - **Impact:** These adjustments increased the development workload, potentially impacting the overall polish of the single-player experience.

**Reasons for Leaving Out:**
- **Time Constraints:** Developing a functional and polished multiplayer mode required substantial time, which was redirected to enhancing single-player features.
- **Resource Limitations:** Multiplayer implementation required additional server infrastructure, increasing both development and maintenance costs.
- **Focus on Core Gameplay:** The initial release prioritized a rich single-player experience, with multiplayer planned for future iterations or expansions.

---------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------

## New Java Classes
- Enumerate any new Java classes that I introduced for this game coursework.
- Include a brief description of each class's purpose and its location in the code.

| Class Name                 | Description                                                                                                 | Package                                                                         |
|----------------------------|-------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|
| `BasicFighterPlaneFactory` | Factory for creating `BasicFighterPlane` instances with randomized vertical positioning.                    | `com.example.demo.actors.factory`                                               |
| `FighterPlaneFactory`      | Interface for creating instances of `FighterPlane` with specified constraints.                              | `com.example.demo.actors.factory`                                               |
| `ProjectileFactory`        | Factory for creating projectiles (`Boss`, `Enemy`, `User`) based on their type and position.                | `com.example.demo.actors.factory`                                               |
| `BasicFighterPlane`        | Represents a basic enemy fighter plane with predefined movement and firing behaviors.                       | `com.example.demo.actors.plane`                                                 |
| `LevelThree`               | Represents the third level of the game, featuring enemy waves, power-ups, and a final boss.                 | `com.example.demo.levels`        |
| `ActorManager`             | Manages the lifecycle of all game actors, including spawning, updating, and cleaning up.                    | `com.example.demo.managers`                                                     |
| `CollisionManager`         | Handles collision detection and resolution between game entities, such as planes and projectiles.           | `com.example.demo.managers`                                                     |
| `EnemyManager`             | Manages enemy spawning, projectile firing, and overall enemy-related behaviors.                             | `com.example.demo.managers`                                                     |
| `EventHandler`             | Processes game-winning and game-over events, including music and UI transitions.                            | `com.example.demo.managers`                                                     |
| `GameLoopManager`          | Implements the game loop using a `Timeline` for continuous updates and controls pause/resume functionality. | `com.example.demo.managers`                                                     |
| `GameStateManager`         | Tracks and manages the current state of the game (e.g., playing, paused, game over, win).                   | `com.example.demo.managers`                                                     |
| `InputManager`             | Processes player input for movement, actions, and pausing the game.                                         | `com.example.demo.managers`                                                     |
| `PauseManager`             | Handles pausing, resuming, and restarting the game, as well as returning to the main menu.                  | `com.example.demo.managers`                                                     |
| `SceneManager`             | Configures and manages the main game scene, including the background, controls, and layout.                 | `com.example.demo.managers`                                                     |
| `PowerUp`                  | Represents a collectible item that grants special abilities or effects to the player's plane.               | `com.example.demo.powerups`                                                     |
| `SpreadshotPowerUp`        | A specific type of power-up that enables a spreadshot effect for the user's plane.                          | `com.example.demo.powerups`                                                     |
| `AudioControlPanel`        | Provides buttons to toggle background music and sound effects, integrated into the UI layout.               | `com.example.demo.screens`                                                      |
| `AudioSettingsPage`        | Displays an overlay to adjust audio settings, allowing users to mute/unmute music and effects.              | `com.example.demo.screens`                                                      |
| `CountdownOverlay`         | Shows a countdown (3-2-1-GO) before the game starts, with animations for a polished experience.             | `com.example.demo.screens`                                                      |
| `InstructionsPage`         | Displays the "How to Play" and "Control Keys" pages in an interactive overlay.                              | `com.example.demo.screens`                                                      |
| `MenuView`                 | Manages the main menu, offering options to play, view instructions, access settings, or exit.               | `com.example.demo.screens`                                                      |
| `PauseScreen`              | Handles the pause screen overlay with options to resume, restart, or return to the main menu.               | `com.example.demo.screens`                                                      |
| `SoundManager`             | Manages background music and sound effects, including play, pause, and volume control.                      | `com.example.demo.sounds`                                                       |
| `HealthBar`                | Displays a health bar for game entities, visually representing current and maximum health.                  | `com.example.demo.utils`                                                        |
| `KillCountDisplay`         | Tracks and displays the player's kill count relative to the target kill count for progression.              | `com.example.demo.utils`                                                        |


-------------------------------------------------------------------------------------------------------------------------------------------------

### Detailed in New Java Classes:




-------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------

## Modified Java Classes:
- List the Java classes you modified from the provided code base.
- Describe the changes you made and explain why these modifications were necessary.

| Class Name                | Description                                                                                                                      | Package                              |
|---------------------------|----------------------------------------------------------------------------------------------------------------------------------|--------------------------------------|
| `ActiveActor`             | Enhanced to include image initialization with a fallback to a placeholder image using `initializeImage()` method for robustness. | `com.example.demo.actors`            |
| `UserProjectile`          | Enhanced for user-specific behavior, including rightward movement and unique visual styling.                                     | `com.example.demo.actors.projectile` |
| `Boss`                    | Added shield mechanics with cooldown, a health bar overlay, and randomized movement patterns.                                    | `com.example.demo.actors.plane`      |
| `EnemyPlane`              | Introduced randomized firing offsets and improved projectile spawning logic.                                                     | `com.example.demo.actors.plane`      |
| `FighterPlane`            | Refactored to provide common projectile calculations and damage handling for all planes.                                         | `com.example.demo.actors.plane`      |
| `UserPlane`               | Enhanced with spreadshot capabilities, bounded movement, and velocity-based acceleration.                                        | `com.example.demo.actors.plane`      |
| `Controller`              | Refactored to use reflection for dynamic level transitions, simplifying level management.                                        | `com.example.demo.controller`        |
| `Main`                    | Simplified initialization with clearer separation of concerns for menu setup and application start.                              | `com.example.demo.controller`        |
| `LevelParent`             | Introduced modular game state updates, countdown overlays, and improved enemy management logic.                                  | `com.example.demo.levels`            |
| `LevelOne`                | Enhanced with spawn probabilities and a kill target for progression.                                                             | `com.example.demo.levels`            |
| `LevelTwo`                | Added boss-specific spawning logic with shield and health bar integrations.                                                      | `com.example.demo.levels`            |
| `ShieldImage`             | Added dynamic visibility controls and size configuration for representing temporary invulnerability effects.                     | `com.example.demo.powerups`          |
| `GameOverImage`           | Enhanced with motivational text, fade-in effects, and customizable buttons for restart and exit actions.                         | `com.example.demo.screens`           |
| `LevelViewLevelTwo`       | Introduced the display of shields specific to Level Two, with modular methods for adding images to the scene.                    | `com.example.demo.screens`           |
| `WinImage`                | Added confetti animations, gradient background overlays, and customizable buttons for endgame actions.                           | `com.example.demo.screens`           |
| `HeartDisplay`            | Updated to dynamically add or remove heart icons, visually reflecting the player’s current health.                               | `com.example.demo.utils`             |

-------------------------------------------------------------------------------------------------------------------------------------------------

### Detailed in Modified Java Classes:

#### **1. `Active Actor`**

- **Location:**: `com.example.demo.plane`

**Changes Made**:

1. **Fallback Image Initialization**:
- Implemented a fallback mechanism in the `initializeImage()` method to load a default placeholder image if the specified image resource is unavailable.

**Purpose**:
- To enhance robustness by preventing runtime issues due to missing image resources.

#### **2. `User Projectile`**

**Changes Made**:
1. **Projectile Image and Properties**:
   - Defined the projectile image as `userfire.png` with a predefined height of 125 pixels.
   - Introduced constants (`HORIZONTAL_VELOCITY`) to ensure consistent rightward movement at a fixed speed.

2. **Enhanced Constructor**:
   - Added a constructor that initializes the projectile's position dynamically based on the provided `initialXPos` and `initialYPos`.

**Purpose**:
- To define a specific projectile type for the player's plane with consistent rightward movement.
- To make sure user projectile follow the user plane to shoot correctly.
- To maintain code clarity and flexibility by using constants for projectile properties and movement logic.

#### **3. `Boss`**

- **Location:** `com.example.demo.actors.plane`

**Changes Made**:
1. **Custom Shield Mechanic**:
   - Introduced a shield mechanism using `isShielded`, `activateShield()`, and `deactivateShield()` methods.
   - The shield can be activated based on a random probability (`BOSS_SHIELD_PROBABILITY`) and deactivates after a set duration (`MAX_FRAMES_WITH_SHIELD`).

2. **Health Bar Integration**:
   - Added a `HealthBar` instance to display the boss’s health visually above the entity.
   - The health bar dynamically updates its position and size based on the boss's health percentage.

3. **Custom Hitbox**:
   - Defined a padded bounding box for collision detection using `getCustomHitbox()`.
   - Ensures more accurate hit detection for the boss, considering its visual size.

4. **Visual Effects**:
   - Applied a glowing effect to the boss when its shield is active.
   - Added dynamic updates for the shield’s position and visibility using a `ShieldImage` instance.

**Purpose**:
- To introduce a challenging and dynamic boss entity with unique mechanics such as a temporary shield, randomized movement, and health tracking.
- To improve gameplay depth and visual feedback by integrating a health bar and glowing shield effects.

#### **4. `EnemyPlane`**

- **Location:** `com.example.demo.actors.plane`

**Changes Made**:
1. **Custom Projectile Firing Mechanism**:
   - Implemented a probabilistic firing mechanism using `FIRE_RATE` to determine if a projectile is fired during each update.
   - Added randomized offsets for projectile spawning to make gameplay more dynamic and unpredictable.

2. **Dynamic Projectile Spawning**:
   - Calculated the spawn positions of projectiles using `getProjectileXPosition()` and `getProjectileYPosition()` to ensure correct alignment with the enemy plane's position.

- To introduce dynamic and unpredictable enemy behavior by randomizing projectile firing offsets.
- To simplify the enemy plane's movement and firing logic for better gameplay balance and smoother integration.

#### **5. `FighterPlane`**

- **Location:** `com.example.demo.actors.plane`

**Changes Made**:
1. **Health Management**:
   - Added a `health` property to track the current health of the fighter plane.
   - Introduced a `maxHealth` property to store the initial health as the maximum health.

2. **Health Accessors**:
   - Added `getHealth()` and `getMaxHealth()` methods to allow retrieval of current and maximum health values.

**Purpose**:
- To provide a base class for all fighter planes with consistent health management and projectile firing mechanics.

#### **6. `UserPlane`**

- **Location:** `com.example.demo.actors.plane`

**Changes Made**:
1. **Enhanced Movement Mechanics**:
   - Added `verticalVelocity` and `horizontalVelocity` properties to track movement speed in both directions.
   - Implemented `setVerticalVelocity()` and `setHorizontalVelocity()` methods to adjust velocities based on user input, with acceleration and deceleration.
   - Applied speed limits using `maxSpeed` to ensure balanced movement.

2. **Spreadshot Power-Up**:
   - Introduced `spreadshotCount` to track active spreadshot power-ups.
   - Added `activateOneTimeSpreadshot()` to enable spreadshots, firing multiple projectiles at once.
   - Created `createSpreadshotProjectiles()` to spawn multiple projectiles in a spread pattern.

3. **Projectile Management**:
   - Overrode `fireProjectile()` to handle both single projectiles and spreadshots.
   - Integrated `LevelParent` to manage projectile instances dynamically.

4. **Improved Boundary Handling**:
   - Ensured the plane remains within the game’s bounds using `X_LEFT_BOUND`, `X_RIGHT_BOUND`, `Y_UPPER_BOUND`, and `Y_LOWER_BOUND`.
   - Reverted position changes when the plane moved out of bounds.
   
5. **Deceleration Logic**:
   - Implemented `applyDeceleration()` to reduce velocities gradually when no input is detected.
   - Ensures smoother and more realistic movement mechanics.

6. **Level Integration**:
   - Added a reference to `LevelParent` for managing projectiles.
   - Included `setLevelParent()` to initialize the level reference dynamically.

**Purpose**:
- To provide a dynamic and engaging user-controlled plane with realistic movement and power-ups.
- To enhance gameplay mechanics with spreadshot functionality and responsive velocity adjustments.
- To ensure consistent integration with the game’s level and projectile management systems.

#### **7. `Controller`**

- **Location:** `com.example.demo.controller`

**Changes Made**:
1. **Dynamic Level Transition Using Reflection**:
   - Introduced the `goToLevel(String className)` method to dynamically load levels by their fully qualified class names using reflection.
   - Ensured that levels are instantiated using their constructors, with height and width passed dynamically from the `Stage`.

2. **Property Change Listener Integration**:
   - Implemented `PropertyChangeListener` to monitor level transitions by listening for `PropertyChangeEvent`.
   - Triggered the next level transition when an event is received, using the `goToLevel()` method.

4. **Level Initialization**:
   - Used `LevelParent.initializeScene()` to set up the scene for the current level.
   - Passed the `Stage` object dynamically to `LevelParent.startGame()` to ensure proper stage management for each level.

**Purpose**:
- To enable flexible and dynamic level transitions without hardcoding specific levels, ensuring scalability.

#### **8. `Main`**

- **Location:** `com.example.demo.controller`

**Changes Made**:
1. **Menu and Controller Initialization**:
   - Created a `Controller` instance to manage the game flow and passed it to the `MenuView` for seamless integration.

#### **9. `LevelOne`**

- **Location:** `com.example.demo.levels`

**Changes Made**:
1. **Level-Specific Initialization**:
   - Set up a unique background image (`BACKGROUND_IMAGE_NAME`) and initial player health (`PLAYER_INITIAL_HEALTH`).

2. **Kill-Based Advancement**:
   - Introduced a kill target (`KILLS_TO_ADVANCE`) that the player must achieve to progress to the next level.
   - Added `isKillTargetReached()` and `handleLevelAdvance()` methods to manage level transitions.
   
3. **Enemy Spawning Logic**:
   - Added enemy spawn control based on `ENEMY_SPAWN_PROBABILITY` and the current number of enemies.
   - Implemented methods like `shouldSpawnEnemy()` and `spawnEnemy()` to handle spawning dynamically.

4. **Game Over Management**:
   - Enhanced `checkIfGameOver()` to handle both game loss and advancement scenarios.
   - Stopped background music and transitioned to the appropriate state (loss screen or next level).
   
5. **Level-Specific Music**:
   - Played level-specific background music using `SoundManager.LEVEL_ONE_MUSIC`.
   
6. **Dynamic UI Integration**:
   - Instantiated a `LevelView` to manage health display and other UI elements specific to this level.
  
**Purpose**:
- To establish the foundational gameplay elements for the first level, including a kill-based progression system and dynamic enemy spawning.
- To introduce level-specific visuals and audio for an immersive experience.
- To manage the player’s health and transition states (win or loss) effectively.

#### **10. `LevelTwo`**

- **Location:** `com.example.demo.levels`

**Changes Made**:
1. **Boss-Focused Gameplay**:
   - Added a `Boss` instance as the primary enemy for this level.

2. **Level-Specific Music**:
   - Introduced background music tailored for Level Two using `SoundManager.LEVEL_TWO_MUSIC`.

3. **Boss Health Bar and Shield Integration**:
   - Added methods to display the boss's health bar and shield image on the scene.

4. **Game Over Management**:
   - Implemented `checkIfGameOver()` to handle win (boss defeat) and lose (user destroyed) scenarios.

5. **Level-Specific UI**:
   - Integrated `LevelViewLevelTwo` to manage the UI elements for this level.

6. **Boss Spawning Logic**:
   - Ensured that the boss spawns once and remains until defeated.
   - Added `shouldSpawnBoss()` to control the boss spawning logic.

**Purpose**:
- To provide a focused and intense boss battle as the core challenge of Level Two.
- To integrate the boss’s unique mechanics (health bar, shield) into the gameplay.
- To enhance immersion with level-specific music and UI elements tailored for a boss encounter.

#### **11. `LevelParent`**

- **Location:** `com.example.demo.levels`

**Changes Made**:
1. **Integration of Managers**:
    - Centralized management and integration of various game components through the following managers:
        - `ActorManager`: Handles the addition, removal, and updates of actors such as enemies, projectiles, and power-ups.
        - `GameLoopManager`: Manages the game loop, ensuring the game state updates at fixed intervals.
        - `GameStateManager`: Manages transitions between different game states, such as playing, paused, and game over.
        - `CollisionManager`: Detects and handles collisions between game entities like projectiles and planes.
        - `InputManager`: Processes player inputs, including movement, firing, and pausing.
        - `PauseManager`: Controls the game's pause functionality and displays the pause menu.
        - `SceneManager`: Manages the scene graph, including UI elements, background, and transitions between levels.
        - `EnemyManager`: Spawns enemies dynamically and controls their behaviors such as movement and firing.
        - `EventHandler`: Handles game events, including wins, losses, and other state transitions.

**Purpose**:
- To streamline level management by integrating modular managers, adhering to the Single Responsibility Principle (SRP), and improving code organization and reusability.

2. **Enhanced Game Loop Management**:
   - Integrated `GameLoopManager` for a centralized control of the game loop, ensuring better modularity and ease of updates.
   - `updateScene()` handles all game state updates, including input processing, actor updates, and collision handling.

3. **Dynamic Actor Management**:
   - Introduced `ActorManager` to manage friendly units, enemies, and projectiles efficiently.
   - Added methods like `addEnemyUnit()`, `addPowerUp()`, and `fireProjectile()` for streamlined actor management.

4. **Improved UI Integration**:
   - Incorporated `LevelView` and `KillCountDisplay` for level-specific UI elements, including health display and kill tracking.
   - Added `CountdownOverlay` for a countdown before the game loop starts.

5. **Enhanced State and Event Handling**:
   - Integrated `GameStateManager` for tracking game states (e.g., Playing, Paused).
   - Added `PauseManager` to handle pausing functionality with a user-friendly pause menu.
   - Used `PropertyChangeSupport` for observing and responding to state changes dynamically.

6. **Simplified Enemy and Collision Management**:
   - Leveraged `EnemyManager` to handle enemy spawning and firing logic.
   - Centralized collision handling in `CollisionManager` for improved modularity and ease of debugging.

7. **Dynamic Scene and Sound Management**:
   - Used `SceneManager` for dynamic background setup, scene transitions, and managing UI overlays.
   - Integrated `SoundManager` to control background music and sound effects efficiently.

**Purpose**:
- To provide a robust and reusable foundation for all levels in the game.
- To centralize and modularize key functionalities like actor management, game state updates, and UI integration.
- To improve scalability and ease of adding new levels by abstracting level-specific behaviors into child classes.

#### **12. `ShieldImage`**

- **Location:** `com.example.demo.powerups`

**Changes Made**:
1. **Safe Image Loading**:
   - Implemented a resource check for the shield image to prevent crashes if the resource is unavailable.
   - Prints a warning message to the console if the image resource cannot be located.

2. **Initial Position Setting**:
   - Shield image is positioned based on initial `xPosition` and `yPosition` passed during object construction.

**Purpose**:
- To provide a reusable and dynamic visual effect representing a temporary invulnerability state.
- To enhance visual feedback for players when a shield power-up is activated or deactivated.
- To ensure robust image loading and error handling for smoother gameplay experience.

#### **13. `GameOverImage`**

- **Location**: `com.example.demo.screens`

**Changes Made**:
1. **Dynamic Background Overlay**:
   - Added a semi-transparent black rectangle using the `createBackgroundOverlay` method to focus attention on the game-over elements.

2. **Motivational Text and Fade-in Effects**:
   - Introduced motivational text ("Don’t give up! Try again and show them who's the boss!") below the "Game Over" header.
   - Applied a fade-in animation to the "Game Over" text using the `createGameOverText` method for enhanced visual appeal.

3. **Restart and Exit Buttons with Images**:
   - Designed buttons using the `createImageButton` method with text and images (`RESTART_IMAGE_NAME` and `EXIT_IMAGE_NAME`) to allow the user to restart the game or exit to the main menu.

4. **Game Over Animation Image**:
   - Added an animated background or overlay image that transitions smoothly to enhance the dramatic effect when the player loses.
   - Integrated this animation with the `createBackgroundOverlay` and button actions.

5. **Error Handling for Missing Images**:
   - Implemented error handling in the `loadImageView` method to avoid runtime errors and log missing images gracefully.

6. **Flexible Layout and Text Alignment**:
   - Used a vertical box (`VBox`) and dynamic text alignment (`getTextWidth`) to ensure UI elements are properly centered and responsive.

**Purpose**:
- The `GameOverImage` class enhances user experience by providing a visually engaging "Game Over" screen with interactive options, motivational encouragement, and a dramatic animated background image, making the game more immersive and polished.

#### **14. `WinImage`**

- **Location**: `com.example.demo.screens`

**Changes Made**:
1. **Gradient Background Overlay**:
   - Added a gradient overlay using `LinearGradient` with colors transitioning from light blue to light goldenrod yellow. This creates a celebratory visual effect.
   - Implemented in the `createBackgroundOverlay` method.

2. **"You Win" Image**:
   - Introduced a "You Win" image placed at the top third of the screen.
   - The image dimensions are adjusted dynamically, and horizontal centering ensures proper alignment.
   - Code: `createWinImage`.

3. **Restart and Exit Buttons**:
   - Designed restart and exit buttons using images and text.
   - Buttons are placed below the "You Win" image with consistent styling and spacing.
   - Code: `createButtonContainer` and `createImageButton`.

4. **Confetti Animation**:
   - Implemented a confetti effect with an image falling from the top of the screen.
   - The animation uses `TranslateTransition` to create a smooth falling motion.
   - Code: `createAndAnimateConfetti` and `startConfettiAnimation`.

5. **Error Handling for Missing Resources**:
   - Enhanced error handling to log missing images gracefully.
   - Code: `loadImage`.

**Purpose**:
- To provides a visually engaging "You Win" screen with celebratory animations, gradient backgrounds, and clear options for restarting or exiting to the main menu. 
- These changes enhance the overall user experience and satisfaction upon game completion.

#### **15. `LevelViewLevelTwo`**

- **Location**: `com.example.demo.screens`

**Changes Made**:
1. **Shield Image Integration**:
    - Added a shield image specific to Level Two using the `ShieldImage` class.
    - Positioned the shield image at a fixed location using constants `SHIELD_X_POSITION` and `SHIELD_Y_POSITION`.

**Purpose**:
- Enhances the Level Two experience by adding level-specific visuals such as a shield image, making the gameplay more immersive and visually distinct. 
- This class effectively separates visual concerns for Level Two, supporting modularity and clean code design.

#### **16. `HeartDisplay`**

- **Location**: `com.example.demo.utils`

**Changes Made**:

1. **Dynamic Heart Container Creation**:
    - Added a method to initialize a `HBox` for heart icons with specific x and y coordinates.
    - Ensures that the health display is positioned correctly in the game scene.

2. **Heart Icon Management**:
    - Dynamically populates the container with heart icons based on the player's total health.
    - Uses a method to generate an `ImageView` for each heart, ensuring consistent sizing and aspect ratio.

3. **Health Update Logic**:
    - Introduced a method to visually remove a heart from the container when the player takes damage.
    - Ensures that the display accurately reflects the player’s current health in real-time.

4. **Error Handling for Missing Resources**:
    - Added error handling to log missing image resources and gracefully skip the heart addition.

5. **Reusable Heart Display**:
    - Encapsulated the logic for heart display in a `HBox`, making it modular and easy to integrate across different levels.

**Purpose**:
- Provides a dynamic and visually appealing representation of the player's health. 
- It ensures the health display is modular, responsive, and accurate, contributing to the game's user interface with clear feedback on the player’s status.






