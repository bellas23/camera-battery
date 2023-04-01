package hw1;

public class CameraBattery {

	// given constants
	/**
	 * Number of external charger settings.
	 */
	public static final int NUM_CHARGER_SETTINGS = 4;

	/**
	 * A constant used in calculating the charge rate of the external charger.
	 */
	public static final double CHARGE_RATE = 2.0;

	/**
	 * Default power consumption of the camera at the start of simulation.
	 */
	public static final double DEFAULT_CAMERA_POWER_CONSUMPTION = 1.0;

	// added variables
	/**
	 * The power consumption of the camera
	 */
	private double powerConsumption = DEFAULT_CAMERA_POWER_CONSUMPTION;

	/**
	 * The actual amount in (mAh) the battery connected to the external charger has
	 * been charged
	 */
	private double amountChargedExternal;

	/**
	 * The actual amount (in mAh) drained from the battery
	 */
	private double amountDrained;

	/**
	 * The current charger setting (number between 0 inclusive and
	 * NUM_CHARGER_SETTINGS which is 4)
	 */
	private int currentChargerSetting;

	/**
	 * The total amount of power drained from the battery
	 */
	private double totalDrain;

	/**
	 * The current charge of the battery
	 */
	private double currentBatteryCharge;

	/**
	 * Holds value of battery's capacity
	 */
	private double capacity;

	/**
	 * The current charge of the camera's battery
	 */
	private double currentCameraCharge;

	/**
	 * Indicates whether battery is connected or disconnected from the camera.
	 */
	private int cameraState;

	/**
	 * Indicates whether battery is connected or disconnected from the external
	 * charger.
	 */
	private int chargerState;

	// constructor
	/**
	 * Constructs a new camera battery simulation.
	 * 
	 * @param batteryStartingCharge the initial charge of the battery
	 * @param batteryCapacity       the capacity of the battery
	 */
	public CameraBattery(double batteryStartingCharge, double batteryCapacity) {
		currentBatteryCharge = Math.min(batteryStartingCharge, batteryCapacity);
		capacity = batteryCapacity;
	}

	// public methods
	/**
	 * Indicates the user has pressed the setting button one time on the external
	 * charger. Charge setting increments by one or if already at the maximum
	 * setting wraps around to setting 0.
	 */
	public void buttonPress() {
		currentChargerSetting = (currentChargerSetting + 1) % NUM_CHARGER_SETTINGS;
	}

	/**
	 * Charges the battery connected to the camera (assuming it is connected) for a
	 * given number of minutes.
	 * 
	 * @param minutes the amount of time the battery connected to the camera is
	 *                charged
	 * @return amountChargedCamera the actual amount the battery connected to the
	 *         camera has been charged
	 */
	public double cameraCharge(double minutes) {
		double initial = currentCameraCharge;
		double amountCharged = minutes * CHARGE_RATE * cameraState;
		currentCameraCharge += amountCharged;
		currentCameraCharge = Math.min(capacity, currentCameraCharge);
		currentBatteryCharge = Math.max(currentCameraCharge, currentBatteryCharge);
		amountCharged = currentCameraCharge - initial;
		return amountCharged;
	}

	/**
	 * Drains the battery connected to the camera (assuming it is connected) for a
	 * given number of minutes
	 * 
	 * @param minutes the amount of time the battery is drained
	 * @return amountDrained the actual amount drained from the battery
	 */
	public double drain(double minutes) {
		double lastAmountDrained = currentBatteryCharge;
		amountDrained = (minutes * powerConsumption * cameraState);
		currentBatteryCharge -= amountDrained;
		currentBatteryCharge = Math.max(currentBatteryCharge, 0);
		amountDrained = lastAmountDrained - currentBatteryCharge;
		currentCameraCharge = Math.min(currentCameraCharge, currentBatteryCharge);
		totalDrain += amountDrained;
		return amountDrained;
	}

	/**
	 * Charges the battery connected to the external charger (assuming it is
	 * connected) for a given number of minutes.
	 * 
	 * @param minutes the amount of time the battery connected to the external
	 *                charger is charged
	 * @return amountChargedExternal the actual amount the battery connected to the
	 *         external charger has been charged
	 */
	public double externalCharge(double minutes) {
		double initial = currentBatteryCharge;
		double amountCharged = (minutes * CHARGE_RATE * currentChargerSetting * chargerState);
		currentBatteryCharge += amountCharged;
		currentBatteryCharge = Math.min(currentBatteryCharge, capacity);
		return currentBatteryCharge - initial;
	}

	// accessors
	/**
	 * Get the battery's capacity.
	 * 
	 * @return batteryCapacity the capacity of the battery
	 */
	public double getBatteryCapacity() {
		return capacity;
	}

	/**
	 * Get the battery's current charge.
	 * 
	 * @return currentBatteryCharge the current charge of the battery
	 */
	public double getBatteryCharge() {
		return Math.max(currentBatteryCharge, 0);
	}

	/**
	 * Get the current charge of the camera's battery.
	 * 
	 * @return currentCameraCharge the current charge of the camera's battery
	 */
	public double getCameraCharge() {
		return Math.max(currentCameraCharge, 0);
	}

	/**
	 * Get the power consumption of the camera.
	 * 
	 * @return DEFAULT_CAMERA_POWER_CONSUMPTION the power consumption of the camera
	 *         at the start of simulation
	 */
	public double getCameraPowerConsumption() {
		return powerConsumption;
	}

	/**
	 * Get the external charger setting.
	 * 
	 * @return chargerSetting the setting of the external charger
	 */
	public int getChargerSetting() {
		return currentChargerSetting;
	}

	/**
	 * Get the total amount of power drained from the battery since the last time
	 * the battery monitor was started or reset.
	 * 
	 * @return totalDrain the total amount of power drained from the battery
	 */
	public double getTotalDrain() {
		return totalDrain;
	}

	// mutators
	/**
	 * Reset the battery monitoring system by setting the total battery drain count
	 * back to 0.
	 */
	public void resetBatteryMonitor() {
		totalDrain = 0;
	}

	/**
	 * Move the battery to the external charger. Updates any variables as needed to
	 * represent the move.
	 */
	public void moveBatteryExternal() {
		currentCameraCharge = 0;
		currentBatteryCharge += amountChargedExternal;
		cameraState = 0;
		chargerState = 1;
	}

	/**
	 * Move the battery to the camera. Updates any variables as needed to represent
	 * the move.
	 */
	public void moveBatteryCamera() {
		currentCameraCharge = currentBatteryCharge;
		cameraState = 1;
		chargerState = 0;
	}

	/**
	 * Remove the battery from either the camera or external charger. Updates any
	 * variables as needed to represent the removal.
	 */
	public void removeBattery() {
		currentCameraCharge = 0;
		cameraState = 0;
		chargerState = 0;
		powerConsumption = 0;
	}

	/**
	 * Set the power consumption of the camera.
	 * 
	 * @param cameraPowerConsumption the amount of power that the camera consumes
	 */
	public void setCameraPowerConsumption(double cameraPowerConsumption) {
		powerConsumption = cameraPowerConsumption;
	}

}
