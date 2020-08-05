package KI_Essentials;

import java.util.ArrayList;

public class Tree implements Cloneable {
	public int treeValue;
	Node root;
	public ArrayList<Tree> childs = new ArrayList();
	public Tree clone() {
	     	return new Tree(this.getTreeValue(),this.getRoot(),this.getChilds());
	    }
	public int getTreeValue() {
		return treeValue;
	}
	public void setTreeValue(int treeValue) {
		this.treeValue = treeValue;
	}
	public Node getRoot() {
		return root;
	}
	public void setRoot(Node root) {
		this.root = root;
	}
	public ArrayList<Tree> getChilds() {
		return childs;
	}
	public void setChilds(ArrayList<Tree> childs) {
		this.childs = childs;
	}
	public Tree(Node n) {
		this.treeValue=n.value;
		this.root=n;
	}
	public Tree(Tree tr) {
		
	}
	public Tree(int treeValue,Node root,ArrayList<Tree> childs) {
		this.treeValue=treeValue;
		this.root=root;
		this.childs=childs;
	}
	public void addChild(int val){
		if (val>treeValue)
			setTreeValue(val);
		childs.add(new Tree(new Node(val)));
	}
	public void addChild(Tree tr) {
		if(tr.treeValue>treeValue)
			setTreeValue(tr.treeValue);
		childs.add(tr);
	}
	public int getValue() {
		int result=0;
		if(!childs.isEmpty()) {
			return root.value;
		}
		for(Tree t:childs){
			if(t.getValue()>result)
				result=t.getValue();
		}
		return result;
	}
}
