package component;

import com.simsilica.es.EntityComponent;

public class Model implements EntityComponent {
	private final String name;
	public final static String DEFENDER = "DEFENDER";
	public final static String INVADER = "INVADER";
	public final static String BULLET = "Bullet";
	public final static String CURSOR = "CURSOR";
	public final static String AREA = "AREA";


	public Model(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Model[" + name + "]";
	}
}