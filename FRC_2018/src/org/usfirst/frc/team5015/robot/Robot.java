package org.usfirst.frc.team5015.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	//SendableChooser<Auto_Mode> auto_selector = new SendableChooser<>();	
	AnalogGyro drive_gyro = new AnalogGyro(0);
	Encoder drive_encoder = new Encoder(0, 1);
	Talon left_drive = new Talon(0); 
	Talon right_drive = new Talon(1);
	DifferentialDrive drivetrain = new DifferentialDrive(left_drive, right_drive);
	SWATDrive drive_system = new SWATDrive(drivetrain, drive_gyro, drive_encoder);
	
	Joystick drive_stick = new Joystick(0);
	Joystick gunner_stick = new Joystick(1);

	DigitalInput topLimitSwitch = new DigitalInput(9);
	DigitalInput bottomLimitSwitch = new DigitalInput(8);

	//Spark elevator_motor = new Spark(2);
	WPI_TalonSRX elevator_motor = new WPI_TalonSRX(1);
	Elevator elevatorSystem = new Elevator(0.75, 1.0, elevator_motor, bottomLimitSwitch, topLimitSwitch);
	
	VictorSP left_intake = new VictorSP(2);
	VictorSP right_intake = new VictorSP(3);
	
	
	
	/**   b
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {		
		drive_system.setMaxSpeed(0.75);
		
		CameraServer.getInstance().startAutomaticCapture();

		elevator_motor.configNominalOutputForward(0.0, 0);
		elevator_motor.configNominalOutputForward(0.0, 0);
		elevator_motor.configPeakOutputForward(+1.0, 0);
		elevator_motor.configPeakOutputReverse(-1.0, 0);
		
		drive_gyro.calibrate();
		
		// Configure the distance to be in inches
        drive_encoder.setDistancePerPulse(Math.PI*6.0/250.0);
        
        //auto_selector.addDefault(center_gear.get_name(), center_gear);
        //auto_selector.addObject(left_gear.get_name(), left_gear);
        //auto_selector.addObject(right_gear.get_name(), right_gear);
        //auto_selector.addObject(nothing_auto.get_name(), nothing_auto);
        
    	//SmartDashboard.putData("Auto Selector", auto_selector);
        
        left_intake.setInverted(true);
        
        elevator_motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
	}

	boolean next_step = false;
	int step_number = 0;
	Timer auto_timer = new Timer();
	Test1FootAuto test1Foot = new Test1FootAuto("Test 1 Foot", drive_system, left_intake, right_intake, elevatorSystem, auto_timer);
	TestTurn90Auto test90Turn = new TestTurn90Auto("Test Turn 90 Degrees",  drive_system, left_intake, right_intake, elevatorSystem, auto_timer);
	Auto_Mode nothing_auto = new Auto_Mode("Do Nothing", drive_system, left_intake, right_intake, elevatorSystem, auto_timer);
	Auto_Mode selected_auto;
	MiddleAuto middle_auto = new MiddleAuto("Middle Auto", drive_system, left_intake, right_intake, elevatorSystem, auto_timer);
	
	@Override
	public void autonomousInit() {
		String fieldConfig = DriverStation.getInstance().getGameSpecificMessage();
		
		elevator_motor.configPeakOutputForward(1.0, 0);
		elevator_motor.configPeakOutputReverse(-1.0, 0);
		test1Foot.init_auto(fieldConfig);
		test90Turn.init_auto(fieldConfig);
		middle_auto.init_auto(fieldConfig);
		SmartDashboard.putBoolean("Finished", false);
		//selected_auto = auto_selector.getSelected();
		//selected_auto.init_auto(fieldConfig);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		SmartDashboard.putNumber("Encoder", drive_system.distanceEncoder.getDistance());
		
		// Test auto modes
		//SmartDashboard.putBoolean("Finished", test1Foot.periodic_auto());
		//SmartDashboard.putBoolean("Finished", test90Turn.periodic_auto());
		
		SmartDashboard.putBoolean("Finished", middle_auto.periodic_auto());
		//SmartDashboard.putBoolean("Finished", left_gear.periodic_auto());
		//SmartDashboard.putBoolean("Finished", right_gear.periodic_auto());
		
		//SmartDashboard.putBoolean("Finished", selected_auto.periodic_auto());
	}
		
	public void teleopInit() {
		elevator_motor.setSelectedSensorPosition(0, 0, 10);
		drive_system.distanceEncoder.reset();
	}
	
	@Override
	public void teleopPeriodic() {
		SmartDashboard.putNumber("Elevator Encoder", elevator_motor.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Encoder", drive_system.distanceEncoder.getDistance());
		if(drive_stick.getRawButton(5))
		{
			//Fast button on right trigger.
			drive_system.setMaxSpeed(1.0);
		}
		else if(drive_stick.getRawButton(7)){
			//Slow button on right bumper.
			drive_system.setMaxSpeed(0.5);
		}
		else {
			//If neither slow nor fast button are pressed, go at normal speed.
			drive_system.setMaxSpeed(0.75);
		}

		drive_system.controlDrive(drive_stick.getRawAxis(1), drive_stick.getRawAxis(2)+0.2);
		
		SmartDashboard.putNumber("Climb power", gunner_stick.getRawAxis(1));
		elevatorSystem.driveElevator(gunner_stick.getRawAxis(1)*-1);

		//double intake_speed = gunner_stick.getRawAxis(3);
		//right_intake.set(intake_speed);
		//left_intake.set(intake_speed);
		
		if (gunner_stick.getRawButton(1))
		{
			right_intake.set(0.5);
			left_intake.set(0.5);
		}
		else if (gunner_stick.getRawButton(4))
		{
			right_intake.set(-0.5);
			left_intake.set(-0.5);
		}
		else {
			right_intake.set(0);
			left_intake.set(0);
		}


	}
	

	public void disabledInit()
	{	
    	//SmartDashboard.putData("Auto Selector", auto_selector);
	}
	
	public void disabledPeriodic()
	{
    	//SmartDashboard.putData("Auto Selector", auto_selector);
		//selected_auto = auto_selector.getSelected();
		//SmartDashboard.putString("Selected Auto", selected_auto.get_name());
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

