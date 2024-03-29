import java.util.*;

public class Assignment implements SubmissionHistory {

	private HashMap<String , MyTreeMap> bigMap; // bigMap stores the MyTreeMap for every student
    private TreeMap<Integer, TreeSet<String>> bestSortedGrades; // bestGrades to dynamically keep track of a student's top grade
	/**
	 * Default constructor
	 */
	public Assignment() {
        bigMap = new HashMap<>();
	    bestSortedGrades = new TreeMap<>();
	}

    /**
     *
     * @param unikey
     *            The student to filter on
     * @return Submission made most recently by that student, or null if the
     *         student has made no submissions
     * @throws IllegalArgumentException
     *            If the argument is null or is not a String object
     */
	@Override
	public Integer getBestGrade(String unikey) throws IllegalArgumentException {
	    if(!(unikey instanceof String ))
	        throw new IllegalArgumentException();
	    if(bigMap.containsKey(unikey))                  // If student has entries in bigMap
	        return bigMap.get(unikey).getBestGrade();   // Return best grade
        return null;                                    // Else return null
	}

    /**
     * \
     * @param unikey
     *            The student to filter on
     * @return Submission made most recently by that student, or null if the
     *         student has made no submissions
     * @throws IllegalArgumentException
     *            If the argument is null or is not a String object
     */
	@Override
	public Submission getSubmissionFinal(String unikey) throws IllegalArgumentException{
        if(!(unikey instanceof String ))
            throw new IllegalArgumentException();
        if(bigMap.containsKey(unikey))                          // If student has entries in bigMap
		    return bigMap.get(unikey).getMostRecentSubmission();// Return the most recent submission
        return null;                                            // Else return null
	}

    /**
     *
     * @param unikey
     *            The student to filter on
     * @param deadline
     *            The deadline after which no submissions are considered
     * @return Submission made most recently by that student, or null if the
     *         student has made no submissions
     * @throws IllegalArgumentException
     *            If the argument is null
     */
	@Override
	public Submission getSubmissionBefore(String unikey, Date deadline) throws IllegalArgumentException{
        if(!(unikey instanceof String) || !(deadline instanceof Date))
            throw new IllegalArgumentException();
        MyTreeMap treeMap = null;
        if((treeMap = bigMap.get(unikey)) != null)
		    return treeMap.getSubmissionPriorToTime(deadline);   // Function returns appropriately
        else
            return null;
	}

    /**
     *
     * @param unikey
     * @param timestamp
     * @param grade
     * @return the Submission object that was created
     * @throws IllegalArgumentException
     *         If the arguments are null
     */
	@Override
	public Submission add(String unikey, Date timestamp, Integer grade) throws IllegalArgumentException{
        if(!(unikey instanceof String) || !(timestamp instanceof Date) || !(grade instanceof Integer))
            throw new IllegalArgumentException();

        Submission submission = null;

        if(!bigMap.containsKey(unikey))             // If the student does not already have an entry in bigMap
        {
            MyTreeMap myTreeMap = new MyTreeMap();  // Make new MyTreeMap
            submission = new MyTreeMap.MyEntry(unikey, timestamp, grade);
            myTreeMap.addSubmission(submission);    // And add new submission to MyTreeMap
            bigMap.put(unikey, myTreeMap);          // Make a new entry for student with the new MyTreeMap
            //bestGrades.put(unikey, grade);          // Make a new entry for best grades for that student
            if(bestSortedGrades.containsKey(grade))
            {
                bestSortedGrades.get(grade).add(unikey);
            }
            else
            {
                TreeSet<String> newTreeSet = new TreeSet<>();
                newTreeSet.add(unikey);
                bestSortedGrades.put(grade, newTreeSet);
            }
        }
        else
        {
            MyTreeMap treeMap = bigMap.get(unikey);     // If student already exists in the bigMap
            int prevBestGrade = treeMap.getBestGrade(); // Get the student's current best grade - O(log n)
            submission = new MyTreeMap.MyEntry(unikey, timestamp, grade);
            treeMap.addSubmission(submission);          // Add a new submission to MyTreeMap

            if (grade > prevBestGrade) {                // If this grade is greater than the current best grade
                if(bestSortedGrades.containsKey(grade)) // Change best grade to new grade
                {
                    bestSortedGrades.get(grade).add(unikey); // If the grade is already contained, add to it
                }
                else
                {
                    TreeSet<String> newTreeSet = new TreeSet<>(); //Otherwise, create new entry for grade and put the unikey
                    newTreeSet.add(unikey);
                    bestSortedGrades.put(grade, newTreeSet);
                }
            }
        }
        return submission;
	}

    /**
     *
     * @param submission
     *            The Submission to remove
     * @throws IllegalArgumentException
     *         If the argument is null or is not a Submission
     */
	@Override
	public void remove(Submission submission) throws IllegalArgumentException{
        if(!(submission instanceof Submission))
            throw new IllegalArgumentException();
        if(bigMap.size() == 0)          // If the bigMap doesn't contain entries then return
            return;
        MyTreeMap treeMap= bigMap.get(submission.getUnikey());  // Get the treeMap - O(1)
        int prevBestGrade = treeMap.getBestGrade();             // Get the current best grade - O(log n)
		if(treeMap.removeSubmission(submission) &&              // If removeSubmission returns true
                prevBestGrade == submission.getGrade())         // And current best grade is the same as removed grade
        {                                                       // Put the next best grade in bestGrades
            bestSortedGrades.get(submission.getGrade()).remove(submission.getUnikey());
        }
	}

    /**
     *
     * @return The List of top students
     */
	@Override
	public List<String> listTopStudents() {
        if(bestSortedGrades.lastEntry() == null) return new ArrayList<String>(); // Return empty ArrayList if DS is empty
        List<String> list = new ArrayList<String>(bestSortedGrades.lastEntry().getValue()); //  Return the best grade TreeSet as list
		return list;
	}

    /**
     *
     * @return The list of Regressed Students
     */
	@Override
	public List<String> listRegressions() {
		ArrayList<String> regressedStudents = new ArrayList<>();
        for(Map.Entry<String, MyTreeMap> entry : bigMap.entrySet()) // Iterate through every student
        {
            Integer bestGrade = entry.getValue().getBestGrade();                                  // Get student's best grade
            MyTreeMap myTreeMap = entry.getValue();
            Submission recentSub = myTreeMap.getMostRecentSubmission();
            if(recentSub == null) continue;
                Integer mostRecentSubmission = recentSub.getGrade(); // Get student's latest submission
            if(bestGrade > mostRecentSubmission)
                regressedStudents.add(entry.getKey());
        }
        return regressedStudents;
	}
	
}
