package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogInput;

import java.lang.System;

//the A button is

public class Robot extends TimedRobot{
	private static final int A_BUTTON = 1;
	private static final int B_BUTTON = 2;
	private static final int X_BUTTON = 3;
	private static final int Y_BUTTON = 4;
	private static final int LEFT_BUMPER = 5;
	private static final int RIGHT_BUMPER = 6;
	private static final int LEFT_JOYSTICK_HORIZONTAL = 0;
	private static final int LEFT_JOYSTICK_VERTICAL = 1;
	private static final int LEFT_TRIGGER = 2;
	private static final int RIGHT_TRIGGER = 3;
	private static final int RIGHT_JOYSTICK_HORIZONTAL = 4;
	private static final int RIGHT_JOYSTICK_VERTICAL = 5;

	private Joystick stick1 = new Joystick(0);
	private Joystick stick2 = new Joystick(1);
	PWMVictorSPX intake = new PWMVictorSPX(5);
	// not changing -- pwm port 5
	PWMVictorSPX tower = new PWMVictorSPX(4);
	//not changing -- pwm port 4
	PWMVictorSPX hopper = new PWMVictorSPX(2);
	// not changing -- pwm port 2
	  CANSparkMax shooter = new CANSparkMax(1, MotorType.kBrushless);
	//  spark max port 1
	CANSparkMax frontLeft = new CANSparkMax(8, MotorType.kBrushless);
	// spark max port 8
	CANSparkMax rearLeft = new CANSparkMax(9, MotorType.kBrushless);
	//spark max port 9
	CANSparkMax frontRight = new CANSparkMax(6, MotorType.kBrushless);
	//spark max port 6
	CANSparkMax rearRight = new CANSparkMax(7, MotorType.kBrushless);
	//spark max port 7
	PWMVictorSPX climb = new PWMVictorSPX(0);
	//not changing -- port 0
	CANSparkMax shooterLeft = new CANSparkMax(3, MotorType.kBrushless);
	// spark max port 3
	MecanumDrive m_drive = new MecanumDrive(frontLeft,rearLeft,frontRight,rearRight);
	Encoder leftenc;
	Encoder rightenc;
	boolean firstTime;
	//if controls are flipped, 1
	int flipped = -1;

	AnalogInput testinput = new AnalogInput(0);
	

  @Override
  public void robotInit(){
	  leftenc = new Encoder(0,1);
	  rightenc = new Encoder(2,3);
      firstTime = true;
  }

  @Override
  public void autonomousPeriodic(){
    if(firstTime == true){
        m_drive.setSafetyEnabled(false);
	    m_drive.driveCartesian(0,0.5,0,0);
	    Timer.delay(1.2);
	    m_drive.driveCartesian(0,0,-.25,0);
	    Timer.delay(.2);
	    m_drive.driveCartesian(0,0,0,0);
	    shooter.set(.6);
	    Timer.delay(1.2);
	    tower.set(.3);
	    hopper.set(.3);
	    Timer.delay(5);
	    shooter.set(0);
	    tower.set(0);
	    hopper.set(0);
	    m_drive.setSafetyEnabled(true);
	    firstTime = false;
    }
  }

  @Override
  public void teleopPeriodic(){
//inverse sine scaling for acceleration
	if (stick1.getRawButton(B_BUTTON)){
		flipped = -flipped;
	}

	  double x = stick1.getRawAxis(LEFT_JOYSTICK_HORIZONTAL) * -1;
	  double y = stick1.getRawAxis(LEFT_JOYSTICK_VERTICAL);
	  //right trigger rotates clockwise, left trigger rotates counterclockwise. if both are depressed, no rotation
	  double z = (stick1.getRawAxis(LEFT_TRIGGER) > 0 && stick1.getRawAxis(RIGHT_TRIGGER) > 0) ? 0.0 : stick1.getRawAxis(LEFT_TRIGGER) * .5 + stick1.getRawAxis(RIGHT_TRIGGER) * -.5;

	  m_drive.driveCartesian(x, y, z, 0.0);

	  if (stick1.getRawButton(LEFT_BUMPER)){
		  intake.set(.5);
	  }
	  else if (stick1.getRawButton(RIGHT_BUMPER)){
		  intake.set(-.5);
	  }
	  else{
		  intake.set(0);
	  }

	  if (stick2.getRawButton(LEFT_BUMPER)){
		  tower.set(.3);
	  }
	  else if (stick2.getRawAxis(LEFT_TRIGGER) > 0){
		  tower.set(-.3);
	  }
	  else{
		  tower.set(0);
	  }

    if (stick2.getRawButton(RIGHT_BUMPER)) {
		  shooter.set(-.55);
		  shooterLeft.set(.55);
	  }
	  else if (stick2.getRawAxis(RIGHT_TRIGGER) > 0) {
			shooter.set(.55);
			shooterLeft.set(-.55);
	  }
	  else {
		  shooter.set(0);
		  shooterLeft.set(0);
	  }

	  if (stick2.getRawButton(A_BUTTON)){
		  hopper.set(.5);
	  }
	  else if (stick2.getRawButton(X_BUTTON)){
		  hopper.set(-.5);
	  }
	  else{
	      hopper.set(0);
	  }

	  System.out.println(testinput.getValue());

	//   if (stick2.getRawButton(2)){
	// 	  climb.set(.7);
	//   }
	//   else if (stick2.getRawButton(4)){
	// 	  climb.set(-.7);
	//   }
	//   else{
	// 	  climb.set(0);
	//   }
  }
}
