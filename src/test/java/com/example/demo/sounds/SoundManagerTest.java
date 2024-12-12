package com.example.demo.sounds;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.JavaFXInitializer;

import static org.junit.jupiter.api.Assertions.*;

class SoundManagerTest {

    private SoundManager soundManager;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized once
    }
    @BeforeEach
    void setUp() {
        soundManager = SoundManager.getInstance(); // Ensure Singleton instance
    }

    @Test
    void getInstance() {
        assertNotNull(soundManager, "SoundManager instance should not be null.");
    }

    @Test
    void playBackgroundMusic() {
        assertDoesNotThrow(() -> soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC),
                "Playing background music should not throw an exception.");
    }

    @Test
    void stopBackgroundMusic() {
        soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);
        assertDoesNotThrow(soundManager::stopBackgroundMusic,
                "Stopping background music should not throw an exception.");
    }

    @Test
    void pauseBackgroundMusic() {
        soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);
        assertDoesNotThrow(soundManager::pauseBackgroundMusic,
                "Pausing background music should not throw an exception.");
    }

    @Test
    void resumeBackgroundMusic() {
        soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);
        soundManager.pauseBackgroundMusic();
        assertDoesNotThrow(soundManager::resumeBackgroundMusic,
                "Resuming background music should not throw an exception.");
    }

    @Test
    void playShootSound() {
        assertDoesNotThrow(soundManager::playShootSound,
                "Playing shoot sound should not throw an exception.");
    }

    @Test
    void playCrashSound() {
        assertDoesNotThrow(soundManager::playCrashSound,
                "Playing crash sound should not throw an exception.");
    }

    @Test
    void muteBackgroundMusic() {
        soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);
        soundManager.muteBackgroundMusic();
        assertTrue(soundManager.isBackgroundMusicMuted(),
                "Background music should be muted after calling muteBackgroundMusic.");
    }

    @Test
    void unmuteBackgroundMusic() {
        soundManager.muteBackgroundMusic();
        soundManager.unmuteBackgroundMusic(SoundManager.MENU_MUSIC);
        assertFalse(soundManager.isBackgroundMusicMuted(),
                "Background music should be unmuted after calling unmuteBackgroundMusic.");
    }

    @Test
    void muteSoundEffects() {
        soundManager.muteSoundEffects();
        assertTrue(soundManager.isSoundEffectsMuted(),
                "Sound effects should be muted after calling muteSoundEffects.");
    }

    @Test
    void unmuteSoundEffects() {
        soundManager.muteSoundEffects();
        soundManager.unmuteSoundEffects();
        assertFalse(soundManager.isSoundEffectsMuted(),
                "Sound effects should be unmuted after calling unmuteSoundEffects.");
    }

    @Test
    void isBackgroundMusicMuted() {
        soundManager.muteBackgroundMusic();
        assertTrue(soundManager.isBackgroundMusicMuted(),
                "isBackgroundMusicMuted should return true after muting.");
    }

    @Test
    void isSoundEffectsMuted() {
        soundManager.muteSoundEffects();
        assertTrue(soundManager.isSoundEffectsMuted(),
                "isSoundEffectsMuted should return true after muting.");
    }
}
