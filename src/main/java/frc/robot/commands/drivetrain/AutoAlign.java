
package frc.robot.commands.drivetrain;

import java.util.Set;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;
import frc.robot.commands.drivetrain.MoveHeading;
import frc.robot.subsystems.DriveTrain;


    
public class AutoAlign extends MoveHeading {

    private DriveTrain driveTrain;

    public AutoAlign(DriveTrain driveTrain) {
        super(0, 0, driveTrain);
    }

   // Called just before this Command runs the first time
   @Override
   public void initialize() {
       this.driveTrain.resetEncoders();
   }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {

    }
    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return false;  
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        this.driveTrain.stop();
        
    }
}