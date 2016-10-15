import java.util.ArrayList;
import java.util.Stack;

public class rule {
	String content;
	ArrayList<var> known;
	var inferred;
	String conditionst;

	public rule(String content, ArrayList<var> x) {
		super();
		this.content = content;
		this.known = x;
		String y = content.split(" ")[3];
		this.conditionst = content.split(" ")[1]; // bracket
													// condition
		for (int i = 0; i < known.size(); i++) {
			if (known.get(i).name.equals(y)) {
				this.inferred = known.get(i);
				break;
			}
		}

	}

	public rule(ArrayList<var> x, String condition) {
		this.conditionst = condition;
		this.known = x;
	}

	public static boolean isLetter(char c) {
		return Character.isLetter(c) || c == '_';
	}

	/*
	 * public boolean fulfill() throws ScriptException { String temp =
	 * conditionst; char[] tokens = temp.toCharArray(); ArrayList<var> condition
	 * = new ArrayList<>(); for (int i = 0; i < tokens.length; i++) { // Current
	 * token is a whitespace, skip it // Current token is a number, push it to
	 * stack for numbers if (isLetter(tokens[i])) { StringBuffer sbuf = new
	 * StringBuffer(); // There may be more than one digits in number while (i <
	 * tokens.length && isLetter(tokens[i])) { sbuf.append(tokens[i++]);// find
	 * the variable in content loop: for (int i1 = 0; i1 < known.size(); i1++) {
	 * if (known.get(i1).name.equals(sbuf.toString())) { temp =
	 * temp.replaceFirst(sbuf.toString(), String.valueOf(known.get(i1).truth));
	 * break loop; } }
	 * 
	 * } } } temp = temp.replaceAll("&", "&&"); Object x = new
	 * ScriptEngineManager().getEngineByName("javascript") .eval(temp); if
	 * (x.equals(1)) return true; if (x.equals(0)) return false;
	 * 
	 * return (Boolean) new ScriptEngineManager()
	 * .getEngineByName("javascript").eval(temp);
	 * 
	 * }
	 */

	public boolean stack() {
		String temp = conditionst;
		Stack<Boolean> value = new Stack<Boolean>();
		Stack<Character> oper = new Stack<Character>();
		char[] tokens = temp.toCharArray();
		for (int i = 0; i < tokens.length; i++) {
			// System.out.println("OPER: " + oper.toString());
			// System.out.println("VALUE: " + value.toString());
			// System.out.println("                 ");
			if (tokens[i] == '(') {
				oper.push('(');
			}
			if (tokens[i] == ')') {
				// System.out.println("In the bracket");
				while (oper.peek() != '(') {
					// System.out.println("BRACKETOPER: " + oper.toString());
					// System.out.println("BRACKETVALUE: " + value.toString());
					// System.out.println("                 ");
					char tempchar = oper.pop();
					if (tempchar == '!') {
						value.push(!value.pop());
						// System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
					} else {
						boolean tempx = value.pop();
						boolean tempy = value.pop();
						if (tempchar == '&') {
							value.push(tempx && tempy);
							// System.out.println("&&&&&&&&&&&&&&&&&&&&&&");
						}
						if (tempchar == '|') {
							value.push(tempx || tempy);
						}
					}
				}
				oper.pop();// popping (
				if (!oper.isEmpty() && oper.peek() == '!') {
					// System.out.println("??????????????????/");
					value.push(!value.pop());
					oper.pop();
				}
			}

			if (tokens[i] == '&') {
				if (!oper.isEmpty() && (oper.peek() == '&')) {
					boolean temp1 = value.pop();
					boolean temp2 = value.pop();
					oper.pop();
					value.push(temp1 && temp2);
				}
				oper.push('&');
				// System.out.print(1);

			}

			if (tokens[i] == '!') {
				oper.push('!');
			}

			if (tokens[i] == '|') {
				if (!oper.isEmpty() && oper.peek() == '&') {
					boolean temp1 = value.pop();
					boolean temp2 = value.pop();
					value.push(temp1 && temp2);
					oper.pop();
				}
				if (!oper.isEmpty() && oper.peek() == '|') {
					boolean temp1 = value.pop();
					boolean temp2 = value.pop();
					oper.pop();
					value.push(temp1 || temp2);
				}
				oper.push('|');
			}

			if (isLetter(tokens[i])) {
				StringBuffer sbuf = new StringBuffer();
				// There may be more than one digits in number
				while (i < tokens.length && isLetter(tokens[i])) {
					sbuf.append(tokens[i++]);
					// find the variable in content
				}
				i--;
				boolean tempb = true;
				loop: for (int i1 = 0; i1 < known.size(); i1++) {
					if (known.get(i1).name.equals(sbuf.toString())) {
						tempb = known.get(i1).truth;
						break loop;
					}
				}
				if (!oper.isEmpty() && oper.peek() == '!') {
					oper.pop();
					value.push(!tempb);
				}// top element
				else {
					value.push(tempb);
				}
			}
		}
		// System.out.println("OOOOONO" + oper.peek());
		// System.out.println("FINAL OPER:" + oper.toString());
		// System.out.println("FINAL VALUE:" + value.toString());
		loop2: while (oper.size() > 0) {
			// System.out.println("END OPER:" + oper.toString());
			// System.out.println("END VALUE:" + value.toString());
			char tempchar = oper.pop();
			if (tempchar == '!') {
				value.push(!value.pop());
				break loop2;
			}
			boolean tempx = value.pop();
			boolean tempy = value.pop();
			if (tempchar == '&') {
				value.push(tempx && tempy);
			}
			if (tempchar == '|') {
				value.push(tempx || tempy);
			}

		}
		return value.pop();
	}

}