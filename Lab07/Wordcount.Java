import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordCount {
    public static void main(String[] arrstring) throws Exception {
        Configuration configuration = new Configuration();
        String[] arrstring2 = new GenericOptionsParser(configuration, arrstring).getRemainingArgs();
        if (arrstring2.length != 2) {
            System.err.println("Usage: WordCount <in> <out>");
            System.exit(2);
        }
        Job job = new Job(configuration, "wordcount");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setCombinerClass(Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath((Job)job, (Path)new Path(arrstring2[0]));
        FileOutputFormat.setOutputPath((Job)job, (Path)new Path(arrstring2[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class Reduce
    extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text text, Iterable<IntWritable> iterable, Reducer.Context context) throws IOException, InterruptedException {
            int n = 0;
            for (IntWritable intWritable : iterable) {
                n += intWritable.get();
            }
            this.result.set(n);
            context.write((Object)text, (Object)this.result);
        }
    }

    public static class Map
    extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(LongWritable longWritable, Text text, Mapper.Context context) throws IOException, InterruptedException {
            StringTokenizer stringTokenizer = new StringTokenizer(text.toString());
            while (stringTokenizer.hasMoreTokens()) {
                this.word.set(stringTokenizer.nextToken());
                context.write((Object)this.word, (Object)one);
            }
        }
    }
}
