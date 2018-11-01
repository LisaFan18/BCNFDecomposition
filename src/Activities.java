import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengjuan.fan@rutgers.edu
 * @version Apr 17, 2018
 */
public class Activities {
	
	public static void main(String[] args) {
		String inFileName = "input1.txt";
		String outFileName = "output1.txt";
		Relation r;
		FDList fds; 
		
		try {
			// load data from file
			BufferedReader in = new BufferedReader(new FileReader(inFileName));
			String line = in.readLine();
			r = new Relation(line.trim());
			
			fds = new FDList();
			while ((line = in.readLine()) != null) {
				String[] halves = line.split("->");
				Fd fd = new Fd(new Relation(halves[0]), new Relation(halves[1]));
				fds.insert(fd);
			}
			in.close();
			
			BufferedWriter out = new BufferedWriter(new FileWriter(outFileName));
			out.write("Relations: " +  r.toString());
			out.newLine();
			out.newLine();
			
			out.write("Function dependencies: " + fds.toString());
			out.newLine();
			out.newLine();
			
			FDList allFds = getAllFds(r, fds);
			FDList nonTrivalFds = getNonTrivialFds(allFds);  
			
			out.write("--------------------------- problem 1 ---------------------------");
			out.newLine();
			out.newLine();
			out.write("All non-trivial functional dependencies ( #" + nonTrivalFds.size() + " ) : ");
			out.newLine();
			out.write(nonTrivalFds.toString());
			out.newLine();
			out.newLine();
			
			FDList bcnfViolations= getBCNFviolations(r, nonTrivalFds);  
			out.write("--------------------------- problem 2 ---------------------------");
			out.newLine();
			out.newLine();
			out.write("BCNF violations ( #"  + bcnfViolations.size() + " ) : ");
			out.newLine();
			out.write(bcnfViolations.toString());
			out.newLine();
			out.newLine();
			
			Map<Relation, FDList> rs =  decomposeToBCNF(r, fds);
			out.write("--------------------------- problem 3 ---------------------------");
			out.newLine();
			out.newLine();
			out.write("The BCNF decomposed Relations ( #"  + rs.size() + " ) : ");
			out.newLine();
			for(Map.Entry<Relation, FDList> entry: rs.entrySet()) {
				out.write("(" + entry.getKey().toString() + "), ");
			}
			out.newLine();
			
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compute the BNCF decomposition of a given relation and a set of functional dependencies
	 * @param r Relations
	 * @param fds the functional dependencies in Relations r
	 * @return the result of BCNF decomposition
	 */
	public static Map<Relation, FDList> decomposeToBCNF(Relation r, FDList fds) {
		Map<Relation, FDList> rs = new HashMap<Relation, FDList>();
		if(fds == null || fds.size() == 0) {
			rs.put(r, fds);
			return rs;
		}
		
		//check if it's already in BCNF
		FDList allFds = getAllFds(r, fds);
		FDList nonTrivalFds = getNonTrivialFds(allFds);
		FDList bcnfViolationFds = getBCNFviolations( r,  nonTrivalFds);
		if(bcnfViolationFds.size() == 0) {
			rs.put(r, fds);
			return rs;
		}
		
		// if not, pick a bcnfViolation fd to decompose
		Fd fd = bcnfViolationFds.getFirst(); 
		//System.out.println("bcnfViolationFds: " + fd.toString());
		
		Relation lhs = fd.getLHS(); // X 
		Relation lhsClosure = bcnfViolationFds.closure(lhs); // X+
		Relation r1 = lhsClosure; // R1(XY)
		Relation z = r.difference(r1); // Z 
		Relation r2 = lhs.union(z); // R2(XZ)
		
		// projection
		FDList fds1 = projection(r1, fds);
		FDList fds2 = projection(r2, fds);
		
		//System.out.println("r1=" + r1.toString() + ", fds1= " + fds1.toString());
		//System.out.println("r2=" + r2.toString() + ", fds2= " + fds2.toString());
		rs.putAll(decomposeToBCNF(r1, fds1));
		rs.putAll(decomposeToBCNF(r2, fds2));
		
		return rs;
	}
	
	public static FDList projection(Relation r, FDList fds) {
		if (fds == null || r == null) {
			return null;
		}
		
		FDList rs = new FDList();
		Fd fd = fds.getFirst();
		for(int i=0; i<fds.size(); i++) {
			fd = fds.getNext();
			if(fd.getLHS().subset(r) && fd.getRHS().subset(r)) {
				rs.insert(fd);
			}
		}
		
		return rs;
	}
	
	/**
	 * compute the closure of all possible subset of relation's power sets
	 * @param r Relations 
	 * @param fds Functional dependencies
	 * @return the closure of all possible fds
	 */
	public static FDList getAllFds(Relation r, FDList fds) {
		FDList allFds = new FDList();
		
		Relation powerSet = r.powerSetFirst(); 
		powerSet = r.powerSetNext();  // remove empty set
		for(int i=1; i<r.powerSetSize(); i++) {
			powerSet = r.powerSetNext(); 
			//System.out.println(r.toString() + " has powerset: " + powerSet.toString());
			Relation lhs = powerSet;
			Relation rhs = fds.closure(lhs);
			
			allFds.insert(new Fd(lhs, rhs));
		}
		
		return allFds;
	}
	
	/**
	 * Compute all non-trivial functional dependencies which must be that X -> X+ - X
	 * @param fds functional dependencies
	 * @return all non-trivial fds
	 */
	public static FDList getNonTrivialFds(FDList fds) {
		FDList rs = new FDList();
		
		Fd fd = fds.getFirst();
		Relation lhs = fd.getLHS();
		Relation rhs = fd.getRHS();
		
		for(int i=0; i<fds.size(); i++) {
			fd = fds.getNext();
			lhs = fd.getLHS();
			rhs = fd.getRHS();
			
			// filter out X --> X
			if (!lhs.equals(rhs)) {
				// remove lhs from rhs
				Relation newRHS = rhs.difference(lhs);
				rs.insert(new Fd(lhs, newRHS));
			}
			
		}
		
		return rs;
	}
	
	/**
	 * Compute all functional dependencies from fds are BCNF violations 
	 * @param r Relations
	 * @param fds functional dependencies
	 * @return all BCNF violations 
	 */
	public static FDList getBCNFviolations(Relation r, FDList fds) {
		FDList rs = new FDList();
		
		Fd fd = fds.getFirst();
		for(int i=0; i<fds.size(); i++) {
			fd = fds.getNext();
			
			if (fd.BCNFviolation(r)) {
				rs.insert(fd);
			}
			
		}
		
		return rs;
	}

}
