package org.usfirst.frc.team5015.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * 
 *
 */
public class MiddleAuto extends Auto_Mode {

	public MiddleAuto(String name, SWATDrive drive, VictorSP leftintake_input, VictorSP rightintake_input, Elevator elevatormotor_input, Timer timer) {
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
		this.elevator_system.elevatorMotor.setSelectedSensorPosition(0, 0, 10);
	}
	
	@Override
	public boolean periodic_auto(){
		if(next_step){
			step_number += 1;
			drive_system.resetControllers();
			next_step = false;
			drive_system.driveGyro.reset();
			elevator_system.driveElevator(0);
		}

			switch(step_number)
				{
				case 0:
					elevator_system.driveElevator(0.8);
					left_intake.set(0.5);
					right_intake.set(0.5);
					next_step = elevator_system.elevatorMotor.getSelectedSensorPosition(0) > 3000;
				break;
				
				//case 1:
				//	next_step = drive_system.gyroDistanceDrive(105);
				//break;
				 
				default:
					left_intake.set(0);
					right_intake.set(0);
					drive_system.stopDrive();
					return true;
				}
			return false;
	}

}
