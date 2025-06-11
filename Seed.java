/**
 * Enum Seed digunakan oleh:
 * 1. Player: menggunakan nilai CROSS atau NOUGHT
 * 2. Cell content: menggunakan nilai CROSS, NOUGHT, atau NO_SEED

 * Idealnya, kita ingin membuat dua enum dengan pewarisan, tetapi ini tidak didukung di Java.
 */
public enum Seed {
    CROSS,
    NOUGHT,
    NO_SEED
}//hu