package cron;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import server.UserRepository.Repository;
import server.UserRepository.User;

public class Cron {
    private Repository usersRepo;
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


    public Cron(Repository usersRepo) {
        this.usersRepo = usersRepo;
        this.executor.scheduleAtFixedRate(this::updateData, 0, 5,TimeUnit.SECONDS);
    }
    
    private void updateData() {
        if (this.usersRepo.coordinator != null && this.usersRepo.users.size() > 1) {
            String data = "Coordinator INFO \n";
            for (User u : this.usersRepo.users) {
                if(u.isCoordinator) {
                    data = "(Coord.) " + u.clientID + " - PORT: " + u.port + "\n";  
                    continue;
                }
                data = data + u.requestData() + "\n";
            }
            this.usersRepo.coordinator.info = data;
        }
    }
}
