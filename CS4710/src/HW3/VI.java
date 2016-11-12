package HW3;

import java.util.ArrayList;
import java.util.Scanner;

public class VI {
	static ArrayList<ArrayList<node>> nodelist = new ArrayList<ArrayList<node>>();
	static int iter = 0;
	static float pi = (float) 0.002;

	public static node swindmove(String direction, node x,
			ArrayList<ArrayList<node>> nodelist) {
		if (x.x != 3 && x.x != 5) {
			if (x.x == 4) {
				return windmove("stay", windmove(direction, x, nodelist),
						nodelist);
			} else {
				return move(direction, x, nodelist);
			}
		}
		if (x.x == 3) {
			if (direction.equals("W") || direction.equals("NW")
					|| direction.equals("SW")) {
				return move("N", windmove(direction, x, nodelist), nodelist);
			} else
				return (windmove("stay", windmove(direction, x, nodelist),
						nodelist));
		}
		if (x.x == 5) {
			if (direction.equals("E") || direction.equals("NE")
					|| direction.equals("SE")) {
				return move("N", windmove(direction, x, nodelist), nodelist);
			} else
				return (windmove("stay", windmove(direction, x, nodelist),
						nodelist));
		}
		return x;
	}

	public static node windmove(String direction, node x,
			ArrayList<ArrayList<node>> nodelist) {
		// System.out.println(move(direction, x, nodelist));
		node original = move(direction, x, nodelist);
		// System.out.println(x);
		// System.out.println("ORIGINIAL:" + original + direction);
		if (x.x != 3 && x.x != 4 && x.x != 5) {
			return original;
		}
		if (x.y > 1 && x.y < 6) {
			return (nodelist.get(original.y - 1).get(original.x));
		}
		if (x.y == 0 || x.y == 1) {
			if (direction.equals("N") || direction.equals("NE")
					|| direction.equals("NW")) {
				return original;
			}
			if (direction.equals("S") || direction.equals("SE")
					|| direction.equals("SW")) {
				return (nodelist.get(original.y - 1).get(original.x));
			}
			if (x.y == 0) {
				return original;
			}
			if (x.y == 1) {
				return (nodelist.get(original.y - 1).get(original.x));
			}
		}
		if (x.y == 6) {
			if (direction.equals("S") || direction.equals("SE")
					|| direction.equals("SW")) {
				return original;
			} else
				return (nodelist.get(original.y - 1).get(original.x));
		}
		return x;
	}

	public static node move(String direction, node x,
			ArrayList<ArrayList<node>> nodelist) {
		// System.out.println(nodelist);
		if (direction.equals("W")) {
			if (x.x > 0) {
				return nodelist.get(x.y).get(x.x - 1);
			} else {
				return x;
			}
		}
		if (direction.equals("E")) {
			if (x.x < 6) {
				// System.out.println(nodelist.get(x.y).get(x.x + 1));
				return nodelist.get(x.y).get(x.x + 1);
			} else {
				return x;
			}
		}
		if (direction.equals("N")) {
			if (x.y > 0) {
				return nodelist.get(x.y - 1).get(x.x);
			} else {
				return x;
			}
		}
		if (direction.equals("S")) {
			if (x.y < 6) {
				return nodelist.get(x.y + 1).get(x.x);
			} else {
				return x;
			}
		}
		if (direction.equals("stay")) {
			return x;
		}
		if (direction.equals("NW")) {
			if (x.x > 0 && x.y > 0) {
				return nodelist.get(x.y - 1).get(x.x - 1);
			}
			if (x.x > 0) {
				return nodelist.get(x.y).get(x.x - 1);
			}
			if (x.y > 0) {
				return nodelist.get(x.y - 1).get(x.x);
			} else {
				return x;
			}
		}
		if (direction.equals("NE")) {
			if (x.x < 6 && x.y > 0) {
				return nodelist.get(x.y - 1).get(x.x + 1);
			}
			if (x.x < 6) {
				return nodelist.get(x.y).get(x.x + 1);
			}
			if (x.y > 0) {
				return nodelist.get(x.y - 1).get(x.x);
			} else {
				return x;
			}
		}
		if (direction.equals("SE")) {
			if (x.x < 6 && x.y < 6) {
				return nodelist.get(x.y + 1).get(x.x + 1);
			}
			if (x.x < 6) {
				return nodelist.get(x.y).get(x.x + 1);
			}
			if (x.y < 6) {
				return nodelist.get(x.y + 1).get(x.x);
			} else {
				return x;
			}
		}
		if (direction.equals("SW")) {
			if (x.x > 0 && x.y < 6) {
				return nodelist.get(x.y + 1).get(x.x - 1);
			}
			if (x.x > 0) {
				return nodelist.get(x.y).get(x.x - 1);
			}
			if (x.y < 6) {
				return nodelist.get(x.y + 1).get(x.x);
			} else {
				return x;
			}
		}

		return x;
	}

