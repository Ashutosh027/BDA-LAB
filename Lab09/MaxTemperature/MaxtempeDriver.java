import org.apache.hadoop.io.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxTemperatureDriver 
{
           
            public static void main (String[] args) throws Exception
            {
                        if (args.length != 2)
                        {
                               System.err.println("Please Enter the input and output parameters");
                               System.exit(-1);
                        }
                       
		      // Configuration conf = new Configuration();
                      // Job job = Job.getInstance(conf, "temperature program");
		      Job job = new Job(); // Deprecated
                      job.setJarByClass(MaxTemperatureDriver.class);
                      job.setJobName("Max temperature");
                       
                      FileInputFormat.addInputPath(job,new Path(args[0]));
                      FileOutputFormat.setOutputPath(job,new Path (args[1]));
                       
                      job.setMapperClass(MaxTemperatureMapper.class);
                      job.setReducerClass(MaxTemperatureReducer.class);
                       
                      job.setOutputKeyClass(Text.class);
                      job.setOutputValueClass(IntWritable.class);
                       
                      System.exit(job.waitForCompletion(true)?0:1);                                             
            }
}
