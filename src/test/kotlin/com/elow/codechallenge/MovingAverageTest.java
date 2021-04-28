package com.elow.codechallenge;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovingAverageTest {

    @Test
    void add_should_increment_size() {
        // arrange
        MovingAverage ma = new MovingAverage(3);

        // act
        ma.add(4);

        // assert
        assertEquals(1, ma.size());
    }

    @Test
    void add_should_not_increment_size_past_max() {
        // arrange
        MovingAverage ma = new MovingAverage(2);
        ma.add(3);
        ma.add(4);
        ma.add(5);

        // act
        int size = ma.size();

        // assert
        assertEquals(2, size);
    }

    @Test
    void getAverage_should_return_empty_when_not_enough_data() {
        // arrange
        MovingAverage ma = new MovingAverage(2);
        ma.add(3);

        // act
        Optional avg = ma.getAverage();

        // assert
        assertFalse(avg.isPresent());
    }

    @Test
    void getAverage_should_return_average_when_enough_data() {
        // arrange
        MovingAverage ma = new MovingAverage(3);
        ma.add(3);
        ma.add(4);
        ma.add(5);

        // act
        Optional avg = ma.getAverage();

        // assert
        assertEquals(4d, avg.get());
    }

    @Test
    void getAverage_should_return_average_when_too_much_data() {
        // arrange
        MovingAverage ma = new MovingAverage(3);
        ma.add(3);
        ma.add(4);
        ma.add(5);
        ma.add(6);

        // act
        Optional avg = ma.getAverage();

        // assert
        assertEquals(5d, avg.get());
    }


}