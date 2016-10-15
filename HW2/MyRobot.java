package HW2;

// Feel free to use this java file as a template and extend it to write your solver.
// ---------------------------------------------------------------------------------

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import world.Robot;
import world.World;

public class MyRobot extends Robot {
	boolean isUncertain;
	static ArrayList<node> open = new ArrayList<node>();
	static ArrayList<node> closed = new ArrayList<node>();
	static ArrayList<node> path = new ArrayList<node>();
	static ArrayList<node> wnode = new ArrayList<node>();
	static node start;
	static node end;

	public void travel(node x) {
		for (int i = 0; i < x.temppath.size(); i++) {
			this.move(x.temppath.get(i));
		}
	}

	public void travelback(node x) {
		for (int i = x.temppath.size() - 1; i > -1; i--) {
			this.move(x.temppath.get(i));
		}
	}

	public void checkPing(node x) {
		int count = 0;
		int distance = Math.abs(x.x - start.x) + Math.abs(x.y - start.y);
		distance *= distance;
		if (distance < 12) {
			String a = this.pingMap(x);
			String b = this.pingMap(x);
			String c = this.pingMap(x);
			if (a.equals("X")) {
				if (b.equals("X") && c.equals("X")) {
					x.value = "X?";
					if (!closed.contains(x)) {
						closed.add(x);
						// System.out.println("checkPING ADD CLosed node" + x);
					}
				} else
					x.value = "Unsure";
			}
			if (a.equals("O")) {
				if (b.equals("O") && c.equals("O")) {
					x.value = "O?";
					// System.out.println(x + "Setting O?");
				} else
					x.value = "Unsure";
			} else
				x.value = "Unsure";
		}

		else {
			for (int i = 0; i < distance; i++) {
				if (this.pingMap(x).equals("X")) {
					count++;
				}
			}
			if (count > 6 * distance / 10) {
				x.value = "X?";
				if (!closed.contains(x)) {
					closed.add(x);
				}
			} else if (count > 4 * distance / 10) {
				x.value = "Unsure";
			} else {
				x.value = "O?";
			}
		}
	}

