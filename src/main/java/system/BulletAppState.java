package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;
import component.Direction;
import component.Model;
import component.Position;
import component.Speed;

public class BulletAppState extends AbstractAppState {

	private SimpleApplication app;
	private EntityData ed;
	private EntitySet bullets;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		this.app = (SimpleApplication) app;
		this.ed = this.app.getStateManager().getState(EntityDataState.class).getEntityData();

		bullets = ed.getEntities(Model.class
				, Position.class
				, Speed.class
				, Direction.class);
	}

	@Override
	public void update(float tpf) {
		bullets.applyChanges();
		bullets.stream().forEach((e) -> {
			Position position = e.get(Position.class);
			Direction direction = e.get(Direction.class);
			Speed speed = e.get(Speed.class);
			float distance = tpf * speed.getSpeed();
			Vector3f whereToMove = direction.getVectorNormalized().mult(distance);
			Position newPosition = new Position(position.getLocation().add(whereToMove), direction.getVectorNormalized());
			e.set(newPosition);
		});
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}
}