package component;

import com.simsilica.es.EntityComponent;

public class SelectComponent implements EntityComponent {

	public String name;

	public SelectComponent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "SelectComponent[" + name + "]";
	}

}
