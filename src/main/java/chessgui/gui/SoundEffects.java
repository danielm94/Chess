package chessgui.gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Random;


public class SoundEffects {
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
            case 0 -> playSoundEffect(SoundPaths.INVALID_MOVE1.getPath());
            case 1 -> playSoundEffect(SoundPaths.INVALID_MOVE2.getPath());
            case 2 -> playSoundEffect(SoundPaths.INVALID_MOVE3.getPath());
        }
    }

    public static void playStalemateSound() {
        playSoundEffect(SoundPaths.STALEMATE.getPath());
    }

    public static void playCheckmateSound() {
        playSoundEffect(SoundPaths.CHECKMATE.getPath());
    }
}
