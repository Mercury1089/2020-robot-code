/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.sensors.Limelight;

public class RotateToTarget extends DegreeRotate {
  public RotateToTarget() {
    super(Robot.limelightRotate.getLimeLight().getTargetCenterXAngle());
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    super.initialize();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    right.set(ControlMode.MotionMagic, 0, DemandType.AuxPID, Robot.limelightRotate.getLimeLight().getTargetCenterXAngle());
    left.follow(right, FollowerType.AuxOutput1);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return super.isFinished();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    super.end();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    this.end();
  }
}
