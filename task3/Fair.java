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

    public FairRandomResult generateFairNumber(int min, int max)
            throws NoSuchAlgorithmException, InvalidKeyException {

        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        String key = HexFormat.of().formatHex(keyBytes);

        int range = max - min + 1;
        int computerNumber = new SecureRandom().nextInt(range) + min;

        String hmac = calculateHmac(key, Integer.toString(computerNumber));

        return new FairRandomResult(computerNumber, key, hmac);
    }

    private String calculateHmac(String key, String message)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = HexFormat.of().parseHex(key);

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