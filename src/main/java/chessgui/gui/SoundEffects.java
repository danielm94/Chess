package chessgui.gui;

import chessgui.gui.resource_paths.SoundPaths;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Random;


public final class SoundEffects {
    private SoundEffects() {
    }

    private static void playSoundEffect(String filePath) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(ClassLoader.getSystemResource(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playCheckSound() {
        playSoundEffect(SoundPaths.CHECK.getPath());
    }

    public static void playMoveSound() {
        playSoundEffect(SoundPaths.PIECE_MOVE.getPath());
    }

    public static void playInvalidMoveSound() {
        int rand = new Random().nextInt(3);
        switch (rand) {
            case 0:
                playSoundEffect(SoundPaths.INVALID_MOVE1.getPath());
                break;
            case 1:
                playSoundEffect(SoundPaths.INVALID_MOVE2.getPath());
                break;
            default:
                playSoundEffect(SoundPaths.INVALID_MOVE3.getPath());
                break;
        }

    }

    public static void playStalemateSound() {
        playSoundEffect(SoundPaths.STALEMATE.getPath());
    }

    public static void playCheckmateSound() {
        playSoundEffect(SoundPaths.CHECKMATE.getPath());
    }

    public static void playStalemateBy50MoveRuleSound() {
        playSoundEffect(SoundPaths.STALEMATE_50.getPath());
    }

    public static void playEasterEgg() {
        playSoundEffect(SoundPaths.EASTER_EGG.getPath());
    }
}
