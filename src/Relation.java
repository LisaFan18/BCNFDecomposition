/**
*@author zhengjuan.fan@rutgers.edu
*@version Apr 11, 2018
*/
public class Relation {
	
	private int[] attrs; 
	private String[] powerSet;
	private int size; 
	private int powerSetCurr;  // the leading one is empty set  
	private int powerSetSize;
	protected static final int MAX_LENGTH = 26; // the maximum number of attributes is 26
	protected static final int ASCIIA = 65; // 'A'
	
	public Relation(String in_r) {
		// initialize
		attrs = new int[MAX_LENGTH]; 
		// save attributes
		for(Character c: in_r.toCharArray()) {
			if (c == ' ') {
				continue;
			}  
			
			if (c < ASCIIA || c > ASCIIA + 32) {
				System.out.println(
						"Invalid Attributes: " + c + "! Possible attributes are the 26 letters from A through Z.");
				return;
			}
			
			//System.out.println(c);
			this.attrs[c - ASCIIA] = 1;
			this.size++;
		}
	}
	
	/**
	 * Return the first element of power sets and this first element is empty set
	 * @return  the first element of power sets 
	 */
	public Relation powerSetFirst() {
		computePowerset();
		return new Relation(powerSet[powerSetCurr]);
	}
	
	/**
	 *  Return the next element of power sets. Note: you must first call {@code powerSetFirst()} before call it
	 * @return the next element of power sets
	 */
	public Relation powerSetNext() {
		if(powerSetCurr >= powerSetSize) {
			System.out.println("Error! No next powerSet!");
			return null;
		}
		
		Relation next = new Relation(powerSet[powerSetCurr]);
		powerSetCurr++;
		return next;
	}
	
	public int[] getAttrs() {
		return attrs;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(MAX_LENGTH);
		for (int i = 0; i < MAX_LENGTH; i++) {
			if (attrs[i] == 1) {
				sb.append((char)(i + ASCIIA));
			}
		}
		return sb.toString();
	}
	
	public boolean equals(Relation r2) {
		if (this.size != r2.size) {
			return false;
		}

		for (int i = 0; i < MAX_LENGTH; i++) {
			if (attrs[i] != r2.attrs[i]) {
				return false;
			}
		}

		return true;
	}
	
	public boolean contains(char c) {
		// check if character is alphabet
		if(c <'A' || c > 'Z') {
			return false;
		}
		
		return this.attrs[c - ASCIIA] == 1;
	}
	
	public boolean subset(Relation r2) {
		if(this.size > r2.size) {
			return false;
		}
		
		for(int i=0; i< MAX_LENGTH; i++) {
			if(attrs[i] == 1) {
				if(r2.attrs[i] == 0) {
					return false;
				}
			}
		}
		
	   return true;
	}
	
	/**
	 * compute the union of two Relations
	 * @param r2
	 * @return the union of this and r2
	 */
	public Relation union(Relation r2) {
		Relation r3 = new Relation();
		
		for(int i=0; i< MAX_LENGTH; i++) {
			if(attrs[i] == 1 || r2.attrs[i] == 1) {
				r3.attrs[i] = 1;
				r3.size++;
			}
		}
		
		return r3;
	}
	
	/**
	 * compute the intersect of two Relations
	 * @param r2
	 * @return the intersect of this and r2
	 */
	public Relation intersect(Relation r2) {
		Relation r3 = new Relation();
		for(int i=0; i< MAX_LENGTH; i++) {
			if(attrs[i] == 1 && r2.attrs[i] == 1) {
				r3.attrs[i] = 1;
				r3.size++;
			}
		}
		
		return r3;
	}
	
	/**
	 * compute the difference of two Relations 
	 * @param r2
	 * @return the difference: in r1 not in r2 
	 */
	public Relation difference(Relation r2) {
		Relation r3 = new Relation();
		for(int i=0; i< MAX_LENGTH; i++) {
			if(attrs[i] == 1 && r2.attrs[i] == 0) {
				r3.attrs[i] = 1;
				r3.size++;
			}
		}
		
		return r3;
	}
	
	public boolean isAttriEmpty() {
		return size == 0;
	}
	
	public int powerSetSize() {
		return this.powerSetSize;
	}
	
	private Relation() {
		attrs = new int[MAX_LENGTH]; 
		size = 0;
	}
	
	private void computePowerset() {
		powerSetCurr = 0; 
		powerSetSize = (int)Math.pow(2, size); 
		powerSet = new String[powerSetSize];
		String input = this.toString();
		for (int k = 0; k < powerSetSize; k++) 
	      {
	         String set = "";
	         int binaryDigits = k;
	         for (int j = 0; j < size; j++)
	         {
	            if (binaryDigits % 2 == 1)
	               set += input.charAt(j);
	            binaryDigits >>= 1;
	         }
	         powerSet[k] = set;
	      }
	}
	
   public static void main(String[] args) {
//	   String in_r = "C D Z";
//	   String in_r2 = "A D E Z";
	   
	   String in_r = "A B C";
//	   String in_r2 = "A B C";
//	   
	   Relation r = new Relation(in_r);
//	   Relation r2 = new Relation(in_r2);
	   System.out.println("toString(): " + r.toString());
//	   System.out.println("subset(): " + r.subset(r2));
//	   
//	   System.out.println("union()" + r.union(r2));
//	   System.out.println("intersect()" + r.intersect(r2));
	   
	   // test powerset
		System.out.println("powerSetFirst(): " + r.powerSetFirst().toString());
		System.out.println("powerSetNext(): Start");
		for (int i = 1; i < r.powerSetSize; i++) {
			System.out.println(r.powerSetNext());
		}
		System.out.println("powerSetNext():  End");
	   
   }

}
