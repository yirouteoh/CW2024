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

---------------------------------------------------------------------------------------------------------------------------------------------------------

# Features
## Implemented and Working Properly

### **1. User Interface (UI)**

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
- **Description**: Provides an intuitive main menu interface with options to start the game, access instructions, and modify settings.
- **Details**: The `MenuView` class renders the main menu using the `showMenu()` method, featuring a polished layout created by the `createMenuPanel()` method. Buttons are styled and strategically placed for easy navigation, ensuring an engaging first impression.

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