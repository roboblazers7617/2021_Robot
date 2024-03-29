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
import edu.wpi.first.wpilibj.Controller;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import java.lang.System;
import java.sql.Driver;
import java.sql.DriverAction;

import javax.xml.namespace.QName;

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

	AnalogInput ultrasonicsensor = new AnalogInput(0);
	AnalogInput proximitySensor = new AnalogInput(1);

	void testingDynamicCorrection(float time){
		ahrs.reset();
		for(float x=0;x<time;x+=.01){
			System.out.println(ahrs.getAngle());
			if(Math.abs(ahrs.getAngle())>0){
				m_drive.driveCartesian(.25, 0, -ahrs.getAngle()/50);
			}
			else{
				m_drive.driveCartesian(.25, 0, 0);
			}
			Timer.delay(.01);
		}
	}

	void driveCartesianRotationalCorrection(double ySpeed, double xSpeed, float time){
		ahrs.reset();
		for(float x=0;x<time;x+=.01){
			m_drive.driveCartesian(ySpeed, xSpeed, -ahrs.getAngle()/50);
			Timer.delay(.01);
		}
	}

	void printNavX2Info(){
		System.out.println("Angle: "+ahrs.getAngle());
		System.out.println("Pitch: "+ahrs.getPitch()+" Roll: "+ahrs.getRoll()+" Yaw: "+ahrs.getYaw());
		System.out.println("XAccel: "+ahrs.getWorldLinearAccelX()+" YAccel: "+ahrs.getWorldLinearAccelY()+" ZAccel: "+ahrs.getWorldLinearAccelZ());
		System.out.println("XVelocity: "+ahrs.getVelocityX()+" YVelocity: "+ahrs.getVelocityY()+" ZVelocity: "+ahrs.getVelocityZ());
		System.out.println("XDisp: "+ahrs.getDisplacementX()+" YDisp: "+ahrs.getDisplacementY()+" ZDisp: "+ahrs.getDisplacementZ());
	}

	void DRIFT(float time, float radiusratio){
		m_drive.driveCartesian(-.35, 0, .35*radiusratio);
		Timer.delay(time);
		m_drive.driveCartesian(0, 0, 0);
	}

	void DriftOtherDriection (float time, float radiusratio){
		m_drive.driveCartesian(.35, 0, -.35*radiusratio);
		Timer.delay(time);
		m_drive.driveCartesian(0, 0, 0);
	}

	// void correctAngle(){
	// 	while(Math.abs(ahrs.getAngle())>1){
	// 		System.out.println(ahrs.getAngle());
	// 		m_drive.driveCartesian(0, 0, -ahrs.getAngle()/(5*ahrs.getAngle()));
	// 		Timer.delay(.01);
	// 	}
	// }
		void SlalomPath() {
		ahrs.reset();
		m_drive.setSafetyEnabled(false);
		driveCartesianRotationalCorrection(0, 1, .25f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		driveCartesianRotationalCorrection(0, -1, .25f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.5);
		driveCartesianRotationalCorrection(-1, 0, .5f);
	//	m_drive.driveCartesian(0, 0, .45);
		//Timer.delay(Math.abs(ahrs.getAngle()/200.0f));
		m_drive.driveCartesian(0, 0, .45);
		Timer.delay(.2);
		m_drive.driveCartesian(0, 1, 0);
		Timer.delay(.5);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		driveCartesianRotationalCorrection(1, 0, .6f);
		driveCartesianRotationalCorrection(0, 1, .2f);
		driveCartesianRotationalCorrection(0, -1, .2f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		driveCartesianRotationalCorrection(-.25, 0, 2.3f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.2);
		m_drive.driveCartesian(0, -.25, 0);
		Timer.delay(.75);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		driveCartesianRotationalCorrection(.25, 0, 2.4f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.2);
		m_drive.driveCartesian(0, -.25, 0);
		Timer.delay(3.2);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		driveCartesianRotationalCorrection(-.25, 0, 2.3f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.2);
		m_drive.driveCartesian(0, -.25, 0);
		Timer.delay(.3);
		m_drive.driveCartesian(0,0,0);
		Timer.delay(10);
	    firstTime = false;
		}
		
		void BouncePath(){
			ahrs.reset();
			m_drive.setSafetyEnabled(false);
			driveCartesianRotationalCorrection(-.25, 0, 2.4f);
			m_drive.driveCartesian(0, 0, 0);
			Timer.delay(.1);
			m_drive.driveCartesian(0, -.25, 0);
			Timer.delay(.5);
			m_drive.driveCartesian(0, 0, 0);
			Timer.delay(1);
			m_drive.driveCartesian(0, .25, 0);
			Timer.delay(.25);
			driveCartesianRotationalCorrection(-.25, .3, 1.2f);
			m_drive.driveCartesian(0, .25, 0);
			Timer.delay(.05);
			driveCartesianRotationalCorrection(-.25, .3, .4f);
			m_drive.driveCartesian(0, 0, 0);
			Timer.delay(.1);
			driveCartesianRotationalCorrection(-.25, 0, 1.7f);
			m_drive.driveCartesian(0, 0, 0);
			Timer.delay(.1);
			m_drive.driveCartesian(0, -.25, 0);
			Timer.delay(2);
			m_drive.driveCartesian(0, 0, 0);
			Timer.delay(1);
			m_drive.driveCartesian(0, .25, 0);
			Timer.delay(2.5);
			m_drive.driveCartesian(0, 0, 0);
			Timer.delay(.1);
			driveCartesianRotationalCorrection(-.25, 0, 3.1f);
			m_drive.driveCartesian(0, 0, 0);
			Timer.delay(.1);
			m_drive.driveCartesian(0, -.25, 0);
			Timer.delay(2.5);
			m_drive.driveCartesian(0, 0, 0);
			Timer.delay(.1);
			m_drive.driveCartesian(0, .25, 0);
			Timer.delay(1);
			driveCartesianRotationalCorrection(-.25, 0, 2.4f);
			m_drive.driveCartesian(0, 0, 0);
		}

		void BarrelRacingPath(){
			ahrs.reset();
		m_drive.setSafetyEnabled(false);
		driveCartesianRotationalCorrection(-.45, 0, 1.9f);
		driveCartesianRotationalCorrection(-.45, -.17, .2f);
		driveCartesianRotationalCorrection(-.45, 0, .2f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(0.1);
		DRIFT(3.8f, .6f);
		//Need a return to angle function
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.5);
		m_drive.driveCartesian(0, 0, -.25);
		// Timer.delay(Math.abs(ahrs.getAngle()/150.0f));
		Timer.delay(.13);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.4);
		driveCartesianRotationalCorrection(-.45, 0, 1.4f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.1);
		driveCartesianRotationalCorrection(-.45, 0, .9f);
		m_drive.driveCartesian(0, -.25, 0);
		Timer.delay(.9);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		driveCartesianRotationalCorrection(.25, 0, 2.3f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(.2);
		m_drive.driveCartesian(0, .25, 0);
		Timer.delay(1.2);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		// driveCartesianRotationalCorrection(-.25, 0, 2.3f);
		// m_drive.driveCartesian(0, 0, 0);
		// Timer.delay(.2);
		// m_drive.driveCartesian(0, .25, 0);
		// Timer.delay(1);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		driveCartesianRotationalCorrection(-.25, 0, 4.2f);
		m_drive.driveCartesian(0, -.25, 0);
		Timer.delay(.73);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		m_drive.driveCartesian(0, 0, .36);
		Timer.delay(.355);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);
		// m_drive.driveCartesian(0, .8, 0);
		// Timer.delay(.4f);
		driveCartesianRotationalCorrection(0, .4, 1.5f);
		m_drive.driveCartesian(0, 0, 0);
		Timer.delay(1);		
		Timer.delay(100);
		m_drive.setSafetyEnabled(true);
		}

		void APath(){
			System.out.println("Nice Sensor: " + proximitySensor.getValue());
			System.out.println("Shitty Sensor: " + ultrasonicsensor.getValue());
			ahrs.reset();
			if(ultrasonicsensor.getValue()>4000&&proximitySensor.getValue()<400){
				System.out.println("Red Route");
				m_drive.driveCartesian(0, 0, 0);
				Timer.delay(.1);
				driveCartesianRotationalCorrection(0, -.55, .3f);
				driveCartesianRotationalCorrection(0, .55, .4f);
				intake.set(.5);
				m_drive.driveCartesian(0, 0, .25);
				Timer.delay(.07); 
				// m_drive.driveCartesian(0, 0, 0);
				// Timer.delay(.5); 
				driveCartesianRotationalCorrection(0, -.1, .7f);
				driveCartesianRotationalCorrection(0, -.2, 1.7f);
				driveCartesianRotationalCorrection(-.25, 0, 1.1f);
				driveCartesianRotationalCorrection(0, -.20, 1.3f);
				driveCartesianRotationalCorrection(.25, 0, 3f);
				driveCartesianRotationalCorrection(0, -.20, 1.1f);
				driveCartesianRotationalCorrection(.25, 0, .2f);
				driveCartesianRotationalCorrection(0, -1, .15f);
				m_drive.driveCartesian(0, 0, 0);
				Timer.delay(1);
			}
			else if(ultrasonicsensor.getValue()>4000&&proximitySensor.getValue()<600){
				System.out.println("Blue Route");
				driveCartesianRotationalCorrection(0, -.7, .3f);
				driveCartesianRotationalCorrection(0, .55, .4f);
				m_drive.driveCartesian(0, 0, 0);
				Timer.delay(.2);
				driveCartesianRotationalCorrection(0, -.5, .6f);
				intake.set(.5);
				driveCartesianRotationalCorrection(0, -.2, 2.3f);
				driveCartesianRotationalCorrection(.25, 0, 3.1f);
				driveCartesianRotationalCorrection(0, -.25, .8f);
				driveCartesianRotationalCorrection(-.25, 0, 1.3f);
				driveCartesianRotationalCorrection(0, -.25, .6f);
			}
		}

		
	
		void BPath(){
			System.out.println("nice sensor: " + proximitySensor.getValue());
				if(ultrasonicsensor.getValue()>4000&&proximitySensor.getValue()<500){
				driveCartesianRotationalCorrection(0, -.6, .45f);
				driveCartesianRotationalCorrection(0, .6, .55f);
				m_drive.driveCartesian(0, 0, .45);
				Timer.delay(.3);
				m_drive.driveCartesian(0, -.1, 0);
				Timer.delay(.7);
				intake.set(.5);
				m_drive.driveCartesian(0, -.25, 0);
				Timer.delay(2.3);
				m_drive.driveCartesian(0, -.15, 0);
				Timer.delay(.7);
				m_drive.driveCartesian(0, 0, 0);
				Timer.delay(.1);
				m_drive.driveCartesian(0, 0, -.45);
				Timer.delay(.4);
				m_drive.driveCartesian(0, -.35, 0); 
				Timer.delay(1.2);
				m_drive.driveCartesian(0, -.15, 0);
				Timer.delay(.7);
				m_drive.driveCartesian(0, 0, .45);
				Timer.delay(.3);
				m_drive.driveCartesian(0, -.4, 0);
				Timer.delay(.3);
				m_drive.driveCartesian(0, 0, 0);
				Timer.delay(1);
				m_drive.driveCartesian(0, 0, .45);
				Timer.delay(.2);
				m_drive.driveCartesian(0, -.4, 0);
				Timer.delay(.3);
				m_drive.driveCartesian(0, 0, 0);
				Timer.delay(1);
				}
				else if(ultrasonicsensor.getValue()>4000&&proximitySensor.getValue()<2700){
				// driveCartesianRotationalCorrection(0, -.5, 1.4f);
				driveCartesianRotationalCorrection(0, .5, .3f);
				m_drive.driveCartesian(0, 0, 0);
				Timer.delay(1);
				m_drive.driveCartesian(0, 0, 1);
				Timer.delay(.3);
				m_drive.driveCartesian(0, 0, 0);
				Timer.delay(.5);
				// driveCartesianRotationalCorrection(0, .5, 1f);
				intake.set(.5);
				m_drive.driveCartesian(-.25, 0, 0);
				Timer.delay(1.1);
				driveCartesianRotationalCorrection(0, -.2, .9f);
				m_drive.driveCartesian(.25, 0, 0);
				Timer.delay(2.4);
				driveCartesianRotationalCorrection(0, -.2, 1.5f);
				m_drive.driveCartesian(-.25, 0, 0);
				Timer.delay(2.4);
				driveCartesianRotationalCorrection(0, -.2, 1.3f);
				m_drive.driveCartesian(.25, 0, 0);
				Timer.delay(1.1);
				driveCartesianRotationalCorrection(0, -.5, .2f);
				m_drive.driveCartesian(0, 0, 0);
				Timer.delay(1);
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
	m_drive.setSafetyEnabled(false);
	APath();
	m_drive.setSafetyEnabled(true);
   
	}
		// ahrs.reset();
		// m_drive.setSafetyEnabled(false);
		// BPath();
		// for(int x=0;x<10000000;x++){
			// System.out.println(proximitySensor.getValue());
			//Path A red route  blocks + blocks
			//~550 for proximity
			//~3900 for ultra
			//Path A yellow route wall + blocks
			//~590 proximity 
			//~1700 ultra
			//Path B red route blocks + nothing 
			//~300 for proximity
			//cap 4000 for ultra
			//Path B yellow route nothing
			//2300 for proximity
			//cap 4000 ultra

			// Timer.delay(.1);
		// }
	

  

  @Override
  public void teleopPeriodic(){
//inverse sine scaling for acceleration
	if (stick1.getRawButton(Y_BUTTON)){
		ahrs.reset();
	}
	

	  double x = stick1.getRawAxis(LEFT_JOYSTICK_HORIZONTAL)*.7;
	  double y = stick1.getRawAxis(LEFT_JOYSTICK_VERTICAL)*-1*.7;
	  //right trigger rotates clockwise, left trigger rotates counterclockwise. if both are depressed, no rotation
	  double z = (stick1.getRawAxis(RIGHT_TRIGGER) > 0 && stick1.getRawAxis(LEFT_TRIGGER) > 0) ? 0.0 : stick1.getRawAxis(RIGHT_TRIGGER) * .35 + stick1.getRawAxis(LEFT_TRIGGER) * -.35;

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

	  if (stick1.getRawButton(A_BUTTON)){
		  driveCartesianRotationalCorrection(.25, 0, 2.3f);
	  }
	  else{
		  m_drive.driveCartesian(x, y, z, 0.0);
	  }

	  if (stick1.getRawButton(Y_BUTTON)){
		  driveCartesianRotationalCorrection(-.25, 0, 2.3f);
	  }
	  else {
		  m_drive.driveCartesian(x, y, z, 0.0);
	  }

	  if (stick2.getRawAxis(LEFT_TRIGGER) > 0){
		  tower.set(-.5);
	  }
	  else if (stick2.getRawButton(LEFT_BUMPER)){
		  tower.set(.5);
	  }
	  else{
		  tower.set(0);
	  }
    if (stick2.getRawButton(A_BUTTON)) {
		//Green
		hopper.set(-.5);
		Timer.delay(.1);
		shooter.set(.38);
		shooterLeft.set(-.38);
		//shooter.setVoltage(4.75);
		//shooterLeft.setVoltage(-4.75);
		Timer.delay(1.1);
		tower.set(1);
		Timer.delay(.5);
	  }
	  else if (stick2.getRawButton(Y_BUTTON)) {
		//Yellow
		hopper.set(-.5);
		Timer.delay(.15);
		shooter.set(1);
		shooterLeft.set(-1);
		// //shooter.set(.3);
		// //shooterLeft.set(-.3);
		// shooter.setVoltage(3.625);
		// shooterLeft.setVoltage(-3.625);
		Timer.delay(.73);
		tower.set(1);
		//tower.set(.5);
		Timer.delay(.73);
	  }
	  else if (stick2.getRawButton(X_BUTTON)){
		  //Blue
		hopper.set(-.6);
		Timer.delay(.17);
		  //shooter.set(.3);
		//shooterLeft.set(-.3);
		shooter.setVoltage(3.625);  
		shooterLeft.setVoltage(-3.625);
		Timer.delay(.74);
		   tower.set(.6);
		   Timer.delay(.74);

	  }
	  else if (stick2.getRawButton(B_BUTTON)){
		  //Red
		  hopper.set(-.6);
		  Timer.delay(.15);
		 // shooter.set(.31);
		  //shooterLeft.set(-.31);
		 shooter.setVoltage(3.825);
		 shooterLeft.setVoltage(-3.825);
		  Timer.delay(1);
		tower.set(.5);
		Timer.delay(.74);
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
