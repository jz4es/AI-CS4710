package HW2;

import java.awt.Point;
import java.util.ArrayList;

public class node extends Point {
	ArrayList<node> temppath = new ArrayList<node>();
	String value = "Unset";
	int g;
	int f = Integer.MAX_VALUE;
	int h;
	ArrayList<node> neighbor = new ArrayList<node>();
	node parent;

}
