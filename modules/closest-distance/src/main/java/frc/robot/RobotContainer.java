// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import java.util.List;

/**
 * The closest-distance lesson lives here. {@link Robot} handles the AdvantageKit logging setup and
 * wires this class into the robot lifecycle, so you can focus on the lesson below.
 */
public class RobotContainer {
  private static final double FIELD_LENGTH_METERS = 17.55;
  private static final double FIELD_WIDTH_METERS = 8.05;

  private final List<Translation2d> fieldPoints =
      List.of(
          new Translation2d(1.0, 1.0),
          new Translation2d(3.5, 6.4),
          new Translation2d(7.2, 2.1),
          new Translation2d(10.8, 5.6),
          new Translation2d(14.5, 1.8),
          new Translation2d(16.4, 7.0));

  public RobotContainer() {}

  /**
   * Called every loop while the robot is running. Use this method for the closest-distance lesson.
   */
  public void robotPeriodic() {
    // Start working here!
  }
}
