import java.util.*;

public class MyTreeMap{

    public static class MyEntry implements Submission {

        private String unikey;
        private Date timestamp;
        private Integer grade;

        public MyEntry(String unikey, Date timestamp, Integer grade)
        {
            this.unikey = unikey;
            this.timestamp = timestamp;
            this.grade = grade;
        }

        @Override
        public String getUnikey() { return unikey; }
        @Override
        public Date getTime() { return timestamp; }
        @Override
        public Integer getGrade() { return grade; }

        public void setGrade(Integer grade) { this.grade = grade; }
        public void setUnikey(String unikey) { this.unikey = unikey; }
        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    }

    private TreeMap<Date, MyEntry> map;             // map sorts the submissions based on date
    private TreeSet<Integer> grades;                // grades keeps track of best grades
    private HashMap<Integer, Integer> gradeCount;   // gradeCount keeps track of duplicates of best grades

    public MyTreeMap() {

        this.map = new TreeMap<>();
        this.grades = new TreeSet<>((x, y) -> y - x); // Lambda function to reverse ordering of grades
        this.gradeCount = new HashMap<>();
    }

    /**
     * Find the highest grade of any submission by this student
     *
     * complexity O(1)
     *
     * @return the best grade by this student, or null if they have made no
     *         submissions
     */
    protected Integer getBestGrade() {
        if(grades.size() > 0)
            return grades.first();
        else
            return 0;
    }

    /**
     * The most recent submission for a given student
     *
     * complexity O(1)
     *
     * @return Submission made most recently by that student, or null if the
     *         student has made no submissions
     */
    protected Submission getMostRecentSubmission() {
        if(map.size() > 0)
            return map.lastEntry().getValue();
        return null;
    }

    /**
     * The most recent submission for a given student, prior to a given time
     *
     * complexity O(log n)
     *
     * @param deadline The deadline after which no submissions are considered
     *
     * @return Submission made most recently by that student, or null if the
     *         student has made no submissions
     *
     * @throws IllegalArgumentException
     */
    protected Submission getSubmissionPriorToTime(Date deadline) throws IllegalArgumentException{

        Map.Entry<Date, MyEntry> entry = null;
        if((entry = map.floorEntry(deadline)) != null)  // floorEntry() returns a key-value mapping associated with the
            return entry.getValue();                    // greatest key less than or equal to the given key, or null if
        return null;                                    // there is no such key.
    }

    /**
     * Add a new submission
     *
     * complexity O(log n)
     *
     * For simplicity, you may assume that all submissions have unique times
     *
     * @param submission The Submission object created before passing
     *
     * @throws IllegalArgumentException
     *             if any argument is null
     */
    public void addSubmission(Submission submission) {

        if(gradeCount.containsKey(submission.getGrade())) {
            gradeCount.put(submission.getGrade(), gradeCount.get(submission.getGrade()) + 1);
        }
        else {
            gradeCount.put(submission.getGrade(), 1);
        }
        grades.add(submission.getGrade());
        map.put(submission.getTime(), (MyEntry) submission);
    }

    /**
     * Removes the submission provided
     *
     * complexity O(log n)
     *
     * @param submission The submission to remove
     *
     * @throws IllegalArgumentException
     */
    public boolean removeSubmission(Submission submission) {
        if(map.containsValue(submission)) // Check if the value is contained
        {
            // Decrease count of grade by 1 for each removal
            gradeCount.put(submission.getGrade(), gradeCount.get(submission.getGrade()) - 1);
            // Check if count for best grade reaches 0, remove it from TreeSet<Integer> grades
            if(submission.getGrade() == getBestGrade() && gradeCount.get(submission.getGrade()) == 0)
                grades.remove(submission.getGrade());
            map.remove(submission.getTime());
            return true;
        }
        return false;
    }

    /**
     * Checks if the current student is in regression
     *
     * complexity O(1)
     *
     * @return true if he is in regression
     */
    /*public boolean checkRegressionTrue() {
        if(getMostRecentSubmission().getGrade() < getBestGrade())
            return true;
        return false;
    }*/

}