package frc.robot.loops;

import frc.lib.util.*;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import java.util.ArrayList;
import java.util.List;

public class Looper implements ILooper{

    public final double kPeriod = 0.1;

    private boolean running_;

    private final Notifier notifier_;
    private final List<Loop> loops_;
    private final Object taskRunningLock_ = new Object();
    private double timestamp_ = 0;
    private double dt_ = 0;

    private final CrashTrackingRunnable runnable_ = new CrashTrackingRunnable() {
        @Override
        public void runCrashTracked() {
            synchronized (taskRunningLock_) {
                if (running_) {
                    double now = Timer.getFPGATimestamp();
                    for (Loop loop : loops_) {
                        loop.onLoop(now);
                    }
                    dt_ = now - timestamp_;
                    timestamp_ = now;
                }
            }
        }
    };

    public Looper() {
        notifier_ = new Notifier(runnable_);
        running_ = false;
        loops_ = new ArrayList<>();
    }

  //  @Override
    public synchronized void register(Loop loop) {
        synchronized (taskRunningLock_) {
            loops_.add(loop);
        }
    }

    public synchronized void start() {
        if (!running_) {
            System.out.println("Starting loops");
            synchronized (taskRunningLock_) {
                timestamp_ = Timer.getFPGATimestamp();
                for (Loop loop : loops_) {
                    loop.onStart(timestamp_);
                }
                running_ = true;
            }
            notifier_.startPeriodic(kPeriod);
        }
    }

    public synchronized void stop() {
        if (running_) {
            System.out.println("Stopping loops");
            notifier_.stop();
            synchronized (taskRunningLock_) {
                running_ = false;
                timestamp_ = Timer.getFPGATimestamp();
                for (Loop loop : loops_) {
                    System.out.println("Stopping " + loop);
                    loop.onStop(timestamp_);
                }
            }
        }
    }

    /*
    public void outputToSmartDashboard() {
        SmartDashboard.putNumber("looper_dt", dt_);
    }    
    */

}