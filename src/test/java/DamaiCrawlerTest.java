import com.alibaba.fastjson.JSON;
import com.flyme.model.Program;
import com.flyme.util.DamaiCrawler;
import org.junit.Test;

/**
 * @author zzzz76
 */
public class DamaiCrawlerTest {

    private DamaiCrawler damaiCrawler = new DamaiCrawler();

    @Test
    public void testCrawlProgram() {
        Program program = new Program();
        String errStatus = damaiCrawler.synCrawl("642874905410", program);
        if (errStatus == null) {
            System.out.println(JSON.toJSONString(program));
        } else {
            System.out.println(errStatus);
        }
    }
}
