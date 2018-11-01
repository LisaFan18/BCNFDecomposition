/**
 * A functional dependency is an object containning two relations(sets). 
 * One on the left hand side and one on the right hand side
*@author zhengjuan.fan@rutgers.edu
*@version Apr 11, 2018
*/
public class Fd {
	
	private Relation lhs;
	private Relation rhs;
	
	public Fd(Relation in_lhs, Relation in_rhs) {
		lhs = new Relation(in_lhs.toString());
		rhs = new Relation(in_rhs.toString());
	}
	
	public String toString() {
		StringBuilder sblhs = new StringBuilder();
		StringBuilder sbrhs = new StringBuilder();
		int[] arr_lhs = lhs.getAttrs();
		for(int i=0; i< Relation.MAX_LENGTH; i++) {
			if(arr_lhs[i] == 1) {
				sblhs.append((char)(i + Relation.ASCIIA));
			}
		}
		
		int[] arr_rhs = rhs.getAttrs();
		for(int i=0; i< Relation.MAX_LENGTH; i++) {
			if(arr_rhs[i] == 1) {
				sbrhs.append((char)(i + Relation.ASCIIA));
			}
		}
		
		return sblhs.toString() + " -> " + sbrhs.toString();
	}
	
	/**
	 * check if this functional dependency is a BCNF violation with respect to the given set of attributes of relation s
	 * @param s set of attributes of relation 
	 * @return {@code true} if this fd is a BCNF violation
	 */
	public boolean BCNFviolation(Relation s) {
		
		// X must not be a superkey, i.e. XY must not contain all attributes in S which can be checked with the condition
		Relation lrhs = lhs.union(rhs);
		if(!s.subset(lrhs)) {
			return true; 
		}
		
		// X → Y must be a valid functional dependency with respect to S
		return !(lhs.subset(s) && !(rhs.intersect(s).isAttriEmpty())) ;
	}
	
	Relation getLHS() {
		return this.lhs;
	}

	Relation getRHS() {
		return this.rhs;
	}
	
	public static void main(String[] args) {
		// test toString()
		String in_r = "A B";
		String in_r2 = "C D";

		// test BCNFviolation()
		// String in_r = "A";
		// String in_r2 = "C";

		Relation lhrFd = new Relation(in_r);
		Relation rhsFd = new Relation(in_r2);
		Fd fd = new Fd(lhrFd, rhsFd);
		System.out.println(fd.toString());

		String s = "A B C";
		Relation sFd = new Relation(s);
		System.out.println(fd.BCNFviolation(sFd));

	}
	
}
