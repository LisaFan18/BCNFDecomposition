import java.util.LinkedList;

/**
*@author zhengjuan.fan@rutgers.edu
*@version Apr 12, 2018
*/
public class RelList {
	
	private LinkedList<Relation> relist;
	private int currIndex = 0; // for getNext()
	
	public RelList() {
		relist = new LinkedList<Relation>();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(relist.size()*Relation.MAX_LENGTH);
		for(Relation re: relist) {
			sb.append(re.toString());
			sb.append(", ");
		}
		
		return sb.toString();
	}
	
	public void insert(Relation r) {
		if(r == null) {
			System.out.println("Error! ReList.insert Relation is null");
			return;
		}
		
		relist.add(r);
	}
	
	public Relation getFirst() {
		if(relist.isEmpty()) {
			return null;
		}
		return relist.getFirst();
	}
	
	public Relation getNext() {
		if(currIndex >= relist.size()) {
			System.out.println("Error! No next Relation!");
			return null;
		}
		Relation next = relist.get(currIndex);
		currIndex++;
		return next;
	}
	
	public static void main(String[] args) {
		RelList res = new RelList();
		res.insert(new Relation("A"));
		res.insert(new Relation("A B C"));
		res.insert(new Relation("B C Z"));
		
		System.out.println(res.getFirst());
		System.out.println(res.getNext());
		System.out.println(res.getNext());
		System.out.println(res.getNext());
	}
	

}
