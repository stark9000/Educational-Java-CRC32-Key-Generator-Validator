package ejcrc32kgv;

import java.util.Random;
import java.util.zip.CRC32;

public class KeyGenerator {

    static final String ALPHABET =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    static final int SECRET_XOR = 0x1D8DF;

    static Random random = new Random();

    public static void main(String[] args) {

        // Step 1: Generate random section
        String randomPart = generateRandomText(12);

        // Step 2: Calculate CRC32
        long crc = calculateCRC32(randomPart);

        // Step 3: XOR mask
        crc = crc ^ SECRET_XOR;

        // Step 4: Encode CRC
        String crcText = encodeCRC(crc);

        // Step 5: Combine
        String fullKey = randomPart + crcText;

        // Step 6: Scramble
        fullKey = scramble(fullKey);

        // Step 7: Format
        fullKey = formatKey(fullKey);

        System.out.println("Generated Key:");
        System.out.println(fullKey);
    }

    static String generateRandomText(int length) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {

            sb.append(
                    ALPHABET.charAt(
                            random.nextInt(ALPHABET.length())
                    )
            );
        }

        return sb.toString();
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

    static String scramble(String text) {

        char[] data = text.toCharArray();

        swap(data, 1, 5);
        swap(data, 2, 8);
        swap(data, 4, 14);
        swap(data, 7, 15);

        return new String(data);
    }

    static void swap(char[] data, int a, int b) {

        char temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }

    static String formatKey(String key) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < key.length(); i++) {

            sb.append(key.charAt(i));

            if ((i + 1) % 4 == 0
                    && i != key.length() - 1) {

                sb.append('-');
            }
        }

        return sb.toString();
    }
}