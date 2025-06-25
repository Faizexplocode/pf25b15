// Seed.java
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public enum Seed {
    NO_SEED(" ", null), // Sel kosong
    CROSS("X", "/images/cross.jpg"),   // Gambar untuk pemain X
    NOUGHT("O", "/images/not.jpeg");  // Gambar untuk pemain O

    private String displayName;
    private Image img = null;

    private Seed(String name, String imagePath) {
        this.displayName = name;

        if (imagePath != null) {
            try {
                URL imgURL = getClass().getResource(imagePath); // Path dari root classpath
                if (imgURL != null) {
                    this.img = new ImageIcon(imgURL).getImage();
                } else {
                    System.err.println("Image not found: " + imagePath);
                }
            } catch (Exception e) {
                System.err.println("Error loading image " + imagePath + ": " + e.getMessage());
            }
        }
    }

    public String getDisplayName() {
        return displayName;
    }
    public Image getImage() {
        return img;
    }
}