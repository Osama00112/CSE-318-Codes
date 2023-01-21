import java.util.List;

public class PenaltyCalculator {
    private int type; // type 1 = linear strategy, type 2 = exponenetial strategy
    private List<Student> studentList;
    private List<Course> courseList;

    public PenaltyCalculator(List<Student> studentList, int type){
        this.studentList = studentList;
        this.type = type;
    }
    public PenaltyCalculator(List<Student> studentList,List<Course> courseList, int type){
        this.studentList = studentList;
        this.type = type;
        this.courseList = courseList;
    }

    public double getAvgPenalty(){
        double totalPenalty = 0;
        double avgPenalty = 0;
        for(Student st:studentList){
            double penalty = 0;
            for(int i=0; i<st.courseList.size() - 1; i++ ){
                for(int j=i+1; j<st.courseList.size(); j++){
                    int gap = st.courseList.get(i).timeSlot - st.courseList.get(j).timeSlot;
                    if(gap < 0) gap *= -1;
                    if(gap == 0){
                        System.out.println("same timeslots, smth is wrong");
                    }
                    penalty += getPenalty(gap);
                }
            }
            totalPenalty += penalty;
        }
        if(studentList.size() != 0)
            avgPenalty = totalPenalty/studentList.size();

        return avgPenalty;
    }
    public double getAvgUpdatedPenalty(){
        courseList.sort(new CourseIdComparator());
        double totalPenalty = 0;
        double avgPenalty = 0;
        for(Student st:studentList){
            double penalty = 0;
            for(int i=0; i<st.courseList.size() - 1; i++ ){
                for(int j=i+1; j<st.courseList.size(); j++){

                    Course target = courseList.get(st.courseList.get(i).id - 1);
                    Course src = courseList.get(st.courseList.get(j).id - 1);
//                    System.out.println("before:" +
//                            + st.courseList.get(i).timeSlot +" " + st.courseList.get(j).timeSlot + " \n" +
//                            " After: " +
//                            + target.timeSlot + " " + src.timeSlot);
                    int gap = target.timeSlot - src.timeSlot;
                    if(gap < 0) gap *= -1;
                    if(gap == 0){
//                        System.out.println("before:" +
//                                + st.courseList.get(i).timeSlot +" " + st.courseList.get(j).timeSlot + " \n" +
//                                " After: " +
//                                + target.timeSlot + " " + src.timeSlot);
                        System.out.println("same timeslots, smth is wrong");
                    }
                    penalty += getPenalty(gap);
                }
            }
            totalPenalty += penalty;
        }
        if(studentList.size() != 0)
            avgPenalty = totalPenalty/studentList.size();

        return avgPenalty;
    }

    public double getPenalty(int gap){
        if(type == 1){
            if(gap <= 5){
                return 2*(5-gap);
            }else
                return 0;
        }else if(type == 2){
            if(gap <= 5){
                return Math.pow(2,(5-gap));
            }else
                return 0;
        }
        else{
            System.out.println("NO TYPE SELETED");
            return 0;
        }
    }

    public void setType(int type) {
        this.type = type;
    }

}
