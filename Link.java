/*Name: Link
propose: This class represents a number stored whithin the tree.
author: Gal Luvton and Daniel Sinaniev
Date Created: 19/5/2013
Last modification: 24/5/2013
*/

public class Link {

	/*Fields*/
	/*Fields*/
	//element- holds the numaric value of this given link
	private int element;
	//prev- points to the previous link in the list of numbers (spans over all the leafs in the tree)
	private Link prev;
	//next- points to the next link in the list (spans over all the leafs in the tree)
	private Link next;
	
	
	/*Behavior*/
	/*Constructors*/
	//this constructor sets the given values into the fields
	public Link(int element){
		this.element= element;
		this.prev= null;
		this.next= null;
	}//Link(Object)
	
	
	//data getter
	public int getElement() {
		return this.element;
	}//getData()
	
	
	//next getter
	public Link getNext() {
		return next;
	}//getNext()
	
	
	//next setter
	public void setNext(Link next) {
		this.next = next;
	}//setNext(Link)

	
	//prev getter
	public Link getPrev() {
		return prev;
	}//getPrev()
	

	//prev setter
	public void setPrev(Link prev) {
		this.prev = prev;
	}//setPrev(Link)

	
	//overrides the toString from Object class
	//returns a string representation of this link
	public String toString(){
			return "" + this.element;
	}//toString()



}//Link
