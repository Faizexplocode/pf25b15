// Seed.java
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public enum Seed {
    NO_SEED(" ", null), // NO_SEED tanpa gambar
    CROSS("X", "/images/cross.jpg"),   // <-- Tambahkan leading slash '/'
    NOUGHT("O", "/images/not.jpeg");  // <-- Tambahkan leading slash '/'

    // Private variables
    private String displayName;
    private Image img = null;

    // Constructor (must be private)
    private Seed(String name, String imageFilename) {
        this.displayName = name;

        if (imageFilename != null) {
            try {
                // Gunakan getClass().getResource() dengan leading slash untuk kejelasan path dari classpath root
                URL imgURL = getClass().getResource(imageFilename); // <-- Ubah di sini
                ImageIcon icon = null;
                if (imgURL != null) {
                    icon = new ImageIcon(imgURL);
                } else {
                    System.err.println("Couldn't find file " + imageFilename);
                }
                if (icon != null) { // Pastikan icon tidak null sebelum mendapatkan image
                    img = icon.getImage();
                }
            } catch (Exception e) {
                System.err.println("Error loading image " + imageFilename + ": " + e.getMessage());
            }
        }
    }

    // Public getters
    public String getDisplayName() {
        return displayName;
    }
    public Image getImage() {
        return img;
    }
}