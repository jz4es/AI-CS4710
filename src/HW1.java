import java.util.ArrayList;
import java.util.Scanner;

//fixed teach _L; fixed Learn
//only problem: operands order
import javax.script.ScriptException;

public class HW1 {
	static ArrayList<var> variable = new ArrayList<var>();
	static ArrayList<String> varname = new ArrayList<String>();
	static ArrayList<rule> rule = new ArrayList<rule>();
	static ArrayList<var> fact = new ArrayList<var>();

	public static boolean isLetter(char c) {
		return Character.isLetter(c) || c == '_';
	}

	public static String replacestring(String x) {
		String temp = x;
		char[] tokens = temp.toCharArray();
		ArrayList<var> condition = new ArrayList<>();
		for (int i = 0; i < tokens.length; i++) {
			if (isLetter(tokens[i])) {
				StringBuffer sbuf = new StringBuffer();
				// There may be more than one digits in number
				while (i < tokens.length && isLetter(tokens[i])) {
					sbuf.append(tokens[i++]);// find the variable in content
					loop: for (int i1 = 0; i1 < variable.size(); i1++) {
						if (variable.get(i1).name.equals(sbuf.toString())) {
							temp = temp.replaceFirst(sbuf.toString(),
									String.valueOf(variable.get(i1).def));
							break loop;
						}
					}

				}
			}
		}
		temp = temp.replace("|", " OR ");
		temp = temp.replaceAll("!", " NOT ");
		temp = temp.replaceAll("&", " AND ");
		return temp;

	}

	// (!A&B)
	public static void whytrue(String condition) throws ScriptException {
		String temp = condition;
		// System.out.println(replacestring(condition));
		char[] tokens = temp.toCharArray();
		for (int i = 0; i < tokens.length; i++) {
			if (isLetter(tokens[i])) {
				StringBuffer sbuf = new StringBuffer();
				while (i < tokens.length && isLetter(tokens[i])) {
					sbuf.append(tokens[i++]);
				}
				// find the variable in content
				loop: for (int i1 = 0; i1 < variable.size(); i1++) {
					if (variable.get(i1).name.equals(sbuf.toString())
							&& variable.get(i1).type.equals("R")) {
						if (variable.get(i1).truth) {
							System.out.println("I KNOW THAT "
									+ variable.get(i1).def);
						} else
							System.out.println("I KNOW IT IS NOT TRUE THAT "
									+ variable.get(i1).def);
						break loop;
					} else if (variable.get(i1).name.equals(sbuf.toString())) {
						boolean canprove = false;
						loop5: for (int j = 0; j < rule.size(); j++) {
							String tempcon = rule.get(j).conditionst;
							// System.out.println("TEMPCON:" + tempcon);
							if (rule.get(j).inferred.name.equals(variable
									.get(i1).name) && query(tempcon)) {// if
																		// there's
																		// a
																		// true
																		// condition
																		// lead
																		// to
																		// variable
								// System.out.println("1");
								for (int k = 0; k < variable.size(); k++) {
									if (variable.get(k).name.equals(tempcon)// if
																			// this
																			// condition
																			// is
																			// only
																			// root
											&& variable.get(k).type.equals("R")) {
										System.out.println("BECAUSE "
												+ variable.get(k).def // recursive?
												+ " I KNOW THAT "
												+ variable.get(i1).def);
										break loop;
									}
									whytrue(tempcon);// if this condition is not
														// root
								}
								break loop5;
							}
						}
						if (!canprove) {
							loop6: // if cannot prove, find a false condition
							for (int j = 0; j < rule.size(); j++) {
								String tempcon = rule.get(j).conditionst;
								// System.out.println("TEMPCON:" + tempcon);
								if (rule.get(j).inferred.name.equals(variable
										.get(i1).name) && !query(tempcon)) {// if
																			// there's
																			// a
																			// false
																			// condition
																			// lead
																			// to
																			// variable
									// System.out.println("1");
									for (int k = 0; k < variable.size(); k++) {
										if (variable.get(k).name
												.equals(tempcon)// if this
																// condition is
																// only root
												&& variable.get(k).type
														.equals("R")) {
											System.out
													.println("BECAUSE IT IS NOT TRUE THAT "
															+ variable.get(k).def // recursive?
															+ " I CANNOT PROVE THAT "
															+ variable.get(i1).def);
											break loop;
										}
										whytrue(tempcon);// if this condition is
															// not root
									}
									break loop6;
								}
							}
						}

					}
				}
			}
		}
		if (query(condition)) {
			System.out.println("I THUS KNOW THAT " + replacestring(condition));
		} else
			System.out.println("THUS I CANNOT PROVE "
					+ replacestring(condition));
	}

