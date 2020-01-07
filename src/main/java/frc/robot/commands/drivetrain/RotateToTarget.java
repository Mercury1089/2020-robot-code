/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrain;

import frc.robot.Robot;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.DriveTrain.DriveTrainSide;
import frc.robot.util.MercMath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RotateToTarget extends DegreeRotate {
    private final Logger LOG = LogManager.getLogger(RotateToTarget.class);

    public RotateToTarget() {
        super(0);
        requires(Robot.driveTrain);

        angleThresholdDeg = 1.2;

        setName("RotateToTarget DegreeRotate Command");
        LOG.info(getName() + " Constructed");
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        super.initialize();

        Robot.driveTrain.configPIDSlots(DriveTrainSide.RIGHT, DriveTrain.DRIVE_PID_SLOT, DriveTrain.DRIVE_SMOOTH_MOTION_SLOT);

        targetHeading = -MercMath.degreesToPigeonUnits(Robot.driveTrain.getLimelight().getTargetCenterXAngle());
        System.out.println("RotateToTarget initialized with angle " + Robot.driveTrain.getLimelight().getTargetCenterXAngle());

        LOG.info(getName() + " Initialized");
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        super.execute();
        LOG.info(getName() + " Executed");
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        double angleError = right.getClosedLoopError(DriveTrain.DRIVE_SMOOTH_MOTION_SLOT);

        angleError = MercMath.pigeonUnitsToDegrees(angleError);
        System.out.println(angleError);

        boolean isFinished = false;

        boolean isOnTarget = (Math.abs(angleError) < angleThresholdDeg);

        if (isOnTarget) {
            onTargetCount++;
        } else {
            if (onTargetCount > 0)
                onTargetCount = 0;
        }

        if (onTargetCount > onTargetMinCount) {
            isFinished = true;
            onTargetCount = 0;
        }

        return isFinished;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        super.end();
        LOG.info(getName() + " Ended");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        LOG.info(getName() + " Interrupted");
        this.end();
    }
}
