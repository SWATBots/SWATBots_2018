package org.usfirst.frc.team5015.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * 
 * A test auto mode which turns 90 degrees clockwise.
 *
 */
public class TestTurn90Auto extends Auto_Mode {

	public TestTurn90Auto(String name, SWATDrive drive, VictorSP leftintake_input, VictorSP rightintake_input, Elevator elevatormotor_input, Timer timer) {
		super(name, drive, leftintake_input, rightintake_input, elevatormotor_input, timer);
	}
	
	@Override
	public void init_auto(String fieldLayout){
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
			auto_timer.reset();
		}

			switch(step_number)
				{
				case 0:
					// Expected turn 90 degrees
		            next_step = drive_system.gyroTurn(90, 0.40);
				break;
				 
				default:
					drive_system.stopDrive();
					return true;
				}
			return false;
	}

}
