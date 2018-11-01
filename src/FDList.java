import java.util.LinkedList;

/**
 * @author zhengjuan.fan@rutgers.edu
 * @version Apr 12, 2018
 */
public class FDList {

	private LinkedList<Fd> fdList;
	private int currIndex = 0; // for getNext() 

	public FDList() {
		fdList = new LinkedList<Fd>();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(fdList.size()*Relation.MAX_LENGTH);
		for(Fd f: fdList) {
			sb.append(f.toString());
			sb.append(", ");
		}
		return sb.toString();
	}
	
	public void insert(Fd f) {
		if(f == null) {
			System.out.println("Error! FdList.insert Fd is null");
			return;
		}
		fdList.add(f);
	}
	

	/**
	 * Return the first functional dependencies
	 * @return  the first functional dependencies
	 */
	public Fd getFirst() {
		if (fdList.isEmpty()) {
			return null;
		}
		currIndex = 0; // initialize
		return fdList.get(currIndex);
	}

	/**
	 *  Return the next functional dependencies. Note: you must first call {@code getFirst()} before call it.
	 * @return the next functional dependencies
	 */
	public Fd getNext() {
		if(currIndex >= fdList.size()) {
			System.out.println("Error! No next Fd!");
			return null;
		}
		Fd nextFd = fdList.get(currIndex);
		currIndex++;
		return nextFd;
	}
	
	public int size() {
		return fdList.size();
	}
	
	/**
	 * Compute the closure of r under a set of FD's
	 * @param r a relation
	 * @return the closure 
	 */
	Relation closure(Relation r) {
		if(r == null) {
			System.out.println(this.getClass().getName() + ".method closure() has NULL input Relation !");
			return null;
		}
		
		Relation result = new Relation(r.toString()); 
		boolean found = true;
		while(found){
			found = false;
			for(Fd fd : fdList){
				if(fd.getLHS().subset(result) && !fd.getRHS().subset(result)){
					result = result.union(fd.getRHS());
					found = true;
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		 // test toString()
		 FDList fds = new FDList();
		 Fd fd1 = new Fd(new Relation("A"), new Relation("B"));
		 Fd fd2 = new Fd(new Relation("B"), new Relation("C"));
		 fds.insert(fd2);
		 fds.insert(fd1);
		// System.out.println(fds.toString());
		 
		 FDList fds2 = new FDList();
		 Fd fd21 = new Fd(new Relation("A B"), new Relation("C"));
		 Fd fd22 = new Fd(new Relation("B"), new Relation("C D"));
		 fds2.insert(fd21);
		 fds2.insert(fd22);
		 
//		 System.out.println(fds2.toString());
		 
		 // test getFirst()
//		 System.out.println(fds.getFirst().toString());
		 
		 // test getNext()
		// System.out.println(fds.getNext().toString());
		// System.out.println(fds.getNext().toString());
		 
		 // test closure()
		// System.out.println(fds.closure(new Relation("A")).toString());
		 System.out.println(fds.closure(new Relation("B")).toString());
		 
	}
	
}
