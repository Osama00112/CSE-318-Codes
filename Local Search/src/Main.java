import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Dataset NO:");
        int datasetNo = sc.nextInt();
        System.out.println("Enter Constructive heuristic no:");
        int heuristicType = sc.nextInt();
        System.out.println("Enter Penalty type:");
        int penType = sc.nextInt();

        String dataSetName;
        if(datasetNo == 1)
            dataSetName = "car-f-92";
        else if(datasetNo == 2)
            dataSetName = "car-s-91";
        else if(datasetNo == 3)
            dataSetName = "kfu-s-93";
        else if(datasetNo == 4)
            dataSetName = "tre-s-92";
        else
            dataSetName = "yor-f-83";
        File courseFile = new File("F:\\3-2\\Sessional\\CSE 318\\Local Search\\Toronto\\" + dataSetName + ".crs");  // car-f-92, car-s-91, kfu-s-93, tre-s-92, yor-f-83
        File stdFile = new File("F:\\3-2\\Sessional\\CSE 318\\Local Search\\Toronto\\" + dataSetName + ".stu");
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
                    graph.addEdge(c.id - 1, d.id - 1);
                }
            }
            studentList.add(std);
        }


        Solver sv = new Solver(graph.courseList, studentList, heuristicType,penType);
        if(heuristicType == 2)
            sv.solveSat();
        else
            sv.solve();
        sv.TotalTimeSlots();
        double avg = sv.calculateAvgPenalty();
        System.out.println("Average penalty without Perturbative Heuristic: " + avg);

        PerturbativeHeuristics ph = new PerturbativeHeuristics(graph.courseList, studentList, penType, avg);
        ph.KempeChain();
        System.out.println("Average penalty after Kempe Chain: " + ph.newAvg);
        ph.PairSwap();
        System.out.println("Average penalty after Pair Swap: " + ph.newAvg);

    }
}
