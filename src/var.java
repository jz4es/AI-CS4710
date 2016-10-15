public class var {
	String type = "L";
	String def = "";
	boolean truth = false;
	String name = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public boolean isTruth() {
		return truth;
	}

	public void setTruth(boolean truth) {
		this.truth = truth;
	}

	public void teach(String type, String name, String def) {
		this.type = type;
		this.name = name;
		this.def = def;
	}

}
