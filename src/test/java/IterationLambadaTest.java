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

        highestScore = 0.0;
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
    public void loopClassicForEach() throws Exception {

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
    public void loopStreamAnonymousLambda() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        highestScore = students.stream().parallel()
                .filter((Student s)-> s.getGradYear() == 2011)
                .mapToDouble((s)->s.getScore())
                .max().getAsDouble();

    }

    @Test
    public void loopStreamNamedFunc() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        highestScore = students.stream().parallel()
                .filter(this::filterStudent)
                .mapToDouble(this::getMapper)
                .max().getAsDouble();




//        highestScore = students.stream().parallel()
//                .max(Comparator.comparing(Student::getScore)).get();
        //.reduce((student, student2) -> {student.getScore()});

//        highestScore = students.stream().parallel()
//                .filter(this::filterStudent)
//                .reduce((student, student2) -> {student.getScore()});

    }

    @Test
    public void loopStreamAnonymousLambdaOpt() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        highestScore = students.parallelStream()
                .filter((Student s)-> s.getGradYear() == 2011)
                .max((s1, s2) -> { return s1.getScore().compareTo(s2.getScore());})
                .get().getScore();

    }

    @Test
    public void loopStreamNamedFuncOpt() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        highestScore = (students.parallelStream()
                .filter(this::filterStudent)
                .max(Comparator.comparing(Student::getScore)).get()).getScore();

    }

    @Test
    public void loopReduceAnonymousFunc() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        highestScore = students.stream().parallel()
                .filter((Student s)-> s.getGradYear() == 2011)
                .reduce((s,s1)-> {
                    if(s.getScore() > s1.getScore()){
                        return s;
                    }
                    else{
                        return s1;
                    }
                }).get().getScore();
    }

    @Test
    public void loopReduceNamedFunc() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        highestScore = students.stream().parallel()
                .filter(this::filterStudent)
                .reduce(this::compareScore).get().getScore();

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


    @Test
    public void forEachAnonymousLambda() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        students.parallelStream().forEach(s->{
            if(s.getGradYear() == 2011){
                if(s.getScore() > highestScore){
                    highestScore = s.getScore();
                }
            }
        });


    }

    @Test
    public void forEachNamedFunction() throws Exception {

        res.setProcess(new Object(){}.getClass().getEnclosingMethod().getName());

        students.parallelStream().forEach(this::studentFilter);
    }

    public Student studentFilter(Student s){
        if(s.getGradYear() == 2011){
            if(s.getScore() > highestScore){
                highestScore = s.getScore();
            }
        }
        return s;
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

