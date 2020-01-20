/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* this is definently not a class to create a school shooter                   */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.MercSparkMax;
import frc.robot.util.PIDGain;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.robot.util.interfaces.IMercMotorController;
import frc.robot.util.interfaces.IMercPIDTunable;
import frc.robot.util.interfaces.IMercShuffleBoardPublisher;
import frc.robot.RobotMap.*;

public class Shooter extends SubsystemBase implements IMercShuffleBoardPublisher, IMercPIDTunable {
  // private IMercMotorController flywheel;

  public static final double NOMINAL_OUT = 0.0, PEAK_OUT = 1.0;

  private IMercMotorController shooterLeft, shooterRight;

  private double currentSpeed;

  private ShooterMode mode;

  private PIDGain velocityGains;

  public enum ShooterMode {
    OVER_THE_TOP, THROUGH_MIDDLE
  }

  public Shooter(ShooterMode mode) {
    setName("Shooter");
    // flywheel = new MercTalonSRX(CAN.SHOOTER_FLYWHEEL);
    shooterLeft = new MercSparkMax(CAN.SHOOTER_LEFT);
    shooterRight = new MercSparkMax(CAN.SHOOTER_RIGHT);
    configVoltage(NOMINAL_OUT, PEAK_OUT);
    this.mode = mode;
    shooterLeft.setNeutralMode(NeutralMode.Coast);
    shooterRight.setNeutralMode(NeutralMode.Coast);

    if (mode == ShooterMode.OVER_THE_TOP) {
      shooterLeft.setInverted(false);
      shooterRight.setInverted(true);
    } else if (mode == ShooterMode.THROUGH_MIDDLE) {
      shooterLeft.setInverted(false);
      shooterRight.setInverted(false);
    }

    shooterRight.follow(shooterLeft);

    setRunSpeed(0.0);

    velocityGains = new PIDGain(1e-5, 2e-7, 1e-5, 0);

    shooterLeft.configPID(SHOOTER_PID_SLOTS.VELOCITY_GAINS.getValue(), velocityGains);
  }

  @Override
  public void periodic() {

  }

  public void setSpeed(double speed) {
    this.currentSpeed = speed;

    shooterLeft.setNeutralMode(NeutralMode.Coast);
    shooterRight.setNeutralMode(NeutralMode.Coast);

    shooterLeft.setSpeed(speed);
  }

  public void configVoltage(double nominalOutput, double peakOutput) {
    shooterLeft.configVoltage(nominalOutput, peakOutput);
    shooterRight.configVoltage(nominalOutput, peakOutput);
  }

  public void increaseSpeed() {
    currentSpeed += 0.05;
    this.setSpeed(currentSpeed);
  }

  public void decreaseSpeed() {
    currentSpeed -= 0.05;
    this.setSpeed(currentSpeed);
  }

  public double getRPM() {
    return shooterLeft.getEncVelocity();
  }

  public Command getDefaultCommand() {
    return CommandScheduler.getInstance().getDefaultCommand(this);
  }

  public void setDefaultCommand(Command command) {
    CommandScheduler.getInstance().setDefaultCommand(this, command);
  }

  public void setRunSpeed(double runSpeed) {
    SmartDashboard.putNumber("Shooting speed", 0.0);
  }

  public double getRunSpeed() {
    return SmartDashboard.getNumber("Shooting speed", 0.0);
  }

  public void setVelocity(double rpm) {
    // Ensures shooter is in coast mode
    shooterLeft.setNeutralMode(NeutralMode.Coast);
    shooterRight.setNeutralMode(NeutralMode.Coast);
    // Sets RPM
    shooterLeft.setVelocity(rpm);
    // shooterRight.setVelocity(rpm);
  }

  public double getRunRPM() {
    return SmartDashboard.getNumber("Set Shooter RPM", 0.0);
  }

  public ShooterMode getMode() {
    return mode;
  }

  public void publishValues() {
    SmartDashboard.putString("Shooter mode",
        getMode() == ShooterMode.OVER_THE_TOP ? "Over the top" : "Through the middle");
    SmartDashboard.putNumber("Shooter RPM", getRPM());
    
    SmartDashboard.putNumber("P Gain", velocityGains.kP);
    SmartDashboard.putNumber("I Gain", velocityGains.kI);
    SmartDashboard.putNumber("D Gain", velocityGains.kD);
    SmartDashboard.putNumber("Feed Forward", velocityGains.kF);
    SmartDashboard.putNumber("Set Shooter RPM", 0.0);
  }

  @Override
  public PIDGain getPIDGain(int slot) {
    return this.velocityGains;
  }

  @Override
  public void setPIDGain(int slot, PIDGain gain) {
    this.velocityGains = gain;

    shooterLeft.configPID(SHOOTER_PID_SLOTS.VELOCITY_GAINS.getValue(), this.velocityGains);
    shooterRight.configPID(SHOOTER_PID_SLOTS.VELOCITY_GAINS.getValue(), this.velocityGains);
  }

  @Override
  public int[] getSlots() {
    return new int[] { 0 };
  }

  public enum SHOOTER_PID_SLOTS {
    VELOCITY_GAINS(0);

    private int value;

    SHOOTER_PID_SLOTS(int value) {
      this.value = value;
    }

    public int getValue() {
      return this.value;
    }
  }
}
