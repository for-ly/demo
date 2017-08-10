package com.gridsum.tvd;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.BitSet;

/**
 * Created by T440P on 2017/7/24.
 */
public class DistinctCount extends UDF {
    public Integer evaluate(Integer[] integers) {
        BitSet bs = new BitSet(integers.length);
        bs.clear();
        for (Integer integer : integers) {
            bs.set(integer, true);
        }
        int count = 0;
        for (int i = 0; i < bs.size(); i++) {
            if (bs.get(i) == true)
                count++;
        }
        return count;
    }

    public static void main(String[] args) {
        Integer[] integers = new Integer[100];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = i;
            if (i % 20 == 0)
                integers[i] = i % 20;
        }
        System.out.println(new DistinctCount().evaluate(integers));
    }
}
