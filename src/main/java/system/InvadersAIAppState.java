package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.simsilica.es.*;
import component.*;
import configurations.Constants;

import java.util.Optional;

public class InvadersAIAppState extends AbstractAppState {

	public static final String FIELD_NAME = "name";
	private SimpleApplication app;
	private EntityData ed;
	private EntitySet invaders;
	private EntitySet defenders;
	private float xDir;
	private float yDir;
	private int numberOfInvaders;

	private static float INVADER_SHOOT_RELOAD_TIMEOUT = 5f;
	public float timePassedFromShoot = 0f;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		this.app = (SimpleApplication) app;
		this.ed = this.app.getStateManager().getState(EntityDataState.class).getEntityData();

		invaders = ed.getEntities(
				Filters.fieldEquals(Model.class, FIELD_NAME, Model.INVADER),
				Model.class,
				Position.class);

		defenders = ed.getEntities(
				Filters.fieldEquals(Model.class, FIELD_NAME, Model.DEFENDER),
				Model.class,
				Position.class);
		xDir = 1f;
		yDir = -1f;
	}

	public int getNumberOfInvaders() {
		return numberOfInvaders;
	}

	@Override
	public void update(float tpf) {
		invaders.applyChanges();
		defenders.applyChanges();
		wabbeling(tpf);
		timePassedFromShoot += tpf;
		if ((timePassedFromShoot += tpf) > INVADER_SHOOT_RELOAD_TIMEOUT) {
			timePassedFromShoot -= INVADER_SHOOT_RELOAD_TIMEOUT;
			groupShoot(tpf);
		}
		numberOfInvaders = invaders.size();
	}

	private void groupShoot(float tpf) {
		for (Entity e : invaders) {
			Vector3f bulletStartPosition = e.get(Position.class).getLocation();
			Optional<Entity> entity = defenders.stream().findAny();
			if (!entity.isPresent()) {
				continue;
			}
			EntityId bullet = ed.createEntity();
			Vector3f defenderPosition = entity.get().get(Position.class).getLocation();
			Vector3f directionVectorNormalized = defenderPosition.subtract(bulletStartPosition).normalize();
			Vector3f newVector3f = bulletStartPosition.add(directionVectorNormalized.mult(3f));
			ed.setComponents(bullet,
					new Model(Model.BULLET),
					new Attack(Constants.BULLET_ATTACK_POWER),
					new CollisionShape(Constants.BULLET_COLLISION_SHAPE),
					new Position(newVector3f, directionVectorNormalized),
					new Direction(directionVectorNormalized, Constants.ON_GETTING_TO_STRATEGY.CONTINUE_MOVEMENT_TO, null),
					new Speed(Constants.BULLET_SPEED),
					new Decay(Constants.BULLET_DECAY_DELTA_MILLIS));
		}
	}

	private void wabbeling(float tpf) {
		float xMin = 0;
		float xMax = 0;
		float yMin = 0;
		float yMax = 0;

		for (Entity e : invaders) {
			Vector3f location = e.get(Position.class).getLocation();
			if (location.getX() < xMin) {
				xMin = location.getX();
			}
			if (location.getX() > xMax) {
				xMax = location.getX();
			}
			if (location.getY() < yMin) {
				yMin = location.getY();
			}
			if (location.getY() > yMax) {
				yMax = location.getY();
			}
			e.set(new Position(location.add(xDir * tpf * 2, yDir * tpf * 0.5f, 0), new Vector3f()));
		}
		if (xMax > 22) {
			xDir = -1;
		}
		if (xMin < -22) {
			xDir = 1;
		}
		if (yMax > 20) {
			yDir = -1;
		}
		if (yMin < 0) {
			yDir = 1;
		}
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}
}