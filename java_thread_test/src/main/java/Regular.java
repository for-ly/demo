import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by T440P on 2017/6/15.
 */
public class Regular {
    public static void main(String args[]){
        String pattern = "&p=(\\w*)";
        String test = "v=129c50045201e2795f045786bbae6fe1&p=11953978761lxqx5f7&adc=0&ab=0";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(test);
        while (m.find())
        {
            System.out.println(m.group(0));
            System.out.println(m.group(0).split("=")[1]);
        }
    }
}
