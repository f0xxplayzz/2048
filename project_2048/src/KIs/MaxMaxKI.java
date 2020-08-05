package KIs;

import java.util.Random;

import KI_Essentials.Node;
import KI_Essentials.Tree;
import pack1.Field;

public class MaxMaxKI implements KI{
	Tree temp;
	int maxDepth;
	int maxValue=0;
	int[] result = new int[3];
	Random random= new Random();

	@Override
	public int[] createTile(Field f,int depth){
		if (depth>maxDepth) {
			maxDepth=depth;
		}
		f.main=false;
		if(depth>0) {
			for(int h=0; h<f.Y; h++) {
				for(int b=0;b<f.X;b++) {
					if (f.field[h][b].get().equals("")) {
						temp= new Tree(new Node(f.score));
						for(int k=1;k<3;k++) {
							int[] temp2 = new int[3];
							temp2[0]=h;
							temp2[1]=b;
							temp2[2]=k;
							temp= createTree(temp2.clone(),f.clone(),temp,depth);
							if(depth==maxDepth)
								if(temp.getTreeValue()>maxValue) {
									maxValue = temp.getTreeValue();
									result[0]=h;
									result[1]=b;
									result[2]=k;
								}
						}
					}
				}
			}
		}
		return result;
	}

	public Tree createTree(int[] k, Field f,Tree t, int depth) {
		f.field[k[0]][k[1]].setPotency(k[2]);
		Field up= f.clone();
		Field down = f.clone();
		Field right= f.clone();
		Field left = f.clone();
		Tree poss = t;
		if(depth>0) {
			depth--;
			down.moveDown();
			poss.addChild(down.score);
			poss.childs.get(0).addChild(createTree(createTile(down.clone(),depth),down,poss.childs.get(0),depth));
			up.moveUp();
			poss.addChild(up.score);
			poss.childs.get(1).addChild(createTree(createTile(up.clone(),depth),up,poss.childs.get(1),depth));
			right.moveRight();
			poss.addChild(right.score);
			poss.childs.get(2).addChild(createTree(createTile(right.clone(),depth),right,poss.childs.get(2),depth));
			left.moveLeft();
			poss.addChild(left.score);
			poss.childs.get(3).addChild(createTree(createTile(left.clone(),depth),left,poss.childs.get(3),depth));
			t=poss.clone();
		}
		return poss;
	}
}
