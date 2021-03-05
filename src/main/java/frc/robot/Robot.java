package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.SendableBase;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import java.lang.System;
import java.lang.Math;

//the A button is


public class Robot extends TimedRobot{
	AHRS ahrs = new AHRS (SPI.Port.kMXP);
	PIDController turnController;
	double rotatetoAngleRate;
	static final double kP = 0.0f;
	static final double kI =  0.0f;
	static final double kD =  0.0f;
	static final double kF =  0.0f;
	static final double kToleranceDegrees = 0.0f;
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
	
	void testingDynamicCorrection(float time){
		ahrs.reset();
		for(float x=0;x<time;x+=.01){
			System.out.println(ahrs.getAngle());
			if(Math.abs(ahrs.getAngle())>0){
				m_drive.driveCartesian(.25, 0, -ahrs.getAngle()/50);
				
				// m_drive.driveCartesian(.25, 0, 0);
			}
			else{
				m_drive.driveCartesian(.25, 0, 0);
			}
			Timer.delay(.01);
		}
	}

  @Override
  public void robotInit(){
	  leftenc = new Encoder(0,1);
	  rightenc = new Encoder(2,3);
      firstTime = true;
  }

  @Override
  public void autonomousPeriodic(){
	  boolean isRed = false;

		
    if(firstTime == true){
		if (isRed == true){
			//run Red course
		}
		else{
			//run Blue course
		}
		
		m_drive.setSafetyEnabled(false);

		// //Gree
	
		}

		ahrs.reset();
		// testingDynamicCorrection(7);
		// // ahrs.reset();
		// // m_drive.driveCartesian(.25, 0, 0, ahrs.getAngle());
		// // Timer.delay(7);
		// m_drive.driveCartesian(0, 0, 0);
// m_drive.driveCartesian(.25, 0, 0);
// Timer.delay(7);
// m_drive.driveCartesian(0, 0, 0);
// Timer.delay(0);

		m_drive.setSafetyEnabled(false);
		ahrs.reset();
		//Slalom auto
		m_drive.setSafetyEnabled(false);
		m_drive.driveCartesian(0, .25, 0);
		Timer.delay(.7);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		// m_drive.driveCartesian(0, 0, -.45);
		// Timer.delay(.47);
		m_drive.driveCartesian(-.25, 0, 0);
		Timer.delay(2);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.1);
		m_drive.driveCartesian(0, 0, .45);
		Timer.delay(Math.abs(ahrs.getAngle()/200.0f));
		m_drive.driveCartesian(0, .25, 0);
		Timer.delay(3.5);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		//Turns
		// m_drive.driveCartesian(0, 0, .45);
		// Timer.delay(.44);
		// m_drive.driveCartesian(0, .25, 0);
		// Timer.delay(3);
		// m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(2);
		// m_drive.driveCartesian(0, -.25, 0);
		// Timer.delay(.1);
		// m_drive.driveCartesian(0,0,0);
		// Timer.delay(1);
		// //m_drive.driveCartesian(0, 0, .45);
		// //Timer.delay(.45);
		 testingDynamicCorrection(1.8f);
		 m_drive.driveCartesian(0, 0, 0);
		 Timer.delay(.1);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		m_drive.driveCartesian(0, .25, 0);
		Timer.delay(.75);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		//turns
		//m_drive.driveCartesian(0, 0, -.45);
		//Timer.delay(.47);
		// m_drive.driveCartesian(0, .25, 0);
		//  Timer.delay(.75);
		//  m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(1);
		//  m_drive.driveCartesian(0, 0, -.45);
		//  Timer.delay(.47);
		//  m_drive.driveCartesian(0, .25, 0);
		//  Timer.delay(.75);
		//  m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(1);
		// m_drive.driveCartesian(0, 0, -.45);
		// Timer.delay(.47);
		// m_drive.driveCartesian(0, .25, 0);
		// Timer.delay(.75);
		// m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(1);
		// m_drive.driveCartesian(0, 0, -.45);
		// Timer.delay(.47);
		// m_drive.driveCartesian(0, .25, 0);
		// Timer.delay(.74);
		// m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(1);
		// m_drive.driveCartesian(0, 0, .45);
		// Timer.delay(.49);
		// m_drive.driveCartesian(0, .25, 0);
		// Timer.delay(3.1);
		// m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(2);
		// m_drive.driveCartesian(0, 0, .45);
		// Timer.delay(.46);
		// m_drive.driveCartesian(0, .25, 0);
		// Timer.delay(.75);
		// m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(1);
		// m_drive.driveCartesian(0, 0, -.45);
		// Timer.delay(.48);
		// m_drive.driveCartesian(0, .25, 0);
		// Timer.delay(.3);
		// m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(1);
		// m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(1);
		 m_drive.driveCartesian(-.25, 0, 0);
		 Timer.delay(2);
		 m_drive.driveCartesian(0, 0, .45);
		 Timer.delay(Math.abs(ahrs.getAngle()/200.0f));
		m_drive.driveCartesian(0, -.25, 0);
		Timer.delay(.75);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		testingDynamicCorrection(1.8f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.1);
		m_drive.driveCartesian(0, 0, .45);
		Timer.delay(Math.abs(ahrs.getAngle()/200.0f));
		m_drive.driveCartesian(0, -.25, 0);
		Timer.delay(3.5);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		m_drive.driveCartesian(-.25, 0, 0);
		Timer.delay(2.4);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.1);
		m_drive.driveCartesian(0, 0, .45);
		Timer.delay(Math.abs(ahrs.getAngle()/200.0f));
		m_drive.driveCartesian(0, -.25, 0);
		Timer.delay(.3);
		m_drive.driveCartesian(0,0,0);
		Timer.delay(1);
	    firstTime = false;
    }
  

  @Override
  public void teleopPeriodic(){
//inverse sine scaling for acceleration
	if (stick1.getRawButton(Y_BUTTON)){
		ahrs.reset();
	}
	

	  double x = stick1.getRawAxis(LEFT_JOYSTICK_HORIZONTAL)*.25;
	  double y = stick1.getRawAxis(LEFT_JOYSTICK_VERTICAL)*-1*.25;
	  //right trigger rotates clockwise, left trigger rotates counterclockwise. if both are depressed, no rotation
	  double z = (stick1.getRawAxis(RIGHT_TRIGGER) > 0 && stick1.getRawAxis(LEFT_TRIGGER) > 0) ? 0.0 : stick1.getRawAxis(RIGHT_TRIGGER) * .5 + stick1.getRawAxis(LEFT_TRIGGER) * -.5;

	  m_drive.driveCartesian(x, y, z, 0.0);

	  if (stick1.getRawButton(X_BUTTON)){
		  intake.set(.5);
	  }
	  else if (stick1.getRawButton(B_BUTTON)){
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
    if (stick2.getRawButton(A_BUTTON)) {
		//For-the-10/15-distance
		shooter.set(.4);
		  shooterLeft.set(-.4);
	  }
	  else if (stick2.getRawButton(X_BUTTON)) {
		//Farther Distances
		shooter.set(.29);
		shooterLeft.set(-.29);
	  }
	  else if (stick2.getRawButton(Y_BUTTON)){
		  //15 Distance?
		  shooter.set(.29);
		  shooterLeft.set(-.29);
	  }
	  else if (stick2.getRawButton(B_BUTTON)){
		  //25?????
		  shooter.set(.3);
		  shooterLeft.set(-.3);
	  }
	  else {
		  shooter.set(0);
		  shooterLeft.set(0);
	  }


	  if (stick2.getRawButton(RIGHT_BUMPER)){
		  hopper.set(.5);
	  }
	  else if (stick2.getRawAxis(RIGHT_TRIGGER) > 0){
		  hopper.set(-.5);
	  }
	  else{
	      hopper.set(0);
	  }

	  System.out.println(ahrs.getAngle());

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
