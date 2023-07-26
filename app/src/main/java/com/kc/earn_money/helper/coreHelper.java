package com.kc.earn_money.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class coreHelper {
    public static String generateTransactionId() {
        List<Character> chars = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            chars.add(c);
        }
        for (char c = '0'; c <= '9'; c++) {
            chars.add(c);
        }

        StringBuilder orderId = new StringBuilder();
        Random random = new Random();
        int length = 7;
        for (int i = 0; i < length; i++) {
            orderId.append(chars.get(random.nextInt(chars.size())));
        }

        return orderId.toString();
    }

}
