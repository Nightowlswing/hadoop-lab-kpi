import java.util.*;
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class ProcessUnits {
    //Mapper class
    public static class E_EMapper extends MapReduceBase implements
            Mapper<LongWritable,/*Input key Type */
                    Text, /*Input value Type*/
                    Text, /*Output key Type*/
                    DoubleWritable> /*Output value Type*/ {
        //Map function
        public void map(LongWritable key, Text value,
                        OutputCollector<
                                Text, DoubleWritable>
                                output,
                        Reporter reporter) throws IOException {
            String line = value.toString();
            StringTokenizer s = new StringTokenizer(line, " ");
            String year = s.nextToken();
            double sum = 0;
            int i = 0;
            while (s.hasMoreTokens()) {
                i++;
                sum += Double.parseDouble(s.nextToken());
            }
            output.collect(new Text(year), new DoubleWritable(sum));
        }
    }

    //Reducer class
    public static class E_EReduce extends MapReduceBase implements
            Reducer<Text, DoubleWritable, Text, DoubleWritable> {
        //Reduce function
        public void reduce(Text key, Iterator<
                DoubleWritable>
                values,
                           OutputCollector<Text, DoubleWritable>
                                   output, Reporter reporter)
                throws IOException {
            double sum = 0;

            int i = 0;
            while (values.hasNext()) {

                double value = values.next().get();
                i++;
                sum += value;
            }
            double avg = sum / i;
            output.collect(key, new DoubleWritable(avg));
        }
    }


    //Main function
    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf(ProcessUnits.class);
        conf.setJobName("avg_eletricityunits");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(DoubleWritable.class);
        conf.setMapperClass(E_EMapper.class);
        conf.setCombinerClass(E_EReduce.class);
        conf.setReducerClass(E_EReduce.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        JobClient.runJob(conf);
    }
}