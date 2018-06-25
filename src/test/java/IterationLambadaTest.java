import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

/**
 * Created by <a href="https://twitter.com/jaehoox">jaehoo</a> on 19/06/2018
 */
@RunWith(JUnit4.class)
@Slf4j
public class IterationLambadaTest {

    private  double highestScore=0;
    private static List<Result> benchmark= new ArrayList<>();
    private Result res;
    private static List<Student> students= new ArrayList();

    @BeforeClass
    public static void setUpClass() throws Exception {

        Random r = new Random();
        double rangeMin = 1.0;
        double rangeMax = 99.0;

        for(int i =1; i<=900000; i++){
            double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            students.add(new Student(2011,randomValue));
        }
        log.info("number of students:{}",students.size());
    }

    @Before
    public void setUp() throws Exception {

        res = new Result();
        res.setStart(System.currentTimeMillis());

    }


    @After
    public void tearDown() throws Exception {

        res.setStop(System.currentTimeMillis());
        res.setScore(highestScore);
        benchmark.add(res);
    }

    @AfterClass
    public static void close() throws Exception {

        log.info("start\t\t\tstop\t\t\tscore\t\t\t\tdiff\tprocess");
        for(Result r:benchmark){
            log.info("{} ", r);
        }
    }

    @Test
    public void testClassicLoop() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        for(Student s : students){

            if(s.getGradYear() == 2011){
                if (s.getScore()> highestScore ){
                    highestScore = s.getScore();
                }
            }
        }

    }

    @Test
    public void testLambdaLoop() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        highestScore = students.stream().parallel()
                .filter((Student s)-> s.getGradYear() == 2011)
                .map((Student s)->s.getScore())
                .max(Comparator.comparing((Double::valueOf)))
                .get();

    }

    @Test
    public void testLambdaLoopWithNamedFunctions() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        highestScore = students.stream().parallel()
                .filter(this::filterStudent)
                .map(this::getMapper)
                .max(Comparator.comparing((Double::valueOf)))
                .get();
//        highestScore = students.stream().parallel()
//                .max(Comparator.comparing(Student::getScore)).get();
        //.reduce((student, student2) -> {student.getScore()});

//        highestScore = students.stream().parallel()
//                .filter(this::filterStudent)
//                .reduce((student, student2) -> {student.getScore()});

    }

    @Test
    public void testLambdaLoopMaxNamedFunc() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        highestScore = (students.stream().parallel()
                .max(Comparator.comparing(Student::getScore)).get()).getScore();

//        Student maxByScore = students.parallelStream()
//                .max(Comparator.comparing(Student::getScore)).get();
//        //.orElseThrow(NoSuchElementException::new);
    }

    @Test
    public void testLambdaLoopReduceAnonymousFunc() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        Student st = students.stream().parallel()
                .reduce((s,s1)-> {
                    if(s.getScore() > s1.getScore()){
                        return s;
                    }
                    else{
                        return s1;
                    }
                }).get();
        ;

        highestScore = st.getScore();

    }

    @Test
    public void testLambdaLoopReduceNamedFunc() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        Student st = students.stream().parallel()
                .reduce(this::compareScore).get();

        highestScore = st.getScore();

    }

    public Student compareScore(Student s, Student s1){
        if(s.getScore() > s1.getScore()){
            return s;
        }
        else{
            return s1;
        }
    }

    private final boolean filterStudent(Student s){
        return s.getGradYear() == 2011;

    }

    private final Double getMapper(Student s){
        return s.getScore();
    }


    @Data
    class Result{
        private String process;
        private long start, stop;
        private double score;

        @Override
        public String toString() {
            return  start +"\t"
                    + stop +"\t"
                    + score+"\t"
                    + (stop - start)+"\t\t"
                    + process +"\t"
                    ;

        }
    }
}

