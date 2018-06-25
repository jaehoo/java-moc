import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by <a href="https://twitter.com/jaehoox">jaehoo</a> on 20/06/2018
 */
@RunWith(JUnit4.class)
@Slf4j
public class IterateList {

    private int[] ints ={1,2,3,4,5,6,7,8};

    private List<String> list= new ArrayList<String>(){

        {add("one");}
        {add("two");}
        {add("three");}
        {add("four");}
        {add("five");}
        {add("six");}

    };

    @BeforeClass
    public static void setUp() throws Exception {



    }

    @Test
    public void testEach() throws Exception {
        list.forEach((s) -> log.info(s) );
//        list.forEach(System.out::println);
    }

    @Test
    public void forEach2() throws Exception {
        list.forEach((s) -> log.info(s) );
    }

    @Test
    public void test() throws Exception {

        int m = Arrays.stream(ints).parallel()
                .reduce(Integer.MIN_VALUE, Math::max);
    }

    @Test
    public void name() throws Exception {

        int m = Arrays.stream(ints)
                .reduce(Integer.MIN_VALUE, Math::max);
    }
}
