/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.cargo;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class RunCargoIntake extends Command {
  private double speed;
  public enum IntakeSpeed {
    FAST(1.0),
    SLOW(0.5);

    public double speed;

    IntakeSpeed(double speed) {
      this.speed = speed;
    }
  }

  public RunCargoIntake(IntakeSpeed intakeSpeed) {
    requires(Robot.cargoIntake);
    this.speed = intakeSpeed.speed;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.cargoIntake.setIntakeSpeed(speed);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.cargoShooter.isCargoInRobot();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
