import java.util.*;

public class TreeMapPQ implements Comparable<TreeMapPQ> {

    private class MyEntry implements Submission {

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

    private PriorityQueue<Integer> gradeQ;
    private TreeMap<Date, MyEntry> map;

    public TreeMapPQ() {

        this.gradeQ = new PriorityQueue<>((x, y) -> y - x);
        this.map = new TreeMap<>();
    }

    @Override
    public int compareTo(TreeMapPQ treeMapPQ) {
        if(this.getBestGrade() > treeMapPQ.getBestGrade())
            return 1;
        else if(this.getBestGrade() == treeMapPQ.getBestGrade())
            return 0;
        else
            return -1;
    }

    /**
     * Find the highest grade of any submission by this student
     *
     * complexity O(1)
     *
     * @return the best grade by this student, or null if they have made no
     *         submissions
     */
    private Integer getBestGrade() {
        if(gradeQ.size() > 0)
            return gradeQ.peek();
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
    private Submission getMostRecentSubmission() {
        return map.lastEntry().getValue();
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
    private Submission getSubmissionPriorToTime(Date deadline) throws IllegalArgumentException{
           return map.lowerEntry(deadline).getValue();
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
    public void addSubmission(Submission submission) throws IllegalArgumentException {

        if(submission.getGrade() > getBestGrade())
            gradeQ.offer(submission.getGrade());
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
    public void removeSubmission(Submission submission) throws IllegalArgumentException {
        if(map.containsValue(submission)) // Check if the value is contained
        {
            if(submission.getGrade() == getBestGrade())
                gradeQ.poll();
            map.remove(submission);
        }
    }

    /**
     * Checks if the current student is in regression
     *
     * complexity O(1)
     *
     * @return true if he is in regression
     */
    public boolean checkRegressionTrue() {
        if(getMostRecentSubmission().getGrade() < getBestGrade())
            return true;
        return false;
    }

}