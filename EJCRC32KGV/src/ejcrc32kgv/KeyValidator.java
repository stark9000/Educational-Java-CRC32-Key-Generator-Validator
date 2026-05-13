package ejcrc32kgv;

import java.util.Scanner;
import java.util.zip.CRC32;

public class KeyValidator {

    static final String ALPHABET =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    static final int SECRET_XOR = 0x1D8DF;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter key:");

        String key = scanner.nextLine();

        boolean valid = validate(key);

        if (valid) {
            System.out.println("VALID KEY");
        } else {
            System.out.println("INVALID KEY");
        }
    }

    static boolean validate(String key) {

        // Remove dashes
        key = key.replace("-", "");

        // Unscramble
        key = unscramble(key);

        // Split sections
        String randomPart = key.substring(0, 12);
        String crcPart = key.substring(12, 18);

        // Recalculate CRC32
        long crc = calculateCRC32(randomPart);

        // XOR again
        crc = crc ^ SECRET_XOR;

        // Encode expected CRC
        String expected = encodeCRC(crc);

        // Compare
        return expected.equals(crcPart);
    }

    static long calculateCRC32(String text) {

        CRC32 crc = new CRC32();

        crc.update(text.getBytes());

        return crc.getValue();
    }

    static String encodeCRC(long value) {

        char[] out = new char[6];

        for (int i = 0; i < out.length; i++) {

            int index =
                    (int)(value % ALPHABET.length());

            out[i] = ALPHABET.charAt(index);

            value /= ALPHABET.length();
        }

        return new String(out);
    }

    static String unscramble(String text) {

        char[] data = text.toCharArray();

        // Reverse swaps
        swap(data, 7, 15);
        swap(data, 4, 14);
        swap(data, 2, 8);
        swap(data, 1, 5);

        return new String(data);
    }

    static void swap(char[] data, int a, int b) {

        char temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }
}