	public static void swind() {
		while (iter < 1000 && pi > 0.001) {
			iter++;
			ArrayList<ArrayList<node>> tempnodelist = new ArrayList<ArrayList<node>>();

			for (int i = 0; i < 7; i++) {
				tempnodelist.add(new ArrayList<node>());
				for (int j = 0; j < 7; j++) {
					node temp2 = new node();
					temp2.x = nodelist.get(i).get(j).x;
					temp2.y = nodelist.get(i).get(j).y;
					temp2.value = nodelist.get(i).get(j).value;
					temp2.reward = nodelist.get(i).get(j).reward;
					tempnodelist.get(i).add(temp2);
				}
			}
			// System.out.println(tempnodelist);
			pi = 0;
			for (int i = 0; i < 7; i++) {
				String result = "";
				for (int j = 0; j < 7; j++) {
					float max = Integer.MIN_VALUE;
					node s = nodelist.get(i).get(j);
					if (swindmove("E", s, tempnodelist).value > max) {
						max = swindmove("E", s, tempnodelist).value;
					}
					if (swindmove("N", s, tempnodelist).value > max) {
						max = swindmove("N", s, tempnodelist).value;
					}
					if (swindmove("W", s, tempnodelist).value > max) {
						max = swindmove("W", s, tempnodelist).value;
					}
					if (swindmove("S", s, tempnodelist).value > max) {
						max = swindmove("S", s, tempnodelist).value;
					}
					if (swindmove("stay", s, tempnodelist).value > max) {
						max = swindmove("stay", s, tempnodelist).value;
					}
					if (swindmove("NE", s, tempnodelist).value > max) {
						max = swindmove("NE", s, tempnodelist).value;
					}
					if (swindmove("NW", s, tempnodelist).value > max) {
						max = swindmove("NW", s, tempnodelist).value;
					}
					if (swindmove("SE", s, tempnodelist).value > max) {
						max = swindmove("SE", s, tempnodelist).value;
					}
					if (swindmove("SW", s, tempnodelist).value > max) {
						max = swindmove("SW", s, tempnodelist).value;
					}
					float newvalue = nodelist.get(i).get(j).reward + max;
					nodelist.get(i).get(j).value = newvalue;
					/*
					 * System.out.println("ABSOLUTE VALUE IS:" +
					 * Math.abs(newvalue - tempnodelist.get(i).get(j).value));
					 * System.out.println("NEWVALUE IS:" + newvalue);
					 * System.out.println("OLDVALUE IS:" +
					 * tempnodelist.get(i).get(j).value);
					 */
					result += "   "
							+ String.valueOf(nodelist.get(i).get(j).value);

					if (Math.abs(newvalue - tempnodelist.get(i).get(j).value) > pi) {
						pi = Math.abs(newvalue
								- tempnodelist.get(i).get(j).value);
						// System.out.println("IIIIIIIIIIIIIIIIIIII");
					}
				}
				System.out.println(result);
			}
			System.out.println("############WHILELOOP#############");
		}
	}

	public static void wind() {
		System.out.println("H");
		while (iter < 1000 && pi > 0.001) {
			iter++;
			ArrayList<ArrayList<node>> tempnodelist = new ArrayList<ArrayList<node>>();

			for (int i = 0; i < 7; i++) {
				tempnodelist.add(new ArrayList<node>());
				for (int j = 0; j < 7; j++) {
					node temp2 = new node();
					temp2.x = nodelist.get(i).get(j).x;
					temp2.y = nodelist.get(i).get(j).y;
					temp2.value = nodelist.get(i).get(j).value;
					temp2.reward = nodelist.get(i).get(j).reward;
					tempnodelist.get(i).add(temp2);
				}
			}
			// System.out.println(tempnodelist);
			pi = 0;
			for (int i = 0; i < 7; i++) {
				String result = "";
				for (int j = 0; j < 7; j++) {
					float max = Integer.MIN_VALUE;
					node s = nodelist.get(i).get(j);
					if (windmove("E", s, tempnodelist).value > max) {
						max = windmove("E", s, tempnodelist).value;
					}
					if (windmove("N", s, tempnodelist).value > max) {
						max = windmove("N", s, tempnodelist).value;
					}
					if (windmove("W", s, tempnodelist).value > max) {
						max = windmove("W", s, tempnodelist).value;
					}
					if (windmove("S", s, tempnodelist).value > max) {
						max = windmove("S", s, tempnodelist).value;
					}
					if (windmove("stay", s, tempnodelist).value > max) {
						max = windmove("stay", s, tempnodelist).value;
					}
					if (windmove("NE", s, tempnodelist).value > max) {
						max = windmove("NE", s, tempnodelist).value;
					}
					if (windmove("NW", s, tempnodelist).value > max) {
						max = windmove("NW", s, tempnodelist).value;
					}
					if (windmove("SE", s, tempnodelist).value > max) {
						max = windmove("SE", s, tempnodelist).value;
					}
					if (windmove("SW", s, tempnodelist).value > max) {
						max = windmove("SW", s, tempnodelist).value;
					}
					float newvalue = nodelist.get(i).get(j).reward + max;
					nodelist.get(i).get(j).value = newvalue;
					/*
					 * System.out.println("ABSOLUTE VALUE IS:" +
					 * Math.abs(newvalue - tempnodelist.get(i).get(j).value));
					 * System.out.println("NEWVALUE IS:" + newvalue);
					 * System.out.println("OLDVALUE IS:" +
					 * tempnodelist.get(i).get(j).value);
					 */
					result += "   "
							+ String.valueOf(nodelist.get(i).get(j).value);

					if (Math.abs(newvalue - tempnodelist.get(i).get(j).value) > pi) {
						pi = Math.abs(newvalue
								- tempnodelist.get(i).get(j).value);
						// System.out.println("IIIIIIIIIIIIIIIIIIII");
					}
				}
				System.out.println(result);
			}
			System.out.println("############WHILELOOP#############");
		}
	}

