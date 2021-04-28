package com.elow.codechallenge;

import com.elow.codechallenge.input.Reader;

public class App {

    public static void main(String[] args) {
        Reader reader = new Reader();
        EventProcessor processor = new EventProcessor();

        reader.read(args[0]).forEach(currencyConversionRate -> processor.process(currencyConversionRate));
    }

}
