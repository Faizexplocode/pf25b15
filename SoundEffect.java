// SoundEffect.java
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public enum SoundEffect {
    // Sesuaikan nama file dan kapitalisasi sesuai yang ada di folder 'audio' Anda
    WELCOME("/audio/welcome.WAV"),
    CROSS_PLAY("/audio/cross.WAV"),
    NOUGHT_PLAY("/audio/not.wav"),
    DIE("/audio/die.wav");

    private Clip clip;

    SoundEffect(String filename) {
        try {
            URL url = getClass().getResource(filename); // Menggunakan getClass().getResource()
            if (url == null) {
                System.err.println("Sound file not found: " + filename);
                throw new RuntimeException("Sound file not found: " + filename); // Melemparkan RuntimeException
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | RuntimeException e) {
            System.err.println("Error loading sound " + filename + ": " + e.getMessage());
            clip = null; // Set clip ke null jika ada error
        }
    }

    public void play() {
        if (clip != null) {
            if (clip.isRunning())
                clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public static void initGame() {
        values(); // Memuat semua klip suara saat game diinisialisasi (memanggil konstruktor setiap enum)
    }
}