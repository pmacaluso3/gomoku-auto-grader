package org.example.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomStringGenerator {
    private final int LOWERCASE_A_CODEPOINT = (int) 'a';
    private final int UPPERCASE_A_CODEPOINT = (int) 'A';
    private final int NUM_LETTERS = 26;
    private final int DEFAULT_LENGTH = 20;
    private final Random random = new Random();
    private final int length;

    public RandomStringGenerator() {
        this.length = DEFAULT_LENGTH;
    }

    public RandomStringGenerator(int length) {
        this.length = length;
    }

    public String generateRandomString() {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than zero.");
        }

        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomOffset = random.nextInt(NUM_LETTERS * 2); // Letters are 52 (26 uppercase + 26 lowercase)
            int randomCodePoint;
            if (randomOffset < NUM_LETTERS) {
                randomCodePoint = UPPERCASE_A_CODEPOINT + randomOffset;
            } else {
                randomCodePoint = LOWERCASE_A_CODEPOINT + randomOffset - NUM_LETTERS;
            }
            char randomChar = (char) randomCodePoint;
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
}
