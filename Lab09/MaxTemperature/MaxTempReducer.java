import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import java.io.IOException;

public class MaxTemperatureReducer extends Reducer <Text, IntWritable, Text, IntWritable>{
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int max_temp = 0;
		int total_temp = 0;
		int count = 0;
		int days = 0;
		for(IntWritable value: values) {
			int temp = value.get();
			if(temp > max_temp) {
				max_temp = temp;
			}
			count+=1;
			if(count == 3) {
				total_temp += max_temp;
				max_temp = 0;
				count = 0;
				days += 1;
			}
			
		}
		context.write(key, new IntWritable(total_temp/days));
	}

}
