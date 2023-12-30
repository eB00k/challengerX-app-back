package dias.photo_app.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String DIGITS_AND_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returValue = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            returValue.append(DIGITS_AND_ALPHABET.charAt(RANDOM.nextInt(DIGITS_AND_ALPHABET.length())));
        }
        return new String(returValue);
    }
}