	public static boolean query(String condition) throws ScriptException {
		ArrayList<var> tempvar = variable;
		boolean found = true;
		while (found) {
			found = false;
			for (int i = 0; i < rule.size(); i++) {// for all the rules
				rule.get(i).known = tempvar;
				if (rule.get(i).stack()) {
					for (int j = 0; j < tempvar.size(); j++) { // update
																// variable
																// and fact
																// boolean
						if (rule.get(i).inferred.name
								.equals(tempvar.get(j).name)) {
							tempvar.get(j).truth = true;
						}
					}
				}

			}

		}

		rule test = new rule(tempvar, condition);
		boolean x = test.stack();
		return x;
	}

	public static void learn() throws ScriptException {
		boolean found = true;
		while (found) {
			found = false;
			for (int i = 0; i < rule.size(); i++) {// for all the rules
				rule.get(i).known = variable;// update fact for that rule
				if (rule.get(i).stack()) {
					for (int j = 0; j < variable.size(); j++) { // update
																// variable
																// and fact
																// boolean
						if (rule.get(i).inferred.name
								.equals(variable.get(j).name)) {
							variable.get(j).truth = true;
							boolean in = true;
							for (int k = 0; k < fact.size(); k++) {
								if (fact.get(k).name
										.equals(variable.get(j).name)) {
									in = false;
								}
							}
							if (in) {

								fact.add(variable.get(j));
								found = true;
							}
						}
					}
				}

			}

		}
	}

	public static void list() {
		System.out.println("Root Variables:");
		for (int i = 0; i < variable.size(); i++) {
			if (variable.get(i).type.equals("R")) {
				System.out.println("	" + variable.get(i).name + " = \""
						+ variable.get(i).def + "\"");
			}
		}
		System.out.println("\nLearned Variables:");
		for (int i = 0; i < variable.size(); i++) {
			if (variable.get(i).type.equals("L")) {
				System.out.println("	" + variable.get(i).name + " = \""
						+ variable.get(i).def + "\"");
			}
		}
		System.out.println("\nFacts:");
		for (int i = 0; i < fact.size(); i++)
			System.out.println("	" + fact.get(i).name);
		System.out.println("\nRules:");
		for (int i = 0; i < rule.size(); i++) {
			System.out.println("	" + rule.get(i).content);
		}
	}

	public static void main(String[] args) throws ScriptException {
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();

		while (input != null) {
			String[] x = input.split(" ");
			if (input.contains("Teach -R") || input.contains("Teach -L")) { // Teach
																			// -R
																			// S
																			// =
																			// SamLikesMe
				String def = x[4].substring(1, x[4].length() - 1);
				String type = x[1].substring(1, 2);
				String name = x[2];
				if (varname.contains(name)) {
				} else {
					varname.add(name);
					var temp = new var();
					temp.teach(type, name, def);
					variable.add(temp);
				}

			}

			else if (x.length == 4
					&& (x[3].equals("true") || x[3].equals("false"))) { // Teach
																		// S =//
																		// true
				String name1 = x[1];
				var temp = new var();
				loop: if (varname.contains(name1)) {
					for (int i = 0; i < variable.size(); i++) {
						if (variable.get(i).name.equals(name1)) {
							if (variable.get(i).type.equals("L")) { // could
																	// only set
																	// root
																	// variable
								break loop;
							} else {

								temp = variable.get(i);
								for (int j = 0; j < variable.size(); j++) {
									if (variable.get(j).type.equals("L")) {
										variable.get(j).truth = false; // reset
																		// all
																		// learned
																		// variables'
																		// truth
									}
								}
								if (x[3].equals("true")) {
									variable.get(i).truth = true;
								} else
									variable.get(i).truth = false;
							}
						}
					}
					fact.clear();
					for (int k = 0; k < variable.size(); k++) {
						if (variable.get(k).truth)
							fact.add(variable.get(k));
					}

				}
			}

			else if (x.length == 4 && x[2].equals("->")) {
				rule temp = new rule(input, variable);
				rule.add(temp);
			}
			if (input.equals("List")) {
				list();
			}
			if (input.equals("Learn")) {
				learn();
			}
			if (input.length() > 5 && input.substring(0, 5).equals("Query")) {
				String condition = input.substring(5, input.length());
				System.out.println(query(condition));
			}
			if (input.length() > 3 && input.substring(0, 3).equals("Why")) {
				String condition = input.substring(3, input.length());
				System.out.println("> Why " + condition);
				Boolean tempboo = query(condition);
				System.out.println(tempboo);
				whytrue(condition);
				/*
				 * if(tempboo){ whytrue(condition); } else whyfalse(condition);
				 */
			}
			input = scanner.nextLine();// keep reading input
		}
	}

}