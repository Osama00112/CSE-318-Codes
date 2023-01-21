import java.util.ArrayList;
import java.util.List;

public class Graph {
    public int courseCount;
    public int [][] adjacencyMatrix;
    public List<Course> courseList;
    public PenaltyCalculator calculator;

    public Graph(int courseCount){
        this.courseCount = courseCount;
        adjacencyMatrix = new int[courseCount][courseCount];
        courseList = new ArrayList<>();

        for(int i=0; i<courseCount; i++){
            courseList.add(new Course(i,0));
            for(int j=0; j<courseCount; j++){
                adjacencyMatrix[i][j] = 0;
            }
        }
    }

    public void addEdge(int c1, int c2){
        adjacencyMatrix[c1][c2] = 1;
        //adjacencyMatrix[c2][c1] = 1;
        courseList.get(c1).addConflict(c2);
    }

    public void printMatrix(){
        for(int i=0; i<courseCount; i++){
            for(int j=0; j<courseCount; j++){
                System.out.print(adjacencyMatrix[i][j]);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    public void printCourseConflictCount(){
        for(Course c: courseList){
            System.out.println(c.id + " conflicts: " +c.conflictingCourses.size());
        }
    }

    public void removeEdge(int c1, int c2){
        adjacencyMatrix[c1][c2] = 0;
    }

    public void printTimeSlots(){
        for(Course c: courseList){
            System.out.println(c.id + ": conflicts: "+ c.conflictCount() +  " timeslot: " + c.timeSlot);
        }
    }
}
