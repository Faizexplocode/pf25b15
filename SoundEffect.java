import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * Enum ini mengelola semua efek suara dalam game, agar kode pemutar suara
 * terpisah dari logika permainan.
 */
public enum SoundEffect {
    WELCOME("audio/welcome.wav"),
    CROSS_PLAY("audio/cross.wav"),
    NOUGHT_PLAY("audio/not.wav"),
    DIE("audio/die.wav");

    // Volume level
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW; // default volume

    private Clip clip;

    /** Konstruktor enum untuk setiap efek suara */
    private SoundEffect(String soundFileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            if (url == null) {
                throw new IllegalArgumentException("Sound file not found: " + soundFileName);
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /** Mainkan efek suara dari awal */
    public void play() {
        if (volume != Volume.MUTE && clip != null) {
            if (clip.isRunning()) {
                clip.stop(); // hentikan jika sedang berjalan
            }
            clip.setFramePosition(0); // rewind ke awal
            clip.start();             // mulai mainkan
        }
    }

    /** Hentikan suara jika sedang dimainkan */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    /** Preload semua efek suara */
    public static void initGame() {
        values(); // akan memanggil konstruktor dari semua enum
    }
}
