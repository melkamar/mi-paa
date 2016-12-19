package com.melkamar.evo;

import java.util.Arrays;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 19.12.2016 11:55.
 */
public class Problem {
    public Item [] items;
    public int maxWeight;
    public int itemsCount;
    public int id;

    public Problem(int id, Item[] items, int maxWeight) {
        this.items = items;
        this.maxWeight = maxWeight;
        this.itemsCount = items.length;
        this.id = id;
    }

    public static class Item{
        public int weight;
        public int value;

        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" +
                    "w=" + weight +
                    ", c=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", items=" + Arrays.toString(items) +
                ", maxWeight=" + maxWeight +
                ", itemsCount=" + itemsCount +
                '}';
    }
}
