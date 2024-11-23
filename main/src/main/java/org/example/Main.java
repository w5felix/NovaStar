package org.example;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Example transactions
        Transaction t1 = new Transaction("Bitcoin", 0.5, 48000.0, new Date(), "BUY");
        Transaction t2 = new Transaction("Ethereum", 1.0, 3500.0, new Date(), "SELL");

        // Print transactions
        System.out.println(t1);
        System.out.println(t2);
    }
}

