## Strategy considerations

* How far away are we from the other bot? Should our movement be dependent on distance?
 * Far away move from side to side
 * Up close move in a sinusoidal pattern

* Detecting other bot
 * Is it possible to track last five positions and fit a curve to predict next position based on angular velocity?

* Firing
 * Getting up close makes accuracy less important

* Good bot strategies from samples bots
 * SpinBot
 * TrackerBot
 * WallBot


Functions:
 * Firing rate function (distance to opponent)
 	* Returns float b/w 1-3 
 * Predict where opponent is
 * Movement function