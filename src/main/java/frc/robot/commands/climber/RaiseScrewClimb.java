/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Climber.ScrewMotor;
import frc.robot.util.MercMath;
import frc.robot.util.interfaces.IMercMotorController;

public class RaiseScrewClimb extends Command {

  private IMercMotorController backRight, backLeft, front;

  private final double CLIMB_DIST_INCHES = 22;
  private double setPos;

  public RaiseScrewClimb() {
    requires(Robot.climber);

    backRight = Robot.climber.getBackRight();
    backLeft = Robot.climber.getBackLeft();
    front = Robot.climber.getFront();

    setPos = MercMath.inchesToEncoderTicks(-CLIMB_DIST_INCHES);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.climber.resetEncoders();

    if (!Robot.climber.isInLiftMode())
      Robot.driveTrain.initializeMotionMagicFeedback();
    
    //onTargetCount = 0;
    //initialCheckCount = 0;

    /* Motion Magic Configurations */
    backRight.configMotionAcceleration(1000);
    backRight.configMotionCruiseVelocity(1000); //TEMP VALUE
    front.configMotionAcceleration(1000);
    front.configMotionCruiseVelocity(1000); //TEMP VALUE

    int closedLoopTimeMs = 1;
    backRight.configClosedLoopPeriod(0, closedLoopTimeMs);
    backRight.configClosedLoopPeriod(1, closedLoopTimeMs);
    front.configClosedLoopPeriod(0, closedLoopTimeMs);
    front.configClosedLoopPeriod(1, closedLoopTimeMs);

    backRight.configAuxPIDPolarity(false);
    front.configAuxPIDPolarity(false);

    Robot.climber.configPIDSlots(ScrewMotor.BACK_RIGHT, Climber.LIFT_BR_RUN, Climber.LIFT_BR_ADJUST);
    Robot.climber.configPIDSlots(ScrewMotor.FRONT, Climber.LIFT_FRONT_RUN, Climber.LIFT_FRONT_ADJUST);
    
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    backRight.set(ControlMode.MotionMagic, setPos, DemandType.AuxPID, 0);
    front.set(ControlMode.MotionMagic, setPos, DemandType.AuxPID, 0); 
  }
  
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    this.end();
  }
}
