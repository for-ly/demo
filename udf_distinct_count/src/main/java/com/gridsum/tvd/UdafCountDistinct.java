package com.gridsum.tvd;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.io.IntWritable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by T440P on 2017/7/25.
 */
public class UdafCountDistinct extends AbstractGenericUDAFResolver {

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] info) throws SemanticException {
        return new UdafCountDistinctEvaluator();
    }

    public static class UdafCountDistinctEvaluator extends GenericUDAFEvaluator {
        public static final int initFactor = 2000000000;
        private IntWritable result = new IntWritable(0);
        private PrimitiveObjectInspector inputOI;
        StandardListObjectInspector listOI;
        StructField listField;
        Object[] partialResult;
        StructObjectInspector structOI;
        ListObjectInspector listFieldOI;

        public static class PartialResultCountDistinct implements AggregationBuffer {
            public List<IntWritable> integerList = new ArrayList<>();
        }

        @Override
        public ObjectInspector init(Mode mode, ObjectInspector[] patameters) throws HiveException {
            super.init(mode, patameters);

            listOI = ObjectInspectorFactory.getStandardListObjectInspector(
                    PrimitiveObjectInspectorFactory.writableIntObjectInspector);

            if (mode == Mode.PARTIAL1 || mode == Mode.COMPLETE) {
                inputOI = (PrimitiveObjectInspector) patameters[0];
            } else if (mode == Mode.PARTIAL2) {
                inputOI = (PrimitiveObjectInspector) patameters[0];
                structOI = (StructObjectInspector) patameters[0];
                listField = structOI.getStructFieldRef("list");
                listFieldOI = (ListObjectInspector) listField.getFieldObjectInspector();
            } else {
                structOI = (StructObjectInspector) patameters[0];
                listField = structOI.getStructFieldRef("list");
                listFieldOI = (ListObjectInspector) listField.getFieldObjectInspector();
            }

            if (mode == Mode.PARTIAL1 || mode == Mode.PARTIAL2) {
                ArrayList<ObjectInspector> foi = new ArrayList<ObjectInspector>();
                foi.add(listOI);
                ArrayList<String> fname = new ArrayList<String>();
                fname.add("list");
                partialResult = new Object[1];
                partialResult[0] = new ArrayList<IntWritable>();
                return ObjectInspectorFactory.getStandardStructObjectInspector(fname, foi);
            } else {
                return PrimitiveObjectInspectorFactory.writableIntObjectInspector;
            }
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            AggregationBuffer aggregationBuffer = new PartialResultCountDistinct();
            reset(aggregationBuffer);
            return aggregationBuffer;
        }

        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            PartialResultCountDistinct partialResultCountDistinct = (PartialResultCountDistinct) agg;
            partialResultCountDistinct.integerList.clear();
        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] patameters) throws HiveException {
            assert (patameters.length == 1);
            PartialResultCountDistinct partialResultCountDistinct = (PartialResultCountDistinct) agg;
            if (patameters[0] != null) {
                //int index = PrimitiveObjectInspectorUtils.getInt(patameters[0], inputOI);
                partialResultCountDistinct.integerList.add((IntWritable) patameters[0]);
            }
        }

        @Override
        public Object terminatePartial(AggregationBuffer aggregationBuffer) throws HiveException {
            PartialResultCountDistinct partialResultCountDistinct = (PartialResultCountDistinct) aggregationBuffer;
            partialResult[0] = new ArrayList<>();
            ((ArrayList<IntWritable>) partialResult[0]).addAll(DistinctList(partialResultCountDistinct.integerList));
            return partialResult;
        }

        public List<IntWritable> DistinctList(List<IntWritable> list) {
            List<IntWritable> newList = new ArrayList<>();
            BitSet bitSet = new BitSet(initFactor);
            for (IntWritable integer : list) {
                bitSet.set(integer.get(), true);
            }
            int bitIndex;
            for (int i = 0; i < list.size(); i++) {
                bitIndex = list.get(i).get();
                if (bitSet.get(bitIndex) == true) {
                    newList.add(new IntWritable(bitIndex));
                }
            }
            bitSet.clear();
            return newList;
        }

        @Override
        public void merge(AggregationBuffer aggregationBuffer, Object o) throws HiveException {
            PartialResultCountDistinct partialResultCountDistinct = (PartialResultCountDistinct) aggregationBuffer;
            Object partialObject = structOI.getStructFieldData(o, listField);
            ArrayList<IntWritable> resultList = (ArrayList<IntWritable>) listFieldOI.getList(partialObject);
            partialResultCountDistinct.integerList.addAll(resultList);
        }

        @Override
        public Object terminate(AggregationBuffer aggregationBuffer) throws HiveException {
            PartialResultCountDistinct partialResultCountDistinct = (PartialResultCountDistinct) aggregationBuffer;
            BitSet bitSet = new BitSet(initFactor);
            if (partialResultCountDistinct.integerList == null) {
                return null;
            }
            for (IntWritable integer : partialResultCountDistinct.integerList) {
                bitSet.set(integer.get(), true);
            }
            int count = 0;
            for (int i = 0; i < bitSet.size(); i++) {
                if (bitSet.get(i) == true)
                    count++;
            }
            result.set(count);
            return result;
        }
    }

    public static void main(String[] args) {

    }

}
