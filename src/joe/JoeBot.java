package joe;

import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.CustomEvent;

import java.awt.*;

public class JoeBot extends AdvancedRobot {

    int count = 0;
    double gunTurnAmt;
    double WIDTH, HEIGHT, MAX_DISTANCE;
    String trackName;
    boolean keepGoing = true;
    boolean leftRight = false;
    double driveDist = 100;
    int BEARING_ADJUSTMENT = 45;
    int FIRE_DIST = 200;

    public double getFirePower(ScannedRobotEvent enemy) {
        double firePower = Math.max((1 - (enemy.getDistance() / MAX_DISTANCE)) * 3, .1);
        System.out.println(firePower);
        return firePower;
    }

    public float time(double firePower, double enemyDistance) {

        double bulletSpeed = 20 - firePower * 3; // calculate speed of bullet
        long time = (long)(enemyDistance / bulletSpeed);
        return time;
    }

	public double aim(double enemyDistance, double enemyBearing, double enemyHeading, double enemySpeed, double time){
		double angle = 0;
		double myX = getX();
		double myY = getY();
		double dist = time * enemySpeed;

		double newEnemyY = enemyY + dist * Math.sin(enemyHeading);
		double newEnemyX = enemyX + dist * Math.cos(enemyHeading);

		double newAimY = newEnemyY - myY;
		double newAimX = newEnemyX - myX;

		angle = 90 - Math.atan(newAimX/newAimY);

		return angle;
	}

    public double getGunTurnAmt(ScannedRobotEvent e) {

        //  return normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));

        double angle = Math.toRadians(getHeadingRadians() + e.getBearingRadians() % 360);

        // Calculate the coordinates of the robot
        double enemyX = (getX() + Math.sin(angle) * e.getDistance());
        double enemyY = (getY() + Math.cos(angle) * e.getDistance());


    }

    public void run() {

        WIDTH = getBattleFieldWidth();
        HEIGHT = getBattleFieldHeight();
        MAX_DISTANCE = Math.sqrt(Math.pow(WIDTH, 2) + Math.pow(HEIGHT, 2));

        setBodyColor(new Color(0, 0, 0));
        setGunColor(new Color(0, 0, 0));
        setRadarColor(new Color(0, 0, 0));
        setScanColor(Color.white);
        setBulletColor(Color.blue);

        trackName = null; // Initialize to not tracking anyone
        setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
        gunTurnAmt = 10; // Initialize gunTurn to 10

        // Loop forever
        while (keepGoing) {

            // turn the Gun (looks for enemy)
            turnGunRight(gunTurnAmt);
            // Keep track of how long we've been looking
            count++;
            // If we've haven't seen our target for 2 turns, look left
            if (count > 2) {
                gunTurnAmt = -10;
            }
            // If we still haven't seen our target for 5 turns, look right
            if (count > 5) {
                gunTurnAmt = 10;
            }
            // If we *still* haven't seen our target after 10 turns, find another target
            if (count > 11) {
                trackName = null;
            }
        }
    }

    /**
     * onScannedRobot:  Here's the good stuff
     */
    public void onScannedRobot(ScannedRobotEvent e) {

        // If we don't have a target, well, now we do!
        if (trackName == null) {
            trackName = e.getName();
            out.println("Tracking " + trackName);
        }

        // This is our target.  Reset count (see the run method)
        count = 0;

        // If our target is too far away, turn and move toward it.
        if (e.getDistance() > 150) {
            gunTurnAmt = getGunTurnAmt(e);

            setTurnGunRight(gunTurnAmt);
            if (leftRight){
                BEARING_ADJUSTMENT = -BEARING_ADJUSTMENT;
            }
            leftRight = !leftRight;
            turnRight(e.getBearing() + BEARING_ADJUSTMENT);
            ahead(driveDist);
        }

        if (e.getDistance() < FIRE_DIST){
        	gunTurnAmt = getGunTurnAmt(e);
        	setTurnGunRight(gunTurnAmt);
        	fire(3);
        }

        // Our target is too close!  Back up.
        if (e.getDistance() < 100) {
            if (e.getBearing() > -90 && e.getBearing() <= 90) {
                back(150);
            } else {
                ahead(40);
            }
        }
        scan();
    }

    /**
     * onHitRobot:  Set him as our new target
     */
    public void onHitRobot(HitRobotEvent e) {
        // Only print if he's not already our target.
        if (trackName != null && !trackName.equals(e.getName())) {
            out.println("Tracking " + e.getName() + " due to collision");
        }
        // Set the target
        trackName = e.getName();
        // Back up a bit.
        // Note:  We won't get scan events while we're doing this!
        // An AdvancedRobot might use setBack(); execute();
        gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
        setTurnGunRight(gunTurnAmt);
        fire(3);
        back(50);
    }
}
