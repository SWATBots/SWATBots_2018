package org.usfirst.frc.team5015.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

public class Auto_Mode {
	private String auto_name;
	public SWATDrive drive_system;
	public Timer auto_timer;
	public boolean next_step = false;
	public int step_number = 0;
	public VictorSP left_intake;
	public VictorSP right_intake;
	public Elevator elevator_system;
	
	public Auto_Mode(String name, SWATDrive drive, VictorSP leftintake_input, VictorSP rightintake_input, Elevator elevator_input, Timer timer){
		auto_name = name;
		drive_system = drive;
		auto_timer = timer;
		left_intake = leftintake_input;
		right_intake = rightintake_input;
		elevator_system = elevator_input;
	}
	
	public String get_name(){
		return auto_name;
	}
	
	public void init_auto(String fieldLayout){
	}
	
	public boolean periodic_auto(){
		return true;
	}
}
