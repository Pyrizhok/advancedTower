package component;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;
import configurations.Constants;

public class Direction implements EntityComponent {

	private final Vector3f vectorNormalized;
	private final Constants.ON_GETTING_TO_STRATEGY onGettingToAction;
	private Vector3f pointToMoveTo;

	public Direction(Vector3f normalizedVector
			, Constants.ON_GETTING_TO_STRATEGY onGettingToAction
			, Vector3f pointToMoveToValue) {
		this.vectorNormalized = normalizedVector;
		this.onGettingToAction = onGettingToAction;
		this.pointToMoveTo = pointToMoveToValue;
	}

	public Constants.ON_GETTING_TO_STRATEGY getOnGettingToAction() {
		return onGettingToAction;
	}

	public Vector3f getVectorNormalized() {
		return vectorNormalized;
	}

	@Override
	public String toString() {
		return "Direction{" +
				"vectorNormalized=" + vectorNormalized +
				", onGettingToAction='" + onGettingToAction + '\'' +
				'}';
	}
}