/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motion.TrajectoryPoint;

/**
 * Add your docs here.
 */
public class MercMotionProfile {
    private final String name;
    private final String pathDirectory;
    private final ProfileDirection direction;
    private final List<TrajectoryPoint> trajectoryPoints;

    public MercMotionProfile(String name, ProfileDirection direction) {
        this.name = name;
        this.direction = direction;
        pathDirectory = MercPathLoader.getBasePathLocation() + name + ".wpilib.json"; 
        trajectoryPoints = MercPathLoader.loadPath(name);
        if(this.direction == ProfileDirection.BACKWARDS)
            driveBackwards();
    }

    public String getName() {
        return name;
    }

    public String getPathDirectory() {
        return pathDirectory;
    }

    public List<TrajectoryPoint> getTrajectoryPoints() {
        return trajectoryPoints;
    }

    public void driveBackwards() {
        for (int i = 0; i < trajectoryPoints.size(); i++) {
            trajectoryPoints.get(i).velocity *= -1;
            trajectoryPoints.get(i).position *= -1;
            //trajectoryPoints.get(i).headingDeg =
            //(trajectoryPoints.get(i).headingDeg + 4096) % 8192;
            //System.out.println("Velocity: " + trajectoryPoints.get(i).velocity);
        }
    }

    public enum ProfileDirection{
        FORWARD,
        BACKWARDS
    }
}
