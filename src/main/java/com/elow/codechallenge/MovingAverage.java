package com.elow.codechallenge;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 * A moving average of with size
 */
public class MovingAverage {

    // the values in the moving averge
    private final Queue<Double> values = new LinkedList<>();

    // the size of the moving average
    private final int maxSize;

    // the current average
    private double average = 0;

    // the current total of values
    private double total = 0;

    /**
     * Constructor
     * @param size The size of the moving average
     */
    public MovingAverage(int size) {
        this.maxSize = size;
    }

    /**
     * Adds a value to the moving average
     * @param value the value to add to the moving average
     */
    public void add(double value) {
        values.add(value);
        total += value;

        if (values.size() > maxSize) {
            double removedValue = values.remove();
            total -= removedValue;
        }

        average = total / values.size();
    }

    /**
     * returns the number of records in the average
     * @return the number of records in the average
     */
    public int size() {
        return values.size();
    }

    /**
     * Returns the moving average
     * @return the moving average, null if there is not enough data
     */
    public Optional<Double> getAverage() {
        if (values.size() == maxSize) {
            return Optional.of(average);
        } else {
            return Optional.empty();
        }
    }
}