	public static void nowind() {
		while (iter < 1000 && pi > 0.001) {
			iter++;
			ArrayList<ArrayList<node>> tempnodelist = new ArrayList<ArrayList<node>>();

			for (int i = 0; i < 7; i++) {
				tempnodelist.add(new ArrayList<node>());
				for (int j = 0; j < 7; j++) {
					node temp2 = new node();
					temp2.x = nodelist.get(i).get(j).x;
					temp2.y = nodelist.get(i).get(j).y;
					temp2.value = nodelist.get(i).get(j).value;
					temp2.reward = nodelist.get(i).get(j).reward;
					tempnodelist.get(i).add(temp2);
				}
			}
			pi = 0;
			for (int i = 0; i < 7; i++) {
				String result = "";
				for (int j = 0; j < 7; j++) {
					float max = Integer.MIN_VALUE;
					node s = nodelist.get(i).get(j);
					if (move("E", s, tempnodelist).value > max) {
						max = move("E", s, tempnodelist).value;
					}
					if (move("N", s, tempnodelist).value > max) {
						max = move("N", s, tempnodelist).value;
					}
					if (move("W", s, tempnodelist).value > max) {
						max = move("W", s, tempnodelist).value;
					}
					if (move("S", s, tempnodelist).value > max) {
						max = move("S", s, tempnodelist).value;
					}
					if (move("stay", s, tempnodelist).value > max) {
						max = move("stay", s, tempnodelist).value;
					}
					if (move("NE", s, tempnodelist).value > max) {
						max = move("NE", s, tempnodelist).value;
					}
					if (move("NW", s, tempnodelist).value > max) {
						max = move("NW", s, tempnodelist).value;
					}
					if (move("SE", s, tempnodelist).value > max) {
						max = move("SE", s, tempnodelist).value;
					}
					if (move("SW", s, tempnodelist).value > max) {
						max = move("SW", s, tempnodelist).value;
					}
					float newvalue = nodelist.get(i).get(j).reward + max;
					nodelist.get(i).get(j).value = newvalue;
					/*
					 * System.out.println("ABSOLUTE VALUE IS:" +
					 * Math.abs(newvalue - tempnodelist.get(i).get(j).value));
					 * System.out.println("NEWVALUE IS:" + newvalue);
					 * System.out.println("OLDVALUE IS:" +
					 * tempnodelist.get(i).get(j).value);
					 */
					result += "   "
							+ String.valueOf(nodelist.get(i).get(j).value);

					if (Math.abs(newvalue - tempnodelist.get(i).get(j).value) > pi) {
						pi = Math.abs(newvalue
								- tempnodelist.get(i).get(j).value);
						// System.out.println("IIIIIIIIIIIIIIIIIIII");
					}
				}
				System.out.println(result);
			}
			System.out.println("############WHILELOOP#############");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// building S

		for (int i = 0; i < 7; i++) {
			nodelist.add(new ArrayList<node>());
			for (int j = 0; j < 7; j++) {
				node temp = new node();
				temp.value = 0;
				temp.reward = -1;
				temp.x = j;
				temp.y = i;
				temp.action.add(temp);
				nodelist.get(i).add(temp);
			}
		}
		nodelist.get(3).get(6).reward = 0;
		// System.out.print(nodelist);
		System.out.println("Please enter your case number:");
		Scanner scanner = new Scanner(System.in);
		int wind = scanner.nextInt();
		if (wind == 1) {
			nowind();
		}
		if (wind == 2) {
			wind();
		}
		if (wind == 3) {
			swind();
		}
		// end of while loop
		/*
		 * for (int i = 0; i < 7; i++) { String result = ""; for (int j = 0; j <
		 * 7; j++) { result += "   " +
		 * String.valueOf(nodelist.get(i).get(j).value); }
		 * System.out.println(result); }
		 */

	}
}