	@Override
	public void travelToDestination() {
		Point p = this.getPosition();
		if (isUncertain) {

			// call function to deal with uncertainty
			node current = start;
			closed.add(current);
			while (!current.neighbor.contains(end)) {
				// System.out.println("NEIGHBOR" + current.neighbor.toString());
				for (int j = 0; j < current.neighbor.size(); j++) {// set f
																	// value and
																	// parent
																	// for
																	// neighbor
					// node

					node temp = current.neighbor.get(j);
					while (temp.value.equals("Unset")
							|| temp.value.equals("Unsure")) {
						this.checkPing(temp);
					}
					if (temp.f > current.g + 1 + temp.h
							&& !closed.contains(temp)
							&& !temp.value.equals("X?")
							&& !temp.value.equals("X")) {
						// if node O, node value needs update, nodes not in the
						// closed list
						// System.out.println("YYYYYYYYYYYYYYYYYYYYYYY");
						temp.g = current.g + 1;
						temp.f = temp.g + temp.h;
						if (!open.contains(temp)) {
							open.add(temp);
						}
						temp.parent = current;
					}
				}

				int min = Integer.MAX_VALUE;
				for (int k = 0; k < open.size(); k++) {// find minimum f
					if (open.get(k).f <= min) {
						min = open.get(k).f;
					}
				}
				// System.out.println("MIN F" + min);
				loop: for (int i = 0; i < open.size(); i++) {// add it to closed
																// list,
																// // remove
																// from open
					// list

					if (open.get(i).f == min) {
						if (open.get(i).value.equals("Unsure")) {
							// if (this.move(open.get(i)).equals(current)) {
							if (this.pingMap(open.get(i)).equals("X")) {
								open.get(i).value = "X";
								if (!closed.contains(open.get(i))) {
									closed.add(open.get(i));
								}
							} else {
								open.get(i).value = "O";
								// this.move(current);
								if (!closed.contains(open.get(i))) {
									closed.add(open.get(i));
								}
								open.get(i).temppath.add(current);
								current = open.get(i);

							}
							open.remove(i);
						} else {
							if (!closed.contains(open.get(i))) {
								closed.add(open.get(i));
							}
							// System.out.println("ADD CLosed node" +
							// open.get(i));
							open.get(i).temppath.add(current);
							current = open.get(i);
							open.remove(i);

							break loop;
						}
					}
				}
			}
			end.parent = current;
			node pathnode = end;
			while (pathnode != start) {
				path.add(pathnode);
				pathnode = pathnode.parent;
			}
			for (int i = path.size() - 1; i > -1; i--) {
				// System.out.println("CURRENT POSITION" + path.get(i));
				this.move(path.get(i));
			}
			// call function to deal with
			// certainty
		}

		// certain
		else {
			node current = start;
			closed.add(current);
			while (!current.neighbor.contains(end)) {// go travel
				for (int j = 0; j < current.neighbor.size(); j++) {// set f
																	// value and
																	// parent
																	// for
																	// neighbor
					// node
					node temp = current.neighbor.get(j);
					if (temp.f > current.g + 1 + temp.h
							&& !closed.contains(temp)
							&& !this.pingMap(temp).equals("X")) {
						// if node O, node value needs update, nodes not in the
						// closed list
						temp.g = current.g + 1;
						temp.f = temp.g + temp.h;
						if (!open.contains(temp)) {
							open.add(temp);
							// System.out.println("NOde" + temp +
							// "added to open");
							// System.out.println("OPENLIST SIZE" +
							// open.size());
						}
						temp.parent = current;
					}
				}
				// System.out.println("OPENLIST SIZE" + open.size());
				int min = Integer.MAX_VALUE;
				for (int k = 0; k < open.size(); k++) {// find minimum f
					if (open.get(k).f <= min) {
						min = open.get(k).f;
					}
				}
				// System.out.println("MIN F" + min);
				loop: for (int i = 0; i < open.size(); i++) {// add it to closed
																// list,
																// // remove
																// from open
					// list
					if (open.get(i).f == min) {
						closed.add(open.get(i));
						current = open.get(i);
						open.remove(i);
						break loop;
					}
				}
			}
			end.parent = current;
			node pathnode = end;
			while (pathnode != start) {
				path.add(pathnode);
				pathnode = pathnode.parent;
			}
			for (int i = path.size() - 1; i > -1; i--) {
				this.move(path.get(i));
			}
			// call function to deal with
			// certainty
		}
	}

	@Override
	public void addToWorld(World world) {
		isUncertain = world.getUncertain();
		super.addToWorld(world);
	}

	public static void main(String[] args) {
		try {
			String file = "myInputFile4.txt";
			World myWorld = new World(file, true);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int x = 0;
			while ((line = br.readLine()) != null) {
				String temp[] = line.split(" ");
				for (int i = 0; i < temp.length; i++) {
					node tempnode = new node();
					tempnode.setLocation(x, i);
					// tempnode.value = temp[i];
					if (temp[i].equals("S")) {
						tempnode.g = 0;
						tempnode.value = "S";
						start = tempnode;
					}
					if (temp[i].equals("F")) {
						tempnode.value = "F";
						end = tempnode;
					}
					wnode.add(tempnode);
				}
				x++;
			}
			int ex = myWorld.getEndPos().x;
			int ey = myWorld.getEndPos().y;
			for (int i = 0; i < wnode.size(); i++) {// set h value;
				int h = Math.abs(wnode.get(i).x - ex)
						+ Math.abs(wnode.get(i).y - ey);
				wnode.get(i).h = h;
			}
			for (int i = 0; i < wnode.size(); i++) {// set neighbor nodes
				for (int j = 0; j < wnode.size(); j++) {
					if ((Math.abs(wnode.get(i).x - wnode.get(j).x) == 1 || wnode
							.get(i).x == wnode.get(j).x)
							&& (Math.abs(wnode.get(i).y - wnode.get(j).y) == 1 || wnode
									.get(i).y == wnode.get(j).y)) {
						wnode.get(i).neighbor.add(wnode.get(j));
					}
				}
			}
			// System.out.println("NEIGBOR" + start.neighbor.toString());
			MyRobot robot = new MyRobot();

			robot.addToWorld(myWorld);
			// robot.move(new Point(0, 0));
			myWorld.createGUI(400, 400, 200); // uncomment this and create a
			// GUI; the last parameter is delay in msec
			robot.travelToDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
