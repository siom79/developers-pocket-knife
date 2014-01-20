package developers.pocket.knife.lifecycle;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LifeCycle {

    public void exit() {
        System.exit(0);
    }
}
