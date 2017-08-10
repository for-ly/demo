package com.gridsum.tvd;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import java.util.BitSet;
import org.apache.hadoop.io.IntWritable;
/**
 * Created by T440P on 2017/7/24.
 */
@Description(name = "UDAF_Distinct_Count",
        value = "_FUNC_(col) - Example UDAF that distinct count one column data")
public class UDAFDistinctCount extends UDAF{

    public static class UDAFDistinctCountEvaluator implements UDAFEvaluator {
        public static final int initFactor = 1000000000;
        private BitSet bitSet;
        private double mSum;
        private IntWritable result;

        UDAFDistinctCountEvaluator(){
            super();
            init();
        }

        @Override
        public void init() {
            bitSet = new BitSet(initFactor);
            bitSet.clear();
            mSum = 0;
        }

        public boolean iterate(IntWritable integer) {
            if (integer != null) {
                bitSet.set(integer.get(), true);
                mSum++;
            }
            return true;
        }

        public BitSet terminatePartial() {
            return mSum == 0 ? null : bitSet;
        }

        public boolean merge(BitSet bitSet1) {
            if(bitSet1 != null)
                bitSet.or(bitSet1);
            return true;
        }

        public IntWritable terminate(){
            int count = 0;
            for (int i = 0; i < bitSet.size(); i++) {
                if (bitSet.get(i) == true)
                    count++;
            }
            result = new IntWritable(count);
            return result;
        }

    }
}
