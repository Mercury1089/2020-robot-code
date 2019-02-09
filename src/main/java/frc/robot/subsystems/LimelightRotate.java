/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Servo;
import frc.robot.sensors.Limelight;
import frc.robot.RobotMap;


/**
 * Add your docs here.
 */
public class LimelightRotate extends Subsystem {
  private Servo servo;
  private Limelight limelight;

  private final double 
    FACING_HATCH_PANEL = 0.0, 
    FACING_CARGO = 1.0;

  public LimelightRotate() {
    //Initialize Servo
    servo = new Servo(RobotMap.PWM.LIMELIGHT_SERVO);
    //Initialize LimeLight
    limelight = new Limelight();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  // Not in use
  public void setServoPosition(double position){
    servo.set(position);
  }
  // Not in use
  public double getServoPosition(){
    return servo.get();
  }
  public Limelight getLimeLight() {
    return limelight;
  }
  // Not in use
  public Servo getServo(){
    return servo;
  }
}
