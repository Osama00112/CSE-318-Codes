import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        //System.out.println("hello");
        File courseFile = new File("F:\\3-2\\Sessional\\CSE 318\\Local Search\\Toronto\\car-f-92.crs");  // car-f-92, car-s-91, kfu-s-93, tre-s-92, yor-f-83
        File stdFile = new File("F:\\3-2\\Sessional\\CSE 318\\Local Search\\Toronto\\car-f-92.stu");
        BufferedReader crsReader = new BufferedReader(new FileReader(courseFile));
        BufferedReader stdReader = new BufferedReader(new FileReader(stdFile));

        String st;
        int courseCount = 0;
        int studentCount = 0;

        List<Course> courseList = new ArrayList<>();
        while((st = crsReader.readLine()) != null){
            courseCount++;
            String []words = st.split(" ");
            if(words.length >= 2){
                int courseId = Integer.parseInt(words[0]);
                int stdCount = Integer.parseInt(words[1]);
                Course crs = new Course(courseId, stdCount);
                courseList.add(crs);
            }
        }

        List<Student> studentList = new ArrayList<>();
        System.out.println(courseCount);
        Graph graph = new Graph(courseCount);
        graph.courseList = courseList;
        graph.courseList.sort(new CourseIdComparator());

        while((st = stdReader.readLine()) != null){
            studentCount++;
            String []words = st.split(" ");
            Student std = new Student(studentCount);
            for(int i=0; i<words.length; i++){
                int found = Integer.parseInt(words[i]);  // course ID found
                Course correspondingCourse = graph.courseList.get(found-1);
                std.addCourse(correspondingCourse);
            }
            for(Course c: std.courseList){
                for(Course d: std.courseList){
                    if(c.id == d.id)
                        continue;
                    //c.addConflict(d.id);
                    //d.addConflict(c.id);
                    graph.addEdge(c.id - 1, d.id - 1);
                }
            }
            studentList.add(std);
        }

        Solver sv = new Solver(graph.courseList, studentList,1,2);
        sv.solve();
        sv.TotalTimeSlots();
        double avg = sv.calculateAvgPenalty();
        System.out.println("avg " + avg);

        PerturbativeHeuristics ph = new PerturbativeHeuristics(graph.courseList, studentList, 2, avg);
        ph.KempeChain();
        System.out.println("new avg " + ph.newAvg);

    }
}
