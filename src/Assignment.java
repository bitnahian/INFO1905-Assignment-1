import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.*;

public class Assignment implements SubmissionHistory {

    public class BigMap {
        private TreeMap<TreeSet<Integer>, MyTreeMap> bigMap;
    }

	private HashMap<String , MyTreeMap> bigMap;
	private HashMap<String, Integer> bestGrades; // This needs to be sorted

	/**
	 * Default constructor
	 */
	public Assignment() {

	    bigMap = new HashMap<>();
	    bestGrades = new HashMap<>(); // For sorting in descending order
		// TODO initialise your data structures
	}

	@Override
	public Integer getBestGrade(String unikey) throws IllegalArgumentException {
	    if(!(unikey instanceof String ))
	        throw new IllegalArgumentException();
	    if(bigMap.containsKey(unikey))
	        return bigMap.get(unikey).getBestGrade(); // O(1) + O(log n) = O(log n)
        return null;
		// TODO Implement this, ideally in better than O(n)

	}

	@Override
	public Submission getSubmissionFinal(String unikey) throws IllegalArgumentException{
		// TODO Implement this, ideally in better than O(n)
        if(!(unikey instanceof String ))
            throw new IllegalArgumentException();
        if(bigMap.containsKey(unikey))
		    return bigMap.get(unikey).getMostRecentSubmission();
        return null;
	}

	@Override
	public Submission getSubmissionBefore(String unikey, Date deadline) throws IllegalArgumentException{
		// TODO Implement this, ideally in better than O(n)
        if(!(unikey instanceof String) || !(deadline instanceof Date))
            throw new IllegalArgumentException();

		return bigMap.get(unikey).getSubmissionPriorToTime(deadline);
	}

	@Override
	public Submission add(String unikey, Date timestamp, Integer grade) throws IllegalArgumentException{
		// TODO Implement this, ideally in better than O(n)
        // Get prev best grade
        if(!(unikey instanceof String) || !(timestamp instanceof Date) || !(grade instanceof Integer))
            throw new IllegalArgumentException();


        if(!bigMap.containsKey(unikey))
        {
            MyTreeMap myTreeMap = new MyTreeMap();
            Submission submission = new MyTreeMap.MyEntry(unikey, timestamp, grade);
            myTreeMap.addSubmission(submission);
            bigMap.put(unikey, myTreeMap);
            bestGrades.put(unikey, grade);
            return submission;
        }
        else
        {
            // If student already exists
            MyTreeMap treeMap = bigMap.get(unikey);
            int prevBestGrade = treeMap.getBestGrade(); // O(log n)
            Submission submission = new MyTreeMap.MyEntry(unikey, timestamp, grade);
            treeMap.addSubmission(submission);
            // If this grade has become the best grade
            if (grade > prevBestGrade)
                bestGrades.put(unikey, grade);
            return submission;
        }
	}

	@Override
	public void remove(Submission submission) throws IllegalArgumentException{
		// TODO Implement this, ideally in better than O
        if(!(submission instanceof Submission))
            throw new IllegalArgumentException();
        if(bigMap.size() == 0)
            return;
        MyTreeMap treeMap= bigMap.get(submission.getUnikey()); // O(1)
        int prevBestGrade = treeMap.getBestGrade(); //O(log n)
		if(treeMap.removeSubmission(submission) && // O(log n)
                prevBestGrade == submission.getGrade())
        {
            bestGrades.put(submission.getUnikey(), treeMap.getBestGrade()); // O(log n) + O(1)
        }
	}

	@Override
	public List<String> listTopStudents() {
		// TODO Implement this, ideally in better than O(n)
		// (you may ignore the length of the list in the analysis)
        ArrayList<String> topStudents = new ArrayList<>();
        int max = 0;
        for(Map.Entry<String, Integer> entry : bestGrades.entrySet())
        // Iteration over HashMap STRICTLY SMALLER THAN n AS SPECIFIED < O(n)
        {
            if(entry.getValue() > max) {
                max = entry.getValue();
                topStudents.clear();
                topStudents.add(entry.getKey());
            }
            else if(entry.getValue() == max)
            {
                topStudents.add(entry.getKey());
            }

        }
        // Get grades from HashMap, which is significantly smaller than
		return topStudents;
	}

	@Override
	public List<String> listRegressions() {
		// TODO Implement this, ideally in better than O(n^2)
		ArrayList<String> regressedStudents = new ArrayList<>();
        for(Map.Entry<String, MyTreeMap> entry : bigMap.entrySet())
        {
            if(entry.getValue().getBestGrade() > entry.getValue().getMostRecentSubmission().getGrade())
                regressedStudents.add(entry.getKey());
        }
        return regressedStudents;
	}
	
}
