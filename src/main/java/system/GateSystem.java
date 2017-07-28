package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import component.CollisionShape;
import component.Defense;
import component.Model;
import component.Position;

import static system.GameAppState.BOUND;
import static system.GameAppState.DEFENCE_INVADER;
import static system.GameAppState.MAX_NUMBER_OF_INVADERS;

public class GateSystem extends BaseAppState {

	private EntityData ed;
	private SimpleApplication app;
	private Integer halfOfBound;
	private Vector3f gateCoordinates;
	private Integer day;

	private static float INVADER_CREATION_TIMEOUT = 5f;
	public float timePassedFromCreation = 0f;


	@Override
	protected void initialize(Application app) {
		this.app = (SimpleApplication) app;
		this.app.setPauseOnLostFocus(true);
		this.app.setDisplayStatView(false);
		halfOfBound = BOUND / 2;
		this.ed = this.app.getStateManager().getState(EntityDataState.class).getEntityData();
		defineGates();
	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {

	}

	@Override
	public void update(float tpf) {
		int numberOfInvaders = getState(InvadersAIAppState.class).getNumberOfInvaders();

		timePassedFromCreation += tpf;
		if (shouldInvaderBeCreated(tpf, numberOfInvaders)) {
			timePassedFromCreation-=INVADER_CREATION_TIMEOUT;
			EntityId invader = ed.createEntity();

			Vector3f gatePosition = gateCoordinates;
			this.ed.setComponents(invader,
					new Defense(DEFENCE_INVADER),
					new CollisionShape(1f),
					new Position(gatePosition, new Vector3f()),
					new Model(Model.INVADER));
		}
	}

	private boolean shouldInvaderBeCreated(float tpf, int numberOfInvaders) {
		return numberOfInvaders < MAX_NUMBER_OF_INVADERS &&
				(timePassedFromCreation += tpf) > INVADER_CREATION_TIMEOUT;
	}

	private void defineGates() {
		EntityId gate = ed.createEntity();
		day = 1;
		gateCoordinates = getGateCoordinates(day);
		this.ed.setComponents(gate,
				new Position(gateCoordinates, new Vector3f()),
				new Model(Model.GATE));
	}

	private Vector3f getGateCoordinates(Integer day) {
		return new Vector3f(10, 10, 0);
	}

}
