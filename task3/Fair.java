package task3;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class Fair {
    private static final String HMAC_ALGORITHM = "HmacSHA3-256";

    public static class FairRandomResult {
        public final int computerNumber;
        public final String secretKey;
        public final String hmac;

        public FairRandomResult(int computerNumber, String secretKey, String hmac) {
            this.computerNumber = computerNumber;
            this.secretKey = secretKey;
            this.hmac = hmac;
        }
    }

    public FairRandomResult generateFairNumber(int min, int max) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] secretKeyBytes = generateSecureRandomBytes(32);
        String secretKeyHex = HexFormat.of().formatHex(secretKeyBytes);

        int computerNumber = generateSecureRandomInt(min, max);

        String hmac = calculateHmac(secretKeyHex, Integer.toString(computerNumber));

        return new FairRandomResult(computerNumber, secretKeyHex, hmac);
    }

    private byte[] generateSecureRandomBytes(int size) {
        byte[] bytes = new byte[size];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    private int generateSecureRandomInt(int min, int max) {
        SecureRandom secureRandom = new SecureRandom();
        int range = max - min + 1;

        int bits, val;
        do {
            bits = secureRandom.nextInt();
            val = bits % range;
        } while (val < 0 || val >= range);
        return min + val;
    }

    private String calculateHmac(String secretKeyHex, String message)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = HexFormat.of().parseHex(secretKeyHex);

        Mac hmac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
        hmac.init(keySpec);

        byte[] hmacBytes = hmac.doFinal(message.getBytes());

        return HexFormat.of().formatHex(hmacBytes);
    }

    public static int calculateFinalResult(int computerNumber, int userNumber, int modulus) {
        return (computerNumber + userNumber) % modulus;
    }
}
