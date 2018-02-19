package org.usfirst.frc.team5015.robot;

import edu.wpi.first.wpilibj.Timer;

/**
 * 
 * A test auto mode which drives 1 foot forward.
 *
 */
public class Test1FootAuto extends Auto_Mode {

	public Test1FootAuto(String name, SWATDrive drive, Timer timer) {
		super(name, drive, timer);
	}
	
	@Override
	public void init_auto(){
		next_step = false;
		step_number = 0;
		auto_timer.reset();
		auto_timer.start();
		drive_system.driveGyro.reset();
		drive_system.distanceEncoder.reset();
	}
	
	@Override
	public boolean periodic_auto(){
		if(next_step){
			step_number += 1;
			drive_system.resetControllers();
			next_step = false;
			drive_system.driveGyro.reset();
		}

			switch(step_number)
				{
				case 0:
					// Expected distance = 12 inches
		            next_step = drive_system.gyroDistanceDrive(12.0, 0.45);
				break;
				 
				default:
					drive_system.stopDrive();
					return true;
				}
			return false;
	}

}
