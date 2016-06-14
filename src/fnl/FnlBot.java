/**
 * Copyright (c) 2001-2016 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package fnl;


import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;


/**
 * Target - a sample robot by Mathew Nelson.
 * <p/>
 * Sits still. Moves every time energy drops by 20.
 * This Robot demonstrates custom events.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class FnlBot extends AdvancedRobot {

	int trigger; // Keeps track of when to move

	public void run() {

	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		
	}
	
	public void onHitRobot(HitRobotEvent e) {
		
	}

}
