package configurations;

public class Constants {

	public static final Integer BULLET_ATTACK_POWER = 1;
	public static final Integer BULLET_COLLISION_SHAPE = 1;
	public static final Integer BULLET_SPEED = 20;
	public static final Integer BULLET_DECAY_DELTA_MILLIS = 20000;

	public enum ON_GETTING_TO_STRATEGY {
		CONTINUE_MOVEMENT_TO
		, STOP_AND_WAIT_FOR
	}
}